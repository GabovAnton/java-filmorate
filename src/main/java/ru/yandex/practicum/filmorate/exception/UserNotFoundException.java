package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String s) {
        super(s);
    }

    public UserNotFoundException(String s, long userId) {
        super(s);
        log.debug("user id: {} not found", userId);
    }

    public UserNotFoundException(String s, String email) {
        super(s);
        log.debug("user email: {} not found", email);
    }

}
