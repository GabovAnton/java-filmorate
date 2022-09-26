package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@Slf4j
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FilmorateValidationException extends RuntimeException {
    public FilmorateValidationException(String message) {
        super(message);
    }

    public FilmorateValidationException(String message, String data) {
        super(message);
        log.debug(message + "provided data: {}", data);
    }
}