package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getAll();

    Optional<Film> create(@Valid @RequestBody Film film);

    @Valid Optional<Film> update(@Valid @RequestBody Film film);

    @Valid Optional<Film> getByID(@Valid Integer id);

    boolean addLike(@Valid long userId, @Valid Film film);

    boolean removeLike(long userId, int filmId);

    List<Film> getTopFilms(int number);

    boolean deleteFilm(int id);

    Genre getGenreById (int id);

    List<Genre> getAllGenres();

    Mpa getMPAById(int id);

    List<Mpa> getAllMPA();
}
