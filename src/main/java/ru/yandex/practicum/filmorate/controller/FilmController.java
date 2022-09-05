package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.HashSet;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final HashSet<Film> films = new HashSet<>();
    private static int id = 0;

    @GetMapping()
    public HashSet<Film> findAll() {
        log.debug("current films amount: {}", films.size());
        return films;
    }

    @PostMapping()
    public @Valid Film create(@Valid @RequestBody Film film) {
        if (ifRealiseDateMatchCriteria(film.getReleaseDate())) {
            film.setId(++id);


            films.stream().filter(x -> x.equals(film))
                    .findAny().ifPresentOrElse(u -> {
                        throw new ValidationException("film already exists");
                    }, () -> films.add(film));

            log.debug("film {} successfully added", film);
            return film;
        } else {
            log.debug("provided film release date doesn't match minimum criteria: {} ", film.getReleaseDate());
            throw new FilmorateValidationException("Provided release date: '" + film.getReleaseDate()
                    + "' doesn't match criteria");
        }

    }

    private boolean ifRealiseDateMatchCriteria(LocalDate date) {
        return date != null && date.isAfter(LocalDate.of(1895, 12, 27));
    }

    @PutMapping()
    public @Valid Film update(@Valid @RequestBody Film film) {

        if (ifRealiseDateMatchCriteria(film.getReleaseDate())) {
            films.stream().filter(x -> x.getId() == film.getId())
                    .findAny().ifPresentOrElse(f -> {
                        films.remove(f);
                        films.add(film);
                    }, () -> {
                        throw new ValidationException("film doesn't exist");
                    });
            log.debug("film {} successfully updated", film);

            return film;

        } else {
            log.debug("provided film release date doesn't match minimum criteria: {} ", film.getReleaseDate());
            throw new ValidationException("Provided release date: '" + film.getReleaseDate()
                    + "' doesn't match criteria");
        }

    }
}
