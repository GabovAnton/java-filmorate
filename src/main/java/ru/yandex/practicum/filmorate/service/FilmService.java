package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public boolean addLike(int filmId, long userId) {
        return filmStorage.addLike(userId, filmStorage.getByID(filmId));
    }
    public List<Film> getAll(){
       return filmStorage.getAll();
    }
    public boolean removeLike(int filmId, long userId) {
        return filmStorage.removeLike(userId, filmStorage.getByID(filmId));
    }

    public List<Film> getTopPopularFilms(int number) {
      return  filmStorage.getTopFilms(number);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }
    public Film getByID(Integer id) {
        return filmStorage.getByID(id);
    }
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public boolean removeFilm (int id) {
        return filmStorage.deleteFilm(id);
    }
}
