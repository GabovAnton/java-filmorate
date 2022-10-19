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

        User userOne = getUser(friendIdOne).orElseThrow(() ->
                new UserNotFoundException( "user id: " + friendIdOne + " doesn't exists"));
        User userTwo = getUser(friendIdTwo).orElseThrow(() ->
                new UserNotFoundException( "user id: " + friendIdTwo + " doesn't exists"));

        return userOne.removeFriend(friendIdTwo) && userTwo.removeFriend(friendIdOne);
    }

    public boolean addFriend(long friendIdOne, long friendIdTwo) {
        return addMutualFriends(getUser(friendIdOne).orElseThrow(() ->
                        new UserNotFoundException( "user id: " + friendIdOne + " doesn't exists")),
                getUser(friendIdTwo).orElseThrow(() ->
                        new UserNotFoundException( "user id: " + friendIdTwo + " doesn't exists")));
    }

    public Optional<User> getUser(long id) {
        return userStorage.getById(id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public List<User> getMutualFriends(long userOneId, long userTwoId) {
        List<User> friends = new ArrayList<>();
        User userOne = getUser(userOneId).orElseThrow(() ->
                new UserNotFoundException( "user id: " + userOneId+ " doesn't exists"));
        User userTwo = getUser(userTwoId).orElseThrow(() ->
                new UserNotFoundException( "user id: " + userTwoId + " doesn't exists"));

        if (userOne.getFriends() != null) {
            for (long friendId : userOne.getFriends()) {
                friends.addAll(userTwo.getFriends().stream()
                        .filter(x -> x.equals(friendId))
                        .map(x->getUser(x).orElseThrow(() ->
                                new UserNotFoundException( "user id: " + x + " doesn't exists")))
                        .collect(Collectors.toList()));
            }
        }
        return friends;
    }

    public List<User> getFriends(long id) {
        User user = getUser(id).orElseThrow(() ->
                new UserNotFoundException( "user id: " + id + " doesn't exists"));

        return user.getFriends().stream().map(x->getUser(x).orElseThrow(() ->
                new UserNotFoundException( "user id: " + x + " doesn't exists"))).collect(Collectors.toList());
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
