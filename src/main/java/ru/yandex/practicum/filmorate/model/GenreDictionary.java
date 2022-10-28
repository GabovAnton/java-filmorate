package ru.yandex.practicum.filmorate.model;

import java.util.Arrays;

public enum GenreDictionary {
    COMEDY(1),
    DRAMA(2),
    CARTOON(3),
    THRILLER(4),
    DOCUMENTARY(5),
    ACTION(6);

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

