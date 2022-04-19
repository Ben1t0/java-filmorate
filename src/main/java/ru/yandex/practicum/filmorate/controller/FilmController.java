package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Set<Film> films = new HashSet<>();

    @GetMapping
    public Set<Film> getAll() {
        return films;
    }

    @PutMapping
    public void putFilm(@Valid @RequestBody Film film) {
        validate(film);
        films.add(film);
        log.info("Film updated: {}", film);
    }

    @PostMapping
    public void postFilm(@Valid @RequestBody Film film) {
        validate(film);
        if (films.contains(film)) {
            log.warn("Film with ID = {} already exists", film.getId());
            throw new AlreadyExistsException("Film with ID = " + film.getId() + " already exists");
        }

        films.add(film);
        log.info("Film created: {}", film);
    }

    private void validate(Film film) {
        if (film.getId() == null) {
            log.warn("Film id should be present");
            throw new ValidationException("Film id should be present", "id");
        }

        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Film name can't be blank");
            throw new ValidationException("Film name can't be blank", "name");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Film description must be shorter than 200 symbols");
            throw new ValidationException("Film description must be shorter than 200 symbols", "description");
        }
        if (film.getReleaseDate() != null
                && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Film release date must be after 28-12-1985");
            throw new ValidationException("Film release date must be after 28-12-1895", "releaseDate");
        }
        if (film.getDuration() == null) {
            log.warn("Film duration should be present");
            throw new ValidationException("Film duration should be present", "duration");
        }

        if (film.getDuration() <= 0) {
            log.warn("Film duration must be greater than 0");
            throw new ValidationException("Film duration must be greater than 0", "duration");
        }
    }
}
