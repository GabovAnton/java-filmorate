package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dao.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("UserDAODb")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<User> getAll() {
        return jdbcTemplate
                .query("SELECT * FROM  \"filmorate.users\"", new UserMapper());
    }

    @Override
    @Transactional
    public Optional<User> create(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        String sqlQuery = "INSERT INTO  \"filmorate.users\"( NAME, LOGIN, EMAIL, BIRTHDAY) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        int createdUserId = keyHolder.getKey().intValue();
        return getById(createdUserId);
    }

    @Override
    public Optional<User> update(@Valid @RequestBody User user) {
        String sqlQuery = "UPDATE  \"filmorate.users\" SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ?" +
                "WHERE ID = ?";

        jdbcTemplate
                .update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        return getById(user.getId());

    }

    @Override
    public Optional<User> getByEmail(@RequestBody String email) {
        return jdbcTemplate
                .query("SELECT * FROM  \"filmorate.users\" WHERE EMAIL = ?", new UserMapper(), new Object[]{email})
                .stream().findAny();
    }

    @Override
    public boolean delete(long userId) {
        String sqlQuery = "DELETE  FROM \"filmorate.users\" where ID = ?";
        return jdbcTemplate.update(sqlQuery, userId) > 0;
    }

    @Override
    public List<User> getUserFriends(long id) {
        String query = "SELECT F.* "+
                " FROM \"filmorate.users\" AS U " +
                " JOIN \"filmorate.friends\" AS FR ON FR.USER_ID =U.ID " +
                " JOIN \"filmorate.users\" AS F ON F.ID  =FR.FOREIGN_USER_ID " +
                " WHERE FR.STATUS = 1 AND U.ID = ?  ";
        List<User> friends =  jdbcTemplate.query(
                query,
                new UserMapper(), new SqlParameterValue(Types.INTEGER,  id));

        return friends;
    }

    @Override
    public boolean addFriend(long friendIdOne, long friendIdTwo) {

        String query = "INSERT INTO \"filmorate.friends\" (USER_ID, FOREIGN_USER_ID, STATUS) " +
                " VALUES(SELECT ID FROM PUBLIC.\"filmorate.users\" AS U  WHERE U.ID = ? , "   +
                " SELECT ID FROM PUBLIC.\"filmorate.users\"  AS UF WHERE UF.ID = ?, 1) ";

        return jdbcTemplate.update(query, friendIdOne, friendIdTwo) > 0;

    }

    @Override
    public List<User> getMutualFriends(long userOneId, long userTwoId) {
String query = "SELECT DISTINCT  U.*  " +
        " FROM " +
        " \"filmorate.friends\" "+
        " AS F "+
        " JOIN " +
        " \"filmorate.users\" "+
        " AS U ON U.ID  = F.FOREIGN_USER_ID " +
        " WHERE F.USER_ID = ? AND F.STATUS = 1 AND F.FOREIGN_USER_ID IN  " +
        " (SELECT FR.FOREIGN_USER_ID " +
        " FROM " +
        " \"filmorate.friends\" "+
        " AS FR " +
        " WHERE FR.USER_ID = ? AND FR.STATUS = 1) ";




        return jdbcTemplate.query(
                query,
                new UserMapper(), userOneId, userTwoId);
    }

    @Override
    public Optional<User> getById(long userId) {
        Optional<User>  user =jdbcTemplate
                .query("SELECT * FROM  \"filmorate.users\" WHERE id = ?", new UserMapper(), new Object[]{userId})
                .stream().findAny();
        return user;
    }
    @Override
    public boolean removeFriend(long friendIdOne, long friendIdTwo) {
        String sqlQuery = "DELETE  FROM \"filmorate.friends\" WHERE USER_ID = ? AND FOREIGN_USER_ID = ?";

        return jdbcTemplate.update(sqlQuery, friendIdOne, friendIdTwo) > 0;
    }
}