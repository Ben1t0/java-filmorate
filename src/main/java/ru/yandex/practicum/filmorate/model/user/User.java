package ru.yandex.practicum.filmorate.model.user;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @EqualsAndHashCode.Include
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public void setLogin(String login) {
        this.login = login.trim();
    }

    public UserDTO toDTO(){
        return UserDTO.builder()
                .id(id)
                .login(login)
                .email(email)
                .name(name)
                .birthday(birthday)
                .build();
    }

    public static User fromDTO(UserDTO userDTO){
        return User.builder()
                .id(userDTO.getId())
                .login(userDTO.getLogin())
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .birthday(userDTO.getBirthday())
                .build();
    }
}
