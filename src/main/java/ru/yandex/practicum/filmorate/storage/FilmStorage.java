package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    @Valid Film create(@Valid @RequestBody Film film);

    @Valid Film update(@Valid @RequestBody Film film);

    @Valid Film getByID(@Valid Integer id);

    boolean addLike(@Valid long userId, @Valid Film film);

    boolean removeLike(long userId, Film film);

    List<Film> getTopFilms(int number);

    boolean deleteFilm(int id);
}
