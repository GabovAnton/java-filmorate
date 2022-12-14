package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FilmorateValidationException extends RuntimeException {
    public FilmorateValidationException(String message) {
        super(message);
    }

    public FilmorateValidationException(String message, String data) {
        super(message + " " + data);
    }
}