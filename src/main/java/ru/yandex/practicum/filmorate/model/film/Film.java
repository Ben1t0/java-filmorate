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
    private MpaaRate mpa;
    
    private List<String> genres;

    public void addLike(int userId) {
        userLikes.add(userId);
    }

    public void removeLike(int userId) {
        userLikes.remove(userId);
    }

    public int getLikesCount(){
        return userLikes.size();
    }

    public Map<String,Object> toMap(){
        Map<String,Object> values = new HashMap<>();
        values.put("name",name);
        values.put("description",description);
        values.put("release_date",releaseDate.toString());
        values.put("duration",duration);
        values.put("mpaa_rate_id",mpa.getId());
        return values;
    }


}
