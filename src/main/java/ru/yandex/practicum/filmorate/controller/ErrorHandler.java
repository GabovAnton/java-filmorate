package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
@Slf4j
public class ErrorHandler {


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final FilmorateValidationException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserPostNotFoundException(final UserNotFoundException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmPostNotFoundException(final FilmNotFoundException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(
                "error", e.getMessage()
        );
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGeneralRuntimeException(final LikeNotFoundException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(
                "error", e.getMessage()
        );
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGeneralRuntimeException(final RuntimeException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(
                "error", e.getMessage()
        );
    }

}
