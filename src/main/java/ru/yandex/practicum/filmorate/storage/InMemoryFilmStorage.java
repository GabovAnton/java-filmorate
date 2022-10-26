package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.model.*;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashSet<Film> films = new HashSet<>();
    private static int id = 0;

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films);
    }

    private boolean ifRealiseDateMatchCriteria(LocalDate date) {
        return date != null && date.isAfter(LocalDate.of(1895, 12, 27));
    }

    @Override
    public Optional<Film> create(Film film) {
        if (ifRealiseDateMatchCriteria(film.getReleaseDate())) {
           int newFilmId = ++id;
           film.setId(newFilmId);
            films.stream().filter(x -> x.equals(film))
                    .findAny().ifPresentOrElse(u -> {
                        throw new ValidationException("film already exists");
                    }, () -> films.add(film));

            log.debug("film {} successfully added", film);
            return getByID(newFilmId);
        } else {
            log.debug("provided film release date doesn't match minimum criteria: {} ", film.getReleaseDate());
            throw new FilmorateValidationException("Provided release date: '" + film.getReleaseDate()
                    + "' doesn't match criteria");
        }
    }

    @Override
    public Optional<Film> update(Film film) {
        if (ifRealiseDateMatchCriteria(film.getReleaseDate())) {
            films.stream().filter(x -> x.getId() == film.getId())
                    .findAny().ifPresentOrElse(f -> {
                        films.remove(f);
                        films.add(film);
                    }, () -> {
                        throw new EntityNotFoundException("film doesn't exist");
                    });
            log.debug("film {} successfully updated", film);

            return getByID(film.getId());

        } else {
            log.debug("provided film release date doesn't match minimum criteria: {} ", film.getReleaseDate());
            throw new ValidationException("Provided release date: '" + film.getReleaseDate()
                    + "' doesn't match criteria");
        }
    }

    @Override
    public Optional<Film> getByID(Integer id) {
        return films.stream().filter(x -> x.getId() == id)
                .findAny();
    }

    @Override
    public boolean addLike(long userId, Film film) {
        film.addLike(userId);
        return true;
    }

    @Override
    public boolean removeLike(long userId, int filmId) {
        Film film = getByID(filmId)
                .stream()
                .findAny()
                .orElseThrow(()->new EntityNotFoundException("film with id +" + filmId + " not found" ));
        return film.removeLike(userId);
    }

    @Override
    public List<Film> getTopFilms(int number) {
        return films.stream()
                .sorted(Comparator.comparingInt(x -> x.getLikes().values().stream().mapToInt(y -> y).sum()))
                .sorted(Comparator.comparingInt(Film::getId).reversed())
                .limit(number)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteFilm(int id) {
        films.remove(getByID(id));
        return true;
    }

    public Genre getGenreById(int id) {
        return Arrays.stream(GenreDictionary.values())
                .filter(x->x.id == id)
                .map(genre -> new Genre(genre.id, genre.name()))
                .findAny().orElseThrow(()->new EntityNotFoundException(String.format("genre with id: %d not found", id )));

    }

    public List<Genre> getAllGenres() {
        return Arrays.stream(GenreDictionary.values())
                .map(genre -> new Genre(genre.id, genre.name())).collect(Collectors.toList());
    }

    public Mpa getMPAById(int id) {
        return Arrays.stream(MpaDictionary.values())
                .filter(x->x.id == id)
                .map(mpa -> new Mpa(mpa.id, mpa.name()))
                .findAny().orElseThrow(()->new EntityNotFoundException(String.format("mpa with id: %d not found", id )));

    }
    @Override
    public List<Mpa> getAllMPA() {
        return Arrays.stream(MpaDictionary.values())
                .map(mpa -> new Mpa(mpa.id, mpa.name())).collect(Collectors.toList());
    }

}
