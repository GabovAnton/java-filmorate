package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(String s) {
        super(s);
    }
}
