package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(String s) {
        super(s);
        log.debug(s);
    }
}
