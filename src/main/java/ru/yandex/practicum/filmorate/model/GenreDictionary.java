package ru.yandex.practicum.filmorate.model;

import java.util.Arrays;

public enum GenreDictionary {
    Comedy(1),
    Drama(2),
    Cartoon(3),
    Thriller(4),
    Documentary(5),
    Action(6);

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

