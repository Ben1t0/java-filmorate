package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.film.FilmDTO;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmDTO> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public FilmDTO getFilmById(@PathVariable("id") int filmId) {
        return filmService.findFilmById(filmId);
    }

    @PutMapping
    public void putFilm(@Valid @RequestBody FilmDTO filmDTO) {
        filmService.update(filmDTO);
        log.info("Film updated: {}", filmDTO);
    }

    @PostMapping
    public void postFilm(@Valid @RequestBody FilmDTO filmDTO) {
        filmService.add(filmDTO);
        log.info("Film created: {}", filmDTO);
    }

    @DeleteMapping("/{id}")
    public FilmDTO removeFilm(@PathVariable(value = "id") int filmId) {
        return filmService.remove(filmId);
    }

    //PUT /films/{id}/like/{userId}
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId,
                        @PathVariable("otherId") int userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") int filmId,
                           @PathVariable("otherId") int userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmDTO> getPopular(
            @RequestParam(value = "count", defaultValue = "10", required = false) int count) {
        return filmService.getPopular(count);
    }
}
