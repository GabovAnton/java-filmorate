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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void addUser() throws Exception {
        User user = new User();
        user.setId(100);
        user.setLogin("loginMe");
        user.setEmail("email@email.com");
        user.setBirthday(LocalDate.of(1984, 01, 21));
        user.setName("Иван");

        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void addUserWithEmptyEmailShouldThrowValidationException() throws Exception {

        User user = new User();
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.of(1984, 01, 21));
        user.setName("Иван");

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

    }

    @Test
    void addUserWithWrongEmailShouldThrowValidationException() throws Exception {

        User user = new User();
        user.setLogin("testLogin");
        user.setEmail("ysfgdf");
        user.setBirthday(LocalDate.of(1984, 01, 21));
        user.setName("Иван");

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

    }

    @Test
    void addUserWithEmptyLoginShouldThrowValidationException() throws Exception {

        User user = new User();
        user.setEmail("mail@ya.ru");
        user.setBirthday(LocalDate.of(1984, 01, 21));
        user.setName("Иван");

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

    }

    @Test
    void addUserWithWrongLoginShouldThrowValidationException() throws Exception {

        User user = new User();
        user.setEmail("mail@ya.ru");
        user.setLogin("my name");
        user.setBirthday(LocalDate.of(1984, 01, 21));
        user.setName("Иван");

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        Objects.requireNonNull(result.getResolvedException())
                                .getMessage().contains("must not contain whitespaces")));
    }

    @Test
    void addUserWithEmptyNameShouldUseLoginInstead() throws Exception {

        User user = new User();
        user.setEmail("mail@ya.ru");
        user.setLogin("myname");
        user.setBirthday(LocalDate.of(1984, 01, 21));

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.name").value("myname"));

    }

    @Test
    void addUserWithWrongBirthdayShouldThrowValidationException() throws Exception {

        User user = new User();
        user.setEmail("mail@ya.ru");
        user.setLogin("myname");
        user.setBirthday(LocalDate.of(2025, 01, 21));
        user.setName("Иван");

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));


    }

    @Test
    void updateUserWithWrongIdShouldThrowValidationException() {
        User user = new User();
        user.setId(500);
        user.setEmail("mail@ya.ru");
        user.setLogin("myname");
        user.setBirthday(LocalDate.of(2000, 01, 21));
        user.setName("Иван");

        try {

            mockMvc.perform(
                            put("/users")
                                    .content(objectMapper.writeValueAsString(user))
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


}
