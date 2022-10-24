package ru.yandex.practicum.filmorate.model;

import java.util.Arrays;

public enum GenreDictionary {
    Комедия(1),
    Драма(2),
    Мультфильм(3),
    Триллер(4),
    Документальный(5),
    Боевик(6);

    public int id;

    GenreDictionary(int id) {
        this.id = id;
    }

    public static String getGenre(int id) {
        GenreDictionary[] c;
        c = GenreDictionary.values();
        return String.valueOf(Arrays.stream(c).filter(x->x.id == id).findAny().orElseThrow());
    }

}

