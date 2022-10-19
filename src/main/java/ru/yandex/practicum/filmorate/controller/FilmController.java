package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> getAll() {
        List<Film> all = filmService.getAll();
        log.debug("all films requested: {}", all.size());
        return all;
    }

    @PostMapping()
    public @Valid Film create(@Valid @RequestBody Film film) {
        Film newFilm = filmService.create(film);
        log.debug("new film created: {}", newFilm);
        return newFilm;
    }

    @PutMapping()
    public @Valid Film update(@Valid @RequestBody Film film) {
        Film updatedFilm = filmService.update(film);
        log.debug("film updated: {} ->  {}", film, updatedFilm);
        return updatedFilm;
    }

    @GetMapping("{id}")
    public Optional<Film> getFilmById(@PathVariable int id) {
        Optional<Film> requestedFilm = filmService.getByID(id);
        log.debug("film with id: {} requested, returned result: {}", id, requestedFilm);
        return requestedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean addLike(@PathVariable int id, @PathVariable long userId) {
        boolean result = filmService.addLike(id, userId);
        log.debug("user with id: {} added like to film: {}" + (result ? "successfully" : "with error"), userId, id);
        return result;
    }

    @DeleteMapping("{id}/like/{userId}")
    public boolean deleteLike(@PathVariable int id, @PathVariable long userId) {
        boolean result = filmService.removeLike(id, userId);
        log.debug("user with id: {} deleted like from film: {} " + (result ? "successfully" : "with error"), userId, id);
        return result;
    }

    @DeleteMapping("{id}")
    public boolean deleteFilm(@PathVariable int id) {
        boolean result = filmService.removeFilm(id);
        log.debug("film with id: {} has been deleted" + (result ? "successfully" : "with error"), id);
        return result;
    }

    @GetMapping("popular")
    public List<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) @Min(1) int amount) {
        List<Film> topPopularFilms = filmService.getTopPopularFilms(amount);
        log.debug("top popular films requested: {}", topPopularFilms.size());

        return topPopularFilms;
    }

}
