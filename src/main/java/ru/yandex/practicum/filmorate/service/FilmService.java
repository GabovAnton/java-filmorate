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

        log.debug("adding like to film with id: {} from user with id: {}", result ? "added successfully" :
                "can't be added because of error", filmId, userId );

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
        List<Film> topFilms = filmStorage.getTopFilms(number);
        log.debug("all films requested, returned: {}", topFilms.size());

        return topFilms;
    }

    public Optional<Film> create(Film film) {
        Optional<Film> filmCreated = filmStorage.create(film);
        log.debug("film: {}", filmCreated.isPresent() ? "created successfully" : "can't be created", film );
        return filmCreated;

    }

    public Film getByID(Integer id) {

        Film film = filmStorage
                .getByID(id).orElseThrow(() -> new EntityNotFoundException("film with id: " + id + " doesn't exist"));
        log.debug("film id: {} requested, returned {}" , id,  film);
        return film;

    }

    public @Valid Optional<Film> update(Film film) {
        Optional<Film> updatedFilm = filmStorage.update(film);
        log.debug("film: {}", updatedFilm.isPresent() ? "updated successfully" : "can't be updated", film );

        return updatedFilm;
    }

    public boolean removeFilm(int id) {
        boolean result = filmStorage.deleteFilm(id);
        log.debug("film id: {}", result ? "deleted successfully" : "can't be deleted", id );
        return result;
    }

    public Genre getGenreById(int id) {
        Genre genre = filmStorage.getGenreById(id);
        log.debug("genre with id: {} requested, returned : {}", id, genre);

        return genre;
    }

    public Mpa getMPAById(int id) {
        Mpa mpa = filmStorage.getMPAById(id);
        log.debug("mpa with id: {} requested, returned : {}", id, mpa);
        return mpa;
    }

    public List<Genre> getAllGenres() {
        List<Genre> allGenres = filmStorage.getAllGenres();
        log.debug("all genres requested, returned : {}", allGenres.size());

        return allGenres;
    }

    public List<Mpa> getAllMPA() {

        List<Mpa> allMPA = filmStorage.getAllMPA();
        log.debug("all mpa requested, returned : {}", allMPA.size());

        return allMPA;
    }

}
