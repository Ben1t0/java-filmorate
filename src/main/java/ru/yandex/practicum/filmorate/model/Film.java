package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class Film {
    @EqualsAndHashCode.Include
    private int id;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private LocalDate releaseDate;
    private int duration;

}
