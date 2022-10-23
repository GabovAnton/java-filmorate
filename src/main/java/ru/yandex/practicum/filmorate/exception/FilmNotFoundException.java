package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(String s) {
        super(s);
    }
}
