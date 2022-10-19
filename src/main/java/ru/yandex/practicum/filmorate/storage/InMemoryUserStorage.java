package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashSet<User> users = new HashSet<>();
    private static int id = 0;


    @Override
    public List<User> getAll() {
        log.debug("current users amount: {}", users.size());
        return new ArrayList<>(users);
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        users.stream().filter(x -> x.equals(user))
                .findAny().ifPresentOrElse(u -> {
                    throw new FilmorateValidationException("user already exists", user.getLogin());
                }, () -> users.add(user));

        log.debug("user {} successfully added", user);

        return user;
    }

    @Override
    public User update(User user) {
        users.stream().filter(x -> x.getId() == user.getId())
                .findAny().ifPresentOrElse(u -> {
                    users.remove(u);
                    users.add(user);
                }, () -> {
                    throw new UserNotFoundException("user doesn't exists", user.getId());
                });

        log.debug("user {} successfully updated", user);

        return user;
    }

    @Override
    public Optional<User> getById(long userId) {
        return users.stream()
                .filter(x -> x.getId() == userId)
                .findAny();
    }

    @Override
    public User getByEmail(String email) {
        return users.stream()
                .filter(x -> x.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("user doesn't exists", email));
    }

    @Override
    public boolean delete(long userId) {
        users.remove(getById(userId));
        return true;
    }
}
