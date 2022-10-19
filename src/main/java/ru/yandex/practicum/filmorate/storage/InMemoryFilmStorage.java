package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenreType;
import ru.yandex.practicum.filmorate.model.FilmRating;

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
                        throw new FilmNotFoundException("film doesn't exist");
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
    public boolean removeLike(long userId, Film film) {
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

    @Override ////TODO реализовать метод!!!!!
    public Optional<FilmGenreType> getGenreById(int id) {


        return Optional.empty();
    }

    @Override ////TODO реализовать метод!!!!!
    public List<FilmGenreType> getAllGenres() {
        return null;
    }
    public  Optional<FilmRating> getMPAById(int id) {
        return Optional.ofNullable(FilmRating.getMPA(id));

    }
    @Override
    public List<FilmRating> getAllMPA() {
        return List.of(FilmRating.values());
    }

}
