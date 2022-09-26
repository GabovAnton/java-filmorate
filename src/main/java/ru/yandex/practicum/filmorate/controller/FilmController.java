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
        return filmService.getAll();
    }

    @PostMapping()
    public @Valid Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping()
    public @Valid Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("{id}")
    public Optional<Film> getFilmById(@PathVariable int id) {
        return Optional.ofNullable(filmService.getByID(id));
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean addLike(@PathVariable int id, @PathVariable long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public boolean deleteLike(@PathVariable int id, @PathVariable long userId) {
        return filmService.removeLike(id, userId);
    }

    @DeleteMapping("{id}")
    public boolean deleteFilm(@PathVariable int id) {
        return filmService.removeFilm(id);
    }

    @GetMapping("popular")
    public List<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) @Min(1) int amount) {
        return filmService.getTopPopularFilms(amount);
    }

}
