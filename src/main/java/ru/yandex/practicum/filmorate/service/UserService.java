package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDAODb") UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public boolean removeFriend(long friendIdOne, long friendIdTwo) {

     return userStorage.removeFriend(friendIdOne,friendIdTwo);
    }

    public boolean addFriend(long friendIdOne, long friendIdTwo) {
        return userStorage.addFriend(friendIdOne, friendIdTwo);
    }

    public Optional<User> getUser(long id) {
        return userStorage.getById(id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.create(user)
                .orElseThrow(() ->
                        new EntityNotFoundException("newly created user with id: " + user.getId() +
                                " doesn't exists in storage"));
    }

    public List<User> getMutualFriends(long userOneId, long userTwoId) {

        return userStorage.getMutualFriends(userOneId,userTwoId);
    }

    public List<User> getFriends(long id) {
     return  userStorage.getUserFriends(id);
    }



    public User update(User user) {
        return userStorage
                .update(user).orElseThrow(() -> new EntityNotFoundException("newly created user with id: " + user.getId() +
        " doesn't exists in storage"));
    }

    Optional<User> getByEmail(@RequestBody String email) {
        return userStorage.getByEmail(email);
    }
}
