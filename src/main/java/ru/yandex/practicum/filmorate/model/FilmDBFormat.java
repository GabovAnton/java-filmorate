package ru.yandex.practicum.filmorate.model;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;

public  class FilmDBFormat {

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

    public Film getFilm() {
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
