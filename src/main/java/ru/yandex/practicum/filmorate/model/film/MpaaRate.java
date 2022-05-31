package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MpaaRate {
    private Integer id;
    private String name;
    private String description;
}
