package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FilmGenreType;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRatingMapper implements RowMapper<FilmRating> {
    @Override
    public FilmRating mapRow(ResultSet rs, int rowNum) throws SQLException {

        switch (rs.getString("NAME")) {
            case ("G"):
                return FilmRating.G;
            case ("PG"):
                return FilmRating.PG;
            case ("PG13"):
                return FilmRating.PG13;
            case ("NC17"):
                return FilmRating.NC17;
            default: return null; //TODO исправить!
        }

    }
}
