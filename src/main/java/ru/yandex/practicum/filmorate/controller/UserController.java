package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
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
        Optional<User> user = userService.getUser(id);
        log.debug("user with id: {} requested, returned result: {}", id, user);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("user with id: " + id + " doesn't exists");
        }
        return user;
    }

    @GetMapping()
    public List<User> getAll() {
        List<User> all = userService.getAll();
        log.debug("all users requested: {}", all.size());
        return all;
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Optional<List<User>> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        Optional<List<User>> mutualFriends = Optional.ofNullable(userService.getMutualFriends(id, otherId));
        log.debug("mutual friends for users with id's: {},{} requested, returned result count: {}",
                id, otherId, mutualFriends);

        return mutualFriends;
    }

    @GetMapping("{id}/friends")
    public Optional<List<User>> getFriends(@PathVariable long id) {
        Optional<List<User>> friends = Optional.ofNullable(userService.getFriends(id));
        log.debug("requested friends for user id: {}, returned result: {}", id, friends);
        return friends;
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        User newUser = userService.create(user);
        log.debug("new user created: {}", newUser);
        return newUser;
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        User updatedUser = userService.update(user);
        log.debug("user updated: {} ->  {}", user, updatedUser);
        return updatedUser;
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public boolean delete(@PathVariable long id, @PathVariable long friendId) {
        boolean result = userService.removeFriend(id, friendId);
        log.debug("friend with id: {} has been deleted from friends of user with id: {} "
                + (result ? " successfully" : "with error"), friendId, id);
        return result;
    }

    @PutMapping("{id}/friends/{friendId}")
    public boolean addFriend(@PathVariable long id, @PathVariable long friendId) {
        boolean result = userService.addFriend(id, friendId);
        log.debug("user with id: {} added as mutual friend to user id: {}" + (result ? " successfully" : "with error"),
                id, friendId);

        return result;
    }


}
