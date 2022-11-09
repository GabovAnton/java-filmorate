package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getAll();

    Optional<User> create(@Valid @RequestBody User user);

    Optional<User> update(@Valid @RequestBody User user);

    Optional<User> getById(@RequestBody long userId);

    Optional<User> getByEmail(@RequestBody String email);

    boolean delete(long userId);

    List<User> getUserFriends(long id);

     boolean addFriend(long friendIdOne, long friendIdTwo);

    List<User> getMutualFriends(long userOneId, long userTwoId);

    boolean removeFriend(long friendIdOne, long friendIdTwo);
}
