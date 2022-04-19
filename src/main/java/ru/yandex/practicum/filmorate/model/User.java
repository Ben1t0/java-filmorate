package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @EqualsAndHashCode.Include
    @NotNull(message = "User id should be present")
    private Integer id;
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "User login can't be blank")
    private String login;
    private String name;
    @NotNull(message = "Birthday should have correct form")
    @Past(message = "User birthday must be in the past")
    private LocalDate birthday;

    public void setLogin(String login) {
        this.login = login.trim();
    }
}
