package ru.yandex.practicum.filmorate.dao;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface FilmDao {


    List<Film> getAll();

    Optional<Film> getByID(@Valid Integer id);

    @Valid Optional<Film> create(@Valid @RequestBody Film film);

    boolean addLike(@Valid long userId, @Valid Film film);
}
