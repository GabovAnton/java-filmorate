package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public @Valid Optional<Film> create(@Valid @RequestBody FilmDBFormat filmDBFormat) throws JsonProcessingException {

        Optional<Film> newFilm = filmService.create(filmDBFormat.getFilm());
        log.debug("new film created: {}", newFilm);
        if (newFilm.isPresent()) {
            log.debug("new film created: {}", newFilm);
        }
        return newFilm;
    }

    @PutMapping()
    public  Optional<Film> update(@Valid @RequestBody Film film) {
        Optional<Film> updatedFilm = filmService.update(film);
         if (updatedFilm.isPresent()) {
             log.debug("film updated: {} ->  {}", film, updatedFilm);
         }
        return updatedFilm;
    }

    @GetMapping("{id}")
    public @Valid  Film getFilmById(@PathVariable int id) {
        Film requestedFilm = filmService.getByID(id);
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

    private static class FilmDBFormat {
        @NotBlank
        @Size(max = 100)
        public String name;
        @NotNull
        public String releaseDate;
        @Size(max = 200)
        public String description;
        @NonNull
        @Positive
        public int duration;
        @NonNull
        @Positive
        @Value("${some.key:0}")
        public int rate;
        @NotNull
        public Mpa mpa;

        public ArrayList<Genre> genres;

        Film getFilm() {
            Film film = new Film();
            film.setName(name);
            film.setReleaseDate(LocalDate.parse(releaseDate));
            film.setDuration(duration);
            film.setMpa(mpa);
            film.setDescription(description);
            film.setRate(rate);
            if (genres != null) {
                film.setGenres(genres);
            }
            return  film;
        }
    }



}
