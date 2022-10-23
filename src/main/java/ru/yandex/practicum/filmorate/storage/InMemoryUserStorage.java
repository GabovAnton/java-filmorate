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
import java.util.stream.Collectors;

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
    public Optional<User> create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        users.stream().filter(x -> x.equals(user))
                .findAny().ifPresentOrElse(u -> {
                    throw new FilmorateValidationException("user already exists", user.getLogin());
                }, () -> users.add(user));

        log.debug("user {} successfully added", user);

        return getById(user.getId());
    }

    @Override
    public Optional<User> update(User user) {
        users.stream().filter(x -> x.getId() == user.getId())
                .findAny().ifPresentOrElse(u -> {
                    users.remove(u);
                    users.add(user);
                }, () -> {
                    throw new UserNotFoundException("user doesn't exists", user.getId());
                });

        log.debug("user {} successfully updated", user);

        return getById(user.getId());
    }

    @Override
    public Optional<User> getById(long userId) {
        return users.stream()
                .filter(x -> x.getId() == userId)
                .findAny();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return users.stream()
                .filter(x -> x.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public boolean delete(long userId) {
        users.remove(getById(userId));
        return true;
    }

    @Override
    public List<User> getUserFriends(long id) {
        User user = getById(id).orElseThrow(() ->
                new UserNotFoundException( "user id: " + id + " doesn't exists"));

        return user.getFriends().stream().map(x->getById(x).orElseThrow(() ->
                new UserNotFoundException( "user id: " + x + " doesn't exists"))).collect(Collectors.toList());    }

    @Override
    public boolean addFriend(long friendIdOne, long friendIdTwo) {
        return addMutualFriends(getById(friendIdOne).orElseThrow(() ->
                        new UserNotFoundException( "user id: " + friendIdOne + " doesn't exists")),
                getById(friendIdTwo).orElseThrow(() ->
                        new UserNotFoundException( "user id: " + friendIdTwo + " doesn't exists")));
    }

    private Boolean addMutualFriends(User one, User two) {
        one.addFriend(two);
        two.addFriend(one);
        return true;
    }

    @Override
    public List<User> getMutualFriends(long userOneId, long userTwoId) {
        List<User> friends = new ArrayList<>();
        User userOne = getById(userOneId).orElseThrow(() ->
                new UserNotFoundException( "user id: " + userOneId+ " doesn't exists"));
        User userTwo = getById(userTwoId).orElseThrow(() ->
                new UserNotFoundException( "user id: " + userTwoId + " doesn't exists"));

        if (userOne.getFriends() != null) {
            for (long friendId : userOne.getFriends()) {
                friends.addAll(userTwo.getFriends().stream()
                        .filter(x -> x.equals(friendId))
                        .map(x->getById(x).orElseThrow(() ->
                                new UserNotFoundException( "user id: " + x + " doesn't exists")))
                        .collect(Collectors.toList()));
            }
        }
        return friends;
    }

    @Override
    public boolean removeFriend(long friendIdOne, long friendIdTwo) {

        User userOne = getById(friendIdOne).orElseThrow(() ->
                new UserNotFoundException( "user id: " + friendIdOne + " doesn't exists"));
        User userTwo = getById(friendIdTwo).orElseThrow(() ->
                new UserNotFoundException( "user id: " + friendIdTwo + " doesn't exists"));

        return userOne.removeFriend(friendIdTwo) && userTwo.removeFriend(friendIdOne);
    }
}
