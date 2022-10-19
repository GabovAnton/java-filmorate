package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.FilmGenreTypeMapper;
import ru.yandex.practicum.filmorate.dao.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenreType;

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
    private int batchSize = 30;

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
    @Transactional//(propagation = Propagation.REQUIRES_NEW)
    public Optional<Film> create(@Valid @RequestBody Film film, List<FilmGenreType> genres) {

        String sqlQueryInsertFilm = "insert into 'filmorate.films'(NAME, DESCRIPTION, DURATION, RELEASE_DATE, RATING_ID) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryInsertFilm, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getRating().id);
            return stmt;
        }, keyHolder);

        String sqlQueryInsertMPA = "insert into 'filmorate.film_genres'(GENRE_ID, FILM_ID) " +
                "VALUES (?, ?)  WHERE ID = ?";

        jdbcTemplate.batchUpdate(
                sqlQueryInsertMPA,
                genres,
                batchSize,
                (ps, argument) -> {
                    ps.setInt(1, argument.getId());
                    ps.setInt(2, Objects.requireNonNull(keyHolder.getKey()).intValue());
                    ps.setInt(3, Objects.requireNonNull(keyHolder.getKey()).intValue());
                });


        return getByID(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public boolean addLike(@Valid long userId, @Valid Film film) {
        String sqlQuery = "insert into 'filmorate.film_likes'(USER_ID, FILM_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(
                sqlQuery, userId, film.getId());
        return true;
    }

    @Override
    public boolean removeLike(long userId, Film film) {
        String sqlQuery = "delete from 'filmorate.film_likes' where USER_ID = ? and FILM_ID = ?";

        return jdbcTemplate.update(sqlQuery, userId, film.getId()) > 0;
    }

    @Override
    public List<Film> getTopFilms(int number) {
        StringBuilder sqlQueryBuilder = new StringBuilder();
        sqlQueryBuilder.append("SELECT F.*");
        sqlQueryBuilder.append("FROM PUBLIC.\"filmorate.films\" AS F");
        sqlQueryBuilder.append("JOIN PUBLIC.\"filmorate.film_likes\" ffl ON F.ID = FFL.FILM_ID");
        sqlQueryBuilder.append("GROUP BY F.ID");
        sqlQueryBuilder.append("ORDER BY COUNT(ffl.USER_ID) DESC");
        sqlQueryBuilder.append("LIMIT ?");

        return jdbcTemplate
                .query(sqlQueryBuilder.toString(), new FilmMapper(), number);
    }

    @Override
    public Optional<FilmGenreType> getGenreById(int id) {
        String sqlQuery = "SELECT * FROM  'filmorate.genres' WHERE id = ?";

        return jdbcTemplate
                .query(sqlQuery, new FilmGenreTypeMapper(), new Object[]{id})
                .stream().findAny();

    }

    @Override
    public List<FilmGenreType> getAllGenres() {
        return jdbcTemplate
                .query("SELECT * FROM  'filmorate.genres'", new FilmGenreTypeMapper());
    }

    boolean deleteFilm(int id) {
        String sqlQuery = "delete from 'filmorate.films' where ID = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }


}

