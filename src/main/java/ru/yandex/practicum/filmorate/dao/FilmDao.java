package ru.yandex.practicum.filmorate.dao;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenreType;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface FilmDao {


    List<Film> getAll();

    Optional<Film> getByID(@Valid Integer id);


    @Valid
    @Transactional
    Optional<Film> create(@Valid @RequestBody Film film, List<FilmGenreType> genres);

    boolean addLike(@Valid long userId, @Valid Film film);

    boolean removeLike(long userId, Film film);

    List<Film> getTopFilms(int number);
}
