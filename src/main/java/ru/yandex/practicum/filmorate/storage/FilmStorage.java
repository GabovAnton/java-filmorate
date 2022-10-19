package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenreType;
import ru.yandex.practicum.filmorate.model.FilmRating;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getAll();

    Optional<Film> create(@Valid @RequestBody Film film);

    @Valid Optional<Film> update(@Valid @RequestBody Film film);

    @Valid Optional<Film> getByID(@Valid Integer id);

    boolean addLike(@Valid long userId, @Valid Film film);

    boolean removeLike(long userId, Film film);

    List<Film> getTopFilms(int number);

    boolean deleteFilm(int id);

    Optional<FilmGenreType> getGenreById (int id);

    List<FilmGenreType> getAllGenres();

    Optional<FilmRating> getMPAById(int id);

    List<FilmRating> getAllMPA();
}
