package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public Optional<User> getUserById(@PathVariable long id) {
        return Optional.ofNullable(userService.getUser(id));
    }

    @GetMapping()
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Optional<List<User>> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        return Optional.ofNullable(userService.getMutualFriends(id, otherId));
    }

    @GetMapping("{id}/friends")
    public Optional<List<User>> getFriends(@PathVariable long id) {
        return Optional.ofNullable(userService.getFriends(id));
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public boolean delete(@PathVariable long id, @PathVariable long friendId) {
        return userService.removeFriend(id, friendId);
    }

    @PutMapping("{id}/friends/{friendId}")
    public boolean addFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.addFriend(id, friendId);
    }


}
