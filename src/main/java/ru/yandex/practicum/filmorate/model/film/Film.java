package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.*;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Film {
    private final Set<Integer> userLikes = new HashSet<>();
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;

    public void addLike(int userId) {
        userLikes.add(userId);
    }

    public void removeLike(int userId) {
        userLikes.remove(userId);
    }

    public int getLikesCount(){
        return userLikes.size();
    }
}
