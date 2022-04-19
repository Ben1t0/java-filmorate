package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void init() {
        filmController = new FilmController();
    }

    @Test
    public void shouldntThrowExceptionWhenCorrectData() {
        Film film = Film.builder()
                .description("Desc")
                .id(12)
                .name("film")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();

        assertDoesNotThrow(() -> filmController.postFilm(film));
        assertEquals(1, filmController.getAll().size());
    }

    @Test
    public void shouldThrowExceptionWhenPostExistFilm() {
        Film film = Film.builder()
                .description("Desc")
                .id(12)
                .name("film")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();

        filmController.postFilm(film);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> filmController.postFilm(film));

        assertEquals("Film with ID = 12 already exists", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenIncorrectId() {
        Film film = Film.builder()
                .description("Desc")
                .name("film")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        assertEquals("Film id should be present", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenToLongDescription() {
        Film film = Film.builder()
                .id(12)
                .description("yipoFNgAY9jTMIZMFfKuDUDx5R2niiw1FRAFtNKPegBkZa2tdV7vcKxQffYKirpV4fs0ClLSzhIxSa5AD4mUh" +
                        "XNIJKT3xtmZuMDCtGarMrhdjbd81ZMr2WMibfb8awPKtpb08dHo5NoWrwz9bq2k6lWjN7Jv2ryK5KSPRe5NKIq9lMo" +
                        "dl640fBRc4c0W9wF2t2G4SRdaI")
                .name("film")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        assertEquals("Film description must be shorter than 200 symbols", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenBlankName() {
        Film film = Film.builder()
                .id(12)
                .description("Desc")
                .name("   ")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        assertEquals("Film name can't be blank", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenReleaseDateBefore1895_12_28() {
        Film film = Film.builder()
                .id(12)
                .description("Desc")
                .name("To old film")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(100)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        assertEquals("Film release date must be after 28-12-1895", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNegativeDuration() {
        Film film = Film.builder()
                .id(12)
                .description("Desc")
                .name("negative film")
                .releaseDate(LocalDate.now())
                .duration(-100)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        assertEquals("Film duration must be greater than 0", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNoDuration() {
        Film film = Film.builder()
                .id(12)
                .description("Desc")
                .name("negative film")
                .releaseDate(LocalDate.now())
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        assertEquals("Film duration should be present", ex.getMessage());
    }
}