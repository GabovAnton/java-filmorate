package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!Objects.equals(email, user.email)) return false;
        if (!Objects.equals(login, user.login)) return false;
        if (!Objects.equals(name, user.name)) return false;
        return Objects.equals(birthday, user.birthday);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        return result;
    }
}
