package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.FilmDTO;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void init() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
    }

    @Test
    public void shouldntThrowExceptionWhenCorrectData() {
        FilmDTO filmDTO = FilmDTO.builder()
                .description("Desc")
                .id(12)
                .name("film")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();

        assertDoesNotThrow(() -> filmController.postFilm(filmDTO));
        assertEquals(1, filmController.getAll().size());
    }

    @Test
    public void shouldThrowExceptionWhenPostExistFilm() {
        FilmDTO filmDTO = FilmDTO.builder()
                .description("Desc")
                .name("film")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();

        FilmDTO filmDTO2 = FilmDTO.builder()
                .description("Desc2")
                .name("film")
                .releaseDate(LocalDate.now())
                .duration(102)
                .build();

        filmController.postFilm(filmDTO);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> filmController.postFilm(filmDTO2));

        assertEquals(String.format("Film with name 'film' and release date '%s' already exist!",
                filmDTO2.getReleaseDate().toString()), ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateWithIncorrectId() {
        FilmDTO filmDTO = FilmDTO.builder()
                .description("Desc")
                .name("film")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.putFilm(filmDTO));
        assertEquals("Film id should be present", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenToLongDescription() {
        FilmDTO filmDTO = FilmDTO.builder()
                .id(12)
                .description("yipoFNgAY9jTMIZMFfKuDUDx5R2niiw1FRAFtNKPegBkZa2tdV7vcKxQffYKirpV4fs0ClLSzhIxSa5AD4mUh" +
                        "XNIJKT3xtmZuMDCtGarMrhdjbd81ZMr2WMibfb8awPKtpb08dHo5NoWrwz9bq2k6lWjN7Jv2ryK5KSPRe5NKIq9lMo" +
                        "dl640fBRc4c0W9wF2t2G4SRdaI")
                .name("film")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.postFilm(filmDTO));
        assertEquals("Film description must be shorter than 200 symbols", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenBlankName() {
        FilmDTO filmDTO = FilmDTO.builder()
                .id(12)
                .description("Desc")
                .name("   ")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.postFilm(filmDTO));
        assertEquals("Film name can't be blank", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenReleaseDateBefore1895_12_28() {
        FilmDTO filmDTO = FilmDTO.builder()
                .id(12)
                .description("Desc")
                .name("To old film")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(100)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.postFilm(filmDTO));
        assertEquals("Film release date must be after 28-12-1895", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNegativeDuration() {
        FilmDTO filmDTO = FilmDTO.builder()
                .id(12)
                .description("Desc")
                .name("negative film")
                .releaseDate(LocalDate.now())
                .duration(-100)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.postFilm(filmDTO));
        assertEquals("Film duration must be greater than 0", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNoDuration() {
        FilmDTO filmDTO = FilmDTO.builder()
                .id(12)
                .description("Desc")
                .name("negative film")
                .releaseDate(LocalDate.now())
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.postFilm(filmDTO));
        assertEquals("Film duration should be present", ex.getMessage());
    }
}