package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class User {


    private long id;

    private HashMap<Long, Byte> friends;

    public Set<Long> getFriends() {
        final Set<Long> friendsCollection = friends.entrySet()
                .stream()
                .filter(x->x.getValue().equals((byte)1))
                .map(y->y.getKey())
                .collect(Collectors.toSet());
        return friendsCollection;
    }

    public User() {
        friends = new HashMap<>();
    }

    @NotBlank
    @Email
    private String email;


    @NotBlank
    @Pattern(regexp = "^\\S*$", message = "must not contain whitespaces")
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

    public boolean addFriend(User user) {
        friends.putIfAbsent(user.id, (byte) 0);
        return true;
    }

    public boolean acceptFriend(long id) {
        friends.computeIfPresent(id, (k, v) -> v = (byte) 1);
        return true;
    }

    public boolean declineFriend(long id) {
        friends.computeIfPresent(id, (k, v) -> v = (byte) 0);
        return true;
    }
    public Set<Long> getNotApprovedFriends() {
        final Set<Long> friendsCollection = friends.entrySet()
                .stream()
                .filter(x->x.getValue().equals((byte)1))
                .map(y->y.getKey())
                .collect(Collectors.toSet());
        return friendsCollection;
    }

    public boolean removeFriend(long id) {
        friends.remove(id);
        return true;
    }

}
