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

        boolean result = userStorage.removeFriend(friendIdOne, friendIdTwo);
        log.debug("friend id: {}",
                result ? "deleted successfully from user id: {}" : "can't be deleted from user id: {}",
                friendIdTwo, friendIdOne );
        return result;
    }

    public boolean addFriend(long friendIdOne, long friendIdTwo) {
        boolean result = userStorage.addFriend(friendIdOne, friendIdTwo);
        log.debug("friend id: {}",
                result ? "added successfully to user id: {}" : "can't be added to user id: {}",
                friendIdTwo, friendIdOne );
        return result;
    }

    public Optional<User> getUser(long id) {
        Optional<User> user = userStorage.getById(id);
        log.debug("requested user with id: {}", user.isPresent() ? "returned successfully" : "doesn't exist", id );

        return user;
    }

    public List<User> getAll() {
        List<User> allUsers = userStorage.getAll();
        log.debug("all films requested, returned: {}", allUsers.size());
        return allUsers;
    }

    public User create(User user) {
        User createdUser = userStorage.create(user)
                .orElseThrow(() ->
                        new EntityNotFoundException("newly created user with id: " + user.getId() +
                                " doesn't exists in storage"));
        log.debug("user: {} created successfully" , createdUser);

        return createdUser;

    }

    public List<User> getMutualFriends(long userOneId, long userTwoId) {
        List<User> mutualFriends = userStorage.getMutualFriends(userOneId, userTwoId);
        log.debug("mutual friends  requested, returned: {}", mutualFriends.size());

        return mutualFriends;
    }

    public List<User> getFriends(long id) {
        List<User> userFriends = userStorage.getUserFriends(id);
        log.debug("user friends requested for user id: {}, returned: {}", id, userFriends.size());

        return  userFriends;
    }



    public User update(User user) {
        User updatedUser = userStorage
                .update(user)
                .orElseThrow(() -> new EntityNotFoundException("newly created user with id: " + user.getId() +
                        " doesn't exists in storage"));
        log.debug("user: {} updated successfully" , updatedUser);

        return updatedUser;

    }

}
