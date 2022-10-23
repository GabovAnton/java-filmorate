package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaDictionary;
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
    public List<Mpa> getAll() {
        List<Mpa> all = filmService.getAllMPA();
        log.debug("all film ratings requested: {}", all.size());
        return all;
    }
    @GetMapping("{id}")
    public Mpa getMPAByID(@PathVariable int id) {
        Mpa mpa = filmService.getMPAById(id);
        log.debug("film rating (MPA) with id: {} requested, returned result: {}", id, mpa);
        return mpa;
    }
}



