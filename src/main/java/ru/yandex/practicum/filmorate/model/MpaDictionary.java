package ru.yandex.practicum.filmorate.model;

import java.util.Arrays;

public enum MpaDictionary {
    G(1),
    PG(2),
    PG13(3),
    R(4),
    NC17(5);
    public int id;

    MpaDictionary(int id) {
        this.id = id;
    }

    public static String getMPA(int id) {
        MpaDictionary[] c;
        c = MpaDictionary.values();
        //TODO доделать!
        String mpaName = String.valueOf(Arrays.stream(c).filter(x->x.id == id).findAny().orElseThrow());
        switch (mpaName){
            case("PG13"):
                return "PG-13";
            case ("NC17"):
                return "NC-17";
            default: return mpaName;
        }
    }

}
