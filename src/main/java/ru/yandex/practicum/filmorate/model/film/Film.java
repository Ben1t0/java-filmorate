package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Builder
@Data
public class Film {
    //@NotNull(message = "Film id should be present")
    private Integer id;

    @NotBlank(message = "Film name can't be blank")
    private String name;
    @Size(max = 200, message = "Film description must be shorter than 200 symbols")
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull(message = "Film duration should be present")
    @Positive(message = "Film duration must be greater than 0")
    private Integer duration;

}
