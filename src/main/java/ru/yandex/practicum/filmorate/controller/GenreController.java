package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenreType;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {
    private final FilmService filmService;

    public GenreController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> getAll() {
        List<Film> all = filmService.getAll();
        log.debug("all films requested: {}", all.size());
        return all;
    }
    @GetMapping("{id}")
    public Optional<FilmGenreType> getByID(@PathVariable int id) {
        Optional<FilmGenreType> requestedGenre = filmService.getGenreById(id);
        log.debug("film genre with id: {} requested, returned result: {}", id, requestedGenre);
        return requestedGenre;
    }
}

