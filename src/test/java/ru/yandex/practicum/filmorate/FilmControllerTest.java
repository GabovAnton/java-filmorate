package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void addFilm() throws Exception {
        Film film = new Film();
        film.setId(100);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(10);
        film.setDescription("интересный фильм не понятно о чем");
        film.setName("Матрица");

        mockMvc.perform(
                post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void addFilmWithReleaseDateBefore28_12_1895ShouldNotThrowValidationException() throws Exception {

        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(10);
        film.setDescription("интересный фильм не понятно о чем");
        film.setName("Матрица");

            mockMvc.perform(
                            post("/films")
                                    .content(objectMapper.writeValueAsString(film))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.name").value("Матрица"))
                    .andExpect(jsonPath("$.duration").value(10))
                    .andExpect(jsonPath("$.releaseDate").value("1895-12-28"));
    }

    @Test
    void addFilmWithLongDescriptionShouldThrowValidationException() throws Exception {

        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(10);
        film.setDescription("интересный фильм не понятно о чем.  Режисер явно не представлял что он снимает, " +
                "актеры играли не свои роли и складывалось ощущение, что они забывают текст. " +
                "Место съемок не соответствует атмосфере фильма");
        film.setName("Матрица");

            mockMvc.perform(
                            post("/films")
                                    .content(objectMapper.writeValueAsString(film))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

    }

    @Test
    void addFilmWithWrongDurationShouldThrowValidationException() throws Exception {

        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(-10);
        film.setDescription("интересный фильм не понятно о чем.");
        film.setName("Матрица");

        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

    }

    @Test
    void addFilmWithEmptyNameShouldThrowValidationException() throws Exception {

        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(10);
        film.setDescription("интересный фильм не понятно о чем.");

        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException));

    }

    @Test
    void updateFilmWithWrongIdShouldThrowValidationException() {
        Film film = new Film();
        film.setId(2200);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(10);
        film.setDescription("интересный фильм не понятно о чем.");
        film.setName("Матрица");

        try {

            mockMvc.perform(
                            put("/films")
                                    .content(objectMapper.writeValueAsString(film))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound());

        } catch (Exception e) {
            Exception exception =
                    assertThrows(
                            javax.validation.ValidationException.class,
                            () -> {
                                throw e.getCause();
                            });
            assertEquals(javax.validation.ValidationException.class,
                    Objects.requireNonNull(exception.getClass()));
        }

    }


    @Test
    void addFilmWithReleaseDateBefore27_12_1895ShouldThrowValidationException() throws Exception {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(10);
        film.setDescription("интересный фильм не понятно о чем");
        film.setName("Матрица");
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FilmorateValidationException))
                .andExpect(result -> assertEquals("Provided release date: '" + film.getReleaseDate()
                                + "' doesn't match criteria",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }


}
