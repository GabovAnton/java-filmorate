package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MpaDictionary;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRatingMapper implements RowMapper<MpaDictionary> {
    @Override
    public MpaDictionary mapRow(ResultSet rs, int rowNum) throws SQLException {

        switch (rs.getString("NAME")) {
            case ("G"):
                return MpaDictionary.G;
            case ("PG"):
                return MpaDictionary.PG;
            case ("PG13"):
                return MpaDictionary.PG13;
            case ("NC17"):
                return MpaDictionary.NC17;
            default: return null; //TODO исправить!
        }

    }
}
