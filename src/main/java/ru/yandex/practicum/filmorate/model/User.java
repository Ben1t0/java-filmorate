package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class User {
    @EqualsAndHashCode.Include
    private int id;
    @Email
    private String email;
    @NotBlank
    private String login;
    @NotBlank
    private String name;
    @NotNull
    private LocalDate birthdate;

    public boolean isValid() {
        if (login.isBlank() || !email.contains("@") || birthdate.isAfter(LocalDate.now())) {
            return false;
        } else {
            return true;
        }
    }
}
