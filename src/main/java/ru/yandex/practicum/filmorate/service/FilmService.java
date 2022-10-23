package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaDictionary;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDAODb") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public boolean addLike(int filmId, long userId) {
        return filmStorage.addLike(userId,
                filmStorage.getByID(filmId).orElseThrow(() -> {
                    throw new FilmNotFoundException("film id: " + filmId + "doesn't exist");
                }));
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public boolean removeLike(int filmId, long userId) {
        if (filmStorage.removeLike(userId, filmId)) {
            return true;
        } else {
            throw new LikeNotFoundException("like for filmId: " + filmId + " and userId: " + userId + " not found");
        }
    }

    public List<Film> getTopPopularFilms(int number) {
        return filmStorage.getTopFilms(number);
    }

    public Optional<Film> create(Film film) {
        return filmStorage.create(film);
    }

    public Film getByID(Integer id) {
        return filmStorage
                .getByID(id).orElseThrow(() -> new FilmNotFoundException("film with id: " + id + " doesn't exist"));

    }

    public @Valid Optional<Film> update(Film film) {
        return filmStorage.update(film);
    }

    public boolean removeFilm(int id) {
        return filmStorage.deleteFilm(id);
    }

    public Genre getGenreById(int id) {
        return filmStorage.getGenreById(id);
    }

    public Mpa getMPAById(int id) {
        return filmStorage.getMPAById(id);
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public List<Mpa> getAllMPA() {
        return filmStorage.getAllMPA();
    }

}
