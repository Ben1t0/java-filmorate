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
        validateAndAdd(film);
    }

    @PostMapping
    public void postFilm(@Valid @RequestBody Film film) {
        if (films.contains(film)) {
            log.warn("Film with ID = {} already exists", film.getId());
            throw new AlreadyExistsException("Film with ID = " + film.getId() + " already exists");
        }
        validateAndAdd(film);
    }

    private void validateAndAdd(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Film release date must be after 28-12-1985");
            throw new ValidationException("Film release date must be after 28-12-1985", "Release date");
        }

        films.add(film);
    }
}
