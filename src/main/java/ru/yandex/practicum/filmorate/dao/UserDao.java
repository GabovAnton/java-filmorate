package ru.yandex.practicum.filmorate.dao;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserDao  {
    Optional<User> getUser(long id);

    List<User> getAll();

    Optional<User> create(@Valid @RequestBody User user);

    Optional<User> update(@Valid @RequestBody User user);

    Optional<User> getByEmail(@RequestBody String email);
}
