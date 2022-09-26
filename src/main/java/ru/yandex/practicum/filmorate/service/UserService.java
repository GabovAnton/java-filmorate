package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public boolean removeFriend(long friendIdOne, long friendIdTwo) {
        return getUser(friendIdOne).removeFriend(friendIdTwo) && getUser(friendIdTwo).removeFriend(friendIdOne);
    }

    public boolean addFriend(long friendIdOne, long friendIdTwo) {
        return addMutualFriends(getUser(friendIdOne), getUser(friendIdTwo));
    }

    public User getUser(long friendIdOne) {
        Optional<User> userOne = Optional.ofNullable(userStorage.getById(friendIdOne));
        return userOne.orElseThrow(() ->
                new UserNotFoundException(userOne.isPresent() ? "user id: " + friendIdOne + " doesn't exists" : ""));
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public List<User> getMutualFriends(long userOneId, long userTwoId) {
        List<User> friends = new ArrayList<>();
        if (getUser(userOneId).getFriends() != null) {
            for (long friendId : getUser(userOneId).getFriends()) {
                friends.addAll(getUser(userTwoId).getFriends().stream()
                        .filter(x -> x.equals(friendId))
                        .map(this::getUser)
                        .collect(Collectors.toList()));
            }
        }
        return friends;
    }

    public List<User> getFriends(long id) {
        return getUser(id).getFriends().stream().map(this::getUser).collect(Collectors.toList());
    }

    private Boolean addMutualFriends(User one, User two) {
        one.addFriend(two);
        two.addFriend(one);
        return true;
    }

    public User update(User user) {
        return userStorage.update(user);
    }
}
