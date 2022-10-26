package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDAODb") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public boolean addLike(int filmId, long userId) {
        boolean result = filmStorage.addLike(userId,
                filmStorage.getByID(filmId).orElseThrow(() -> {
                    log.debug("film with id: {} doesn't exist, ", filmId);
                    throw new EntityNotFoundException("film id: " + filmId + "doesn't exist");
                }));
       if (!result) {
           log.debug("error while adding like to film with id: {} from user with id: {} ", filmId, userId);
       }
        return result;

    }

    public List<Film> getAll() {
        List<Film> allFilms = filmStorage.getAll();
        log.debug("all films requested, returned: {}", allFilms.size());
        return allFilms;
    }

    public boolean removeLike(int filmId, long userId) {
        if (filmStorage.removeLike(userId, filmId)) {
            log.debug("like removed  successfully from film with id: {} and user with id: {} ", filmId, userId);
            return true;
        } else {
            log.debug("error while removing like to film with id: {} from user with id: {} ", filmId, userId);
            throw new EntityNotFoundException("like for filmId: " + filmId + " and userId: " + userId + " not found");
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
                .getByID(id).orElseThrow(() -> new EntityNotFoundException("film with id: " + id + " doesn't exist"));

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
