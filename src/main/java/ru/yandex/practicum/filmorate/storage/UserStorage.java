package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getAll();

    User create(@Valid @RequestBody User user);

    User update(@Valid @RequestBody User user);

    Optional<User> getById(@RequestBody long userId);

    User getByEmail(@RequestBody String email);

    boolean delete(long userId);
}
