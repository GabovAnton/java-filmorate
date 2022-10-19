package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FilmGenreType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRatingMapper implements RowMapper<FilmGenreType> {
    @Override
    public FilmGenreType mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmGenreType filmGenreType = new FilmGenreType();
        filmGenreType.setName(rs.getString("NAME"));
        filmGenreType.setId(rs.getInt("ID"));
        return filmGenreType;
    }
}
