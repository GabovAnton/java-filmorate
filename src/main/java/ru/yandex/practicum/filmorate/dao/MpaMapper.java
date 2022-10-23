package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaDictionary;

import java.lang.annotation.Retention;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {

        Mpa mpa = new Mpa();
        mpa.id = rs.getInt("ID");
        mpa.name = rs.getString("NAME");
        return mpa;
    }
}
