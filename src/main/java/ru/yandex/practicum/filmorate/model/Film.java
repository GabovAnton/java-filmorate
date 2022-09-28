package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;

@Data
public class Film {
    private int id;

    private HashMap<Long, Integer> likes;

    public Film() {
        likes = new HashMap<>();
    }


    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @NonNull
    @Positive
    private int duration;

    @NotNull
    private LocalDate releaseDate;

    public boolean addLike(long userId) {
        likes.put(userId,1);
        return true;
    }
    public boolean removeLike(long userId) {
        if (likes.containsKey(userId)) {
            likes.remove(userId);
            return true;
        } else {
            throw new LikeNotFoundException("Like for user id: " + userId + " not found");
        }
    }

}
