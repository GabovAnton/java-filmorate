package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FilmMapper implements RowMapper<Film> {
    private List<Genre> genres;

    public FilmMapper(List<Genre> genres) {
        this.genres = genres;
    }

    public FilmMapper() {
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setDuration(rs.getInt("DURATION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        Mpa mpa = new Mpa(rs.getInt("RATING_ID"), rs.getString("RATING_NAME"));

        film.setMpa(mpa);
        if (genres != null) {
            film.setGenres(genres);
        }
        return film;
    }
}
