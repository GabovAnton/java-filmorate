package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
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

        String sqlQuery = "insert into \"filmorate.users\"( NAME, LOGIN, EMAIL, BIRTHDAY) " +
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

        return getById(keyHolder.getKey().longValue());
    }

    @Override
    public Optional<User> update(@Valid @RequestBody User user) {
        String sqlQuery = "UPDATE  \"filmorate.users\" SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ?" +
                "WHERE ID = ?";

         jdbcTemplate
                .update(sqlQuery, user.getName(), user.getLogin(),user.getEmail(), user.getBirthday(), user.getId());
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
        String sqlQuery = "delete from \"filmorate.users\" where ID = ?";
        return jdbcTemplate.update(sqlQuery, userId) > 0;    }

    @Override
    public Optional<User> getById(long userId) {
        return jdbcTemplate
                .query("SELECT * FROM  \"filmorate.users\" WHERE id = ?", new UserMapper(), new Object[]{userId})
                .stream().findAny();    }


}
