package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {


    private long id;

    private Set<Long> friends;

    public Set<Long> getFriends() {
        final Set<Long> friendsCollection = friends;
        return friendsCollection;
    }

    public User() {
        friends =new HashSet<>();
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

    public void addFriend(User user) {

        friends.add(user.id);
    }

    public boolean removeFriend(long id) {
        friends.remove(id);
        return true;
    }

}
