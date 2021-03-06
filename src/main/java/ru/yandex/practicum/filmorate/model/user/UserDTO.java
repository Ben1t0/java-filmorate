package ru.yandex.practicum.filmorate.model.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class UserDTO {
    private Integer id;
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "User login can't be blank")
    private String login;
    private String name = "";
    @NotNull(message = "Birthday should have correct form")
    @Past(message = "User birthday must be in the past")
    private LocalDate birthday;

    public User asUser() {
        return User.builder()
                .id(id)
                .login(login)
                .email(email)
                .name(name)
                .birthday(birthday)
                .build();
    }

    public static UserDTO fromUser(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }
}
