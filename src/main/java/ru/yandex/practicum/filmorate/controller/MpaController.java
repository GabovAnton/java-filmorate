package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenreType;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {
    private final FilmService filmService;

    public MpaController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<FilmRating> getAll() {
        List<FilmRating> all = filmService.getAllMPA();
        log.debug("all film ratings requested: {}", all.size());
        return all;
    }
    @GetMapping("{id}")
    public Optional<FilmRating> getMPAByID(@PathVariable int id) {
        Optional<FilmRating> requestedRating = filmService.getMPAById(id);
        log.debug("film rating with id: {} requested, returned result: {}", id, requestedRating);
        return requestedRating;
    }
}



