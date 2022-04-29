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
    private final Map<String, Integer> userLikes = new HashMap<>();
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;

    public void addLike(String userName) {
        userLikes.put(userName, 1);
    }

    public void removeLike(String userName){
        userLikes.remove(userName);
    }

    public Collection<String> getWhoLikes(){
        return userLikes.keySet();
    }

    public FilmDTO toDTO(){
        return FilmDTO.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .build();
    }

    public static Film fromDTO(FilmDTO filmDTO){
        return Film.builder()
                .id(filmDTO.getId())
                .name(filmDTO.getName())
                .description(filmDTO.getDescription())
                .duration(filmDTO.getDuration())
                .releaseDate(filmDTO.getReleaseDate())
                .build();
    }
}
