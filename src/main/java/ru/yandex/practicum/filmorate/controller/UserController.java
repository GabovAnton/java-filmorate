package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.HashSet;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final HashSet<User> users = new HashSet<>();
    private static int id = 0;

    @GetMapping()
    public HashSet<User> findAll() {
        log.debug("current users amount: {}", users.size());
        return users;
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        users.stream().filter(x -> x.equals(user))
                .findAny().ifPresentOrElse(u -> {
                    throw new ValidationException("user already exist");
                }, () -> users.add(user));

        log.debug("user {} successfully added", user);

        return user;
    }


    @PutMapping()
    public User update(@Valid @RequestBody User user) {

        users.stream().filter(x -> x.getId() == user.getId())
                .findAny().ifPresentOrElse(u -> {
                    users.remove(u);
                    users.add(user);
                }, () -> {
                    throw new ValidationException("user doesn't exist");
                });

        log.debug("user {} successfully updated", user);

        return user;
    }

}
