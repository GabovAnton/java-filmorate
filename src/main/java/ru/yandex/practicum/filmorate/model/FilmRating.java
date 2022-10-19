package ru.yandex.practicum.filmorate.model;

public enum FilmRating {
    G(1),
    PG(2),
    PG13(3),
    NC17(4);
    public int id;

    FilmRating(int id) {
        this.id = id;
    }

    public static FilmRating getMPA(int id) {
        FilmRating[] c = new FilmRating[FilmRating.values().length];
        c = FilmRating.values();
        return c[id];
    }
}
