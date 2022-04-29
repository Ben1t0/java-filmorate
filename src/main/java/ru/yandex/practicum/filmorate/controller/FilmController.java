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
}
