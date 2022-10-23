package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dao.FilmGenreTypeMapper;
import ru.yandex.practicum.filmorate.dao.FilmMapper;
import ru.yandex.practicum.filmorate.dao.FilmRatingMapper;
import ru.yandex.practicum.filmorate.dao.MpaMapper;
import ru.yandex.practicum.filmorate.exception.DeleteRowException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.MPANotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaDictionary;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component("FilmDAODb")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private int batchSize = 30;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() {

        List<Film> films = jdbcTemplate
                .query("SELECT F.*, R.NAME AS RATING_NAME " +
                        " FROM \"filmorate.films\" AS F " +
                        " JOIN  \"filmorate.ratings\" AS R ON R.ID =F.RATING_ID ", new FilmMapper());

        films.forEach(x -> x.setGenres(getGenres(x.getId())));

        return films;
    }

    private List<Genre> getGenres(int filmId) {
        return new ArrayList<>(jdbcTemplate
                .query("SELECT * FROM  \"filmorate.film_genres\" AS FG " +
                                " JOIN \"filmorate.genres\" AS G ON FG.GENRE_ID = G.ID " +
                                " WHERE FILM_ID = ? ",
                        new FilmGenreTypeMapper(),
                        new Object[]{filmId}));


    }

    @Override
    public Optional<Film> getByID(@Valid Integer id) {

        return jdbcTemplate
                .query("SELECT F.*, R.NAME AS RATING_NAME " +
                                " FROM \"filmorate.films\" AS F " +
                                " JOIN  \"filmorate.ratings\" AS R ON R.ID =F.RATING_ID " +
                                " WHERE F.ID = ?",
                        new FilmMapper(getGenres(id)), new Object[]{id})
                .stream().findAny();
    }

    @Override
    @Valid
    public Optional<Film> create(@Valid @RequestBody Film film) {

        String sqlQueryInsertFilm = "INSERT INTO \"filmorate.films\" (NAME, DESCRIPTION, DURATION, RELEASE_DATE, RATING_ID) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryInsertFilm, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getMpa().id);
            return stmt;
        }, keyHolder);

        int FilmId = Objects.requireNonNull(keyHolder.getKey()).intValue();

        if (film.getGenres() != null) {
            String sqlQueryInsertGenres = "INSERT INTO \"filmorate.film_genres\" (GENRE_ID, FILM_ID) " +
                    "VALUES (?, ?)";

            List<Integer> genres = film.getGenres().stream().map(x -> x.id).collect(Collectors.toList());

            jdbcTemplate.batchUpdate(sqlQueryInsertGenres,
                    genres,
                    30,
                    (PreparedStatement ps, Integer genreId) -> {
                        ps.setInt(1, genreId);
                        ps.setInt(2, FilmId);
                    });
        }

        return getByID(FilmId);
    }


    @Override
    @Valid
    public Optional<Film> update(Film film) {
        String sqlQueryUpdateFilm = "UPDATE  \"filmorate.films\" SET NAME =?, " +
                "DESCRIPTION =?, DURATION = ?, RELEASE_DATE = ?, RATING_ID = ?" +
                " WHERE ID = ?";


        if (jdbcTemplate
                .update(sqlQueryUpdateFilm, film.getName(), film.getDescription(),
                        film.getDuration(), Date.valueOf(film.getReleaseDate()), film.getMpa().id, film.getId()) > 0) {


            String DeleteFilmGenresQuery = "DELETE FROM \"filmorate.film_genres\" WHERE FILM_ID = ? ";

            jdbcTemplate.update(DeleteFilmGenresQuery, film.getId());


            if (film.getGenres() != null) {

                String sqlQueryInsertGenres = "INSERT INTO \"filmorate.film_genres\" (GENRE_ID, FILM_ID)  " +
                        "VALUES (?, ?)";

                List<Integer> genres = film.getGenres().stream().map(x -> x.id).distinct().collect(Collectors.toList());

                jdbcTemplate.batchUpdate(sqlQueryInsertGenres,
                        genres,
                        30,
                        (PreparedStatement ps, Integer genreId) -> {
                            ps.setInt(1, genreId);
                            ps.setInt(2, film.getId());
                        });
            }

            return getByID(film.getId());
        } else {
            throw new FilmNotFoundException("can't update film with id:  " + film.getId());
        }


    }

    @Override
    public boolean addLike(@Valid long userId, @Valid Film film) {
        String sqlQuery = "INSERT INTO \"filmorate.film_likes\"(USER_ID, FILM_ID) " +
                " VALUES (?, ?)";
        jdbcTemplate.update(
                sqlQuery, userId, film.getId());
        return jdbcTemplate.update(sqlQuery, userId, film.getId()) > 0;

    }

    @Override
    public boolean removeLike(long userId, int filmId) {
        String sqlQuery = "DELETE FROM \"filmorate.film_likes\" WHERE USER_ID = ? AND FILM_ID = ?";

        return jdbcTemplate.update(sqlQuery, userId, filmId) > 0;
    }

    @Override
    public List<Film> getTopFilms(int number) {

        String query = "SELECT F.*,  R.NAME AS RATING_NAME " +
                " FROM PUBLIC.\"filmorate.films\" AS F " +
                " JOIN  \"filmorate.ratings\" AS R ON R.ID =F.RATING_ID " +
                " LEFT JOIN PUBLIC.\"filmorate.film_likes\" ffl ON F.ID = FFL.FILM_ID " +
                " GROUP BY F.ID " +
                " ORDER BY COUNT(ffl.USER_ID) DESC " +
                " LIMIT ? ";
        List<Film> films = jdbcTemplate
                .query(query, new FilmMapper(), number);
        films.forEach(x -> x.setGenres(getGenres(x.getId())));
        return films;
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "SELECT * FROM  \"filmorate.genres\" WHERE id = ?";

        return jdbcTemplate
                .query(sqlQuery, new FilmGenreTypeMapper(), new Object[]{id})
                .stream()
                .findAny()
                .orElseThrow(() -> new GenreNotFoundException("genre with id: " + id + " not found"));


    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate
                .query("SELECT * FROM  \"filmorate.genres\"  ORDER BY  ID", new FilmGenreTypeMapper());
    }

    @Override
    public List<Mpa> getAllMPA() {
        return jdbcTemplate
                .query("SELECT * FROM  \"filmorate.ratings\"", new MpaMapper());
    }

    @Override
    public Mpa getMPAById(int id) {
        String sqlQuery = "SELECT * FROM  \"filmorate.ratings\" WHERE id = ?";

        return jdbcTemplate
                .query(sqlQuery, new MpaMapper(), new Object[]{id})
                .stream().findAny().orElseThrow(() -> new MPANotFoundException("MPA with id: " + id + " not found"));

    }

    @Override
    public boolean deleteFilm(int id) {
        String sqlQuery = "DELETE FROM \"filmorate.films\" where ID = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }


}

