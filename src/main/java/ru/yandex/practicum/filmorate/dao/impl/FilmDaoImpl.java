package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component("FilmDAODb")
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate
                .query("SELECT * FROM  'filmorate.films'", new FilmMapper());
    }

    @Override
    public Optional<Film> getByID(@Valid Integer id) {
        return jdbcTemplate
                .query("SELECT * FROM  'filmorate.films' WHERE id = ?", new FilmMapper(), new Object[]{id})
                .stream().findAny();
    }

    @Override
    @Valid
    public Optional<Film> create(@Valid @RequestBody Film film) {
        String sqlQuery = "insert into 'filmorate.films'(NAME, DESCRIPTION, DURATION, RELEASE_DATE, RATING) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(3, film.getDuration());
            return stmt;
        }, keyHolder);

        return getByID(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public boolean addLike(@Valid long userId, @Valid Film film) {
//

       return true;
    }


}

