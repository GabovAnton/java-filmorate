package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenreType;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public boolean addLike(int filmId, long userId) {
        return filmStorage.addLike(userId,
                filmStorage.getByID(filmId).orElseThrow(() -> {
            throw new FilmNotFoundException("film id: " + filmId + "doesn't exist");}));
    }
    public List<Film> getAll(){
       return filmStorage.getAll();
    }
    public boolean removeLike(int filmId, long userId) {
        return filmStorage.removeLike(userId,
                filmStorage.getByID(filmId).orElseThrow(() -> {
                    throw new FilmNotFoundException("film id: " + filmId + "doesn't exist");}));
    }

    public List<Film> getTopPopularFilms(int number) {
      return  filmStorage.getTopFilms(number);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }
    public Optional<Film> getByID(Integer id) {
        return filmStorage.getByID(id);
    }
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public boolean removeFilm (int id) {
        return filmStorage.deleteFilm(id);
    }

    public Optional<FilmGenreType> getGenreById (int id) {
        return filmStorage.getGenreById(id);
    }
    public  Optional<FilmRating> getMPAById(int id) {
        return filmStorage.getMPAById(id);
    }

    public List<FilmGenreType> getAllGenres(){
        return filmStorage.getAllGenres();
    }

    public List<FilmRating> getAllMPA(){
        return filmStorage.getAllMPA();
    }

}
