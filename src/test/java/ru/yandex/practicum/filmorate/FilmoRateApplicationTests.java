package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.MpaaRate;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    //region User DB Storage Test
    @Test
    public void shouldGetUserById() {
        Optional<User> userOptional = Optional.of(userStorage.getUserById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void throwIfUserNotFound() {
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userStorage.getUserById(100));
        assertEquals("User with ID = 100 not found", ex.getMessage());
    }

    @Test
    public void shouldUpdateUser() {
        Optional<User> userOptional = Optional.of(userStorage.getUserById(2));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "asd2")
                );

        userStorage.update(User.builder()
                .id(2)
                .name("Test user")
                .email("asd2@asd.com")
                .birthday(LocalDate.of(2000, 12, 10))
                .login("updated_login")
                .build());

        Optional<User> userOptionalUpdated = Optional.of(userStorage.getUserById(2));

        assertThat(userOptionalUpdated)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "updated_login")
                );
    }

    @Test
    public void shouldGetAllUsersFromDb() {
        Collection<User> users = userStorage.getAll();
        assertThat(users).anyMatch(user -> user.getEmail().equals("asd1@asd.com"));
        assertThat(users).anyMatch(user -> user.getEmail().equals("asd2@asd.com"));
    }

    @Test
    public void shouldRemoveUserFromDb() {
        User user = userStorage.getUserById(3);
        assertNotNull(user);

        userStorage.remove(User.builder().id(3).build());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userStorage.getUserById(3));
        assertEquals("User with ID = 3 not found", ex.getMessage());
    }

    @Test
    public void shouldAddFriend() {
        userStorage.addUserFriend(4, 1);
        assertThat(userStorage.getUserFriends(4)).anyMatch(user -> user.getId() == 1);
    }

    @Test
    public void shouldRemoveFriend() {
        assertThat(userStorage.getUserFriends(1)).anyMatch(user -> user.getId() == 2);
        userStorage.removeUserFriend(1, 2);
        assertThat(userStorage.getUserFriends(1)).noneMatch(user -> user.getId() == 2);
    }

    //endregion

    //region Film DB Storage Test
    @Test
    public void shouldGetFilmById() {
        Optional<Film> userOptional = Optional.of(filmStorage.getFilmById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void throwIfFilmNotFound() {
        FilmNotFoundException ex = assertThrows(FilmNotFoundException.class, () -> filmStorage.getFilmById(100));
        assertEquals("Film with ID = 100 not found", ex.getMessage());
    }

    @Test
    public void shouldUpdateFilm() {
        Optional<Film> filmOptional = Optional.of(filmStorage.getFilmById(2));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Test film2")
                );

        filmStorage.update(Film.builder()
                .id(2)
                .name("Updated Test film2")
                .releaseDate(LocalDate.of(2000, 12, 10))
                .description("film 2 for test")
                .mpa(MpaaRate.builder().id(2).build())
                .duration(100)
                .build());

        Optional<Film> filmOptionalUpdated = Optional.of(filmStorage.getFilmById(2));

        assertThat(filmOptionalUpdated)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Updated Test film2")
                );
    }

    @Test
    public void shouldGetAllFilmsFromDb() {
        Collection<Film> films = filmStorage.getAll();
        assertThat(films).anyMatch(film -> film.getName().equals("Test film1"));
        assertThat(films).anyMatch(film -> film.getName().equals("Test film3"));
    }

    @Test
    public void shouldRemoveFilmFromDb() {
        Film film = filmStorage.getFilmById(4);
        assertNotNull(film);

        filmStorage.remove(Film.builder().id(4).build());

        FilmNotFoundException ex = assertThrows(FilmNotFoundException.class, () -> filmStorage.getFilmById(4));
        assertEquals("Film with ID = 4 not found", ex.getMessage());
    }

    @Test
    public void shouldLikeAndDislikeFilm() {
        assertThat(filmStorage.getWhoLikedFilm(3)).hasSize(0);
        filmStorage.likeFilm(3, 1);
        filmStorage.likeFilm(3, 2);
        assertThat(filmStorage.getWhoLikedFilm(3)).hasSize(2);
        filmStorage.dislikeFilm(3, 1);
        assertThat(filmStorage.getWhoLikedFilm(3)).hasSize(1);
        filmStorage.dislikeFilm(3, 2);
    }

    @Test
    public void shouldReturnCorrectFilmRating() {
        filmStorage.likeFilm(1, 1);
        filmStorage.likeFilm(2, 1);
        filmStorage.likeFilm(2, 2);
        filmStorage.likeFilm(2, 4);
        filmStorage.likeFilm(3, 1);
        filmStorage.likeFilm(3, 2);
        assertThat(filmStorage.getPopular(3)).hasSize(3);
        assertThat(filmStorage.getPopular(10)).hasSizeGreaterThanOrEqualTo(4);

        assertThat(filmStorage.getPopular(3).stream().map(Film::getId))
                .containsExactly(2, 3, 1);
    }
    //endregion
}

