package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int globalId = 1;

    private Integer getNextID() {
        return globalId++;
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        if (film != null) {
            if (films.values().stream().anyMatch(f -> f.getName().equals(film.getName()) &&
                    f.getReleaseDate().isEqual(film.getReleaseDate()))) {
                throw new AlreadyExistsException(
                        String.format("Film with name '%s' and release date '%s' already exist!",
                                film.getName(), film.getReleaseDate().toString()));
            }
            film.setId(getNextID());
            films.put(film.getId(), film);
            return film;
        }
        return null;
    }

    @Override
    public void update(Film film) {
        if (film != null && film.getId() != null) {
            if(!films.containsKey(film.getId())){
                throw new FilmNotFoundException(String.format("Фильм с таким ID = %d не найден",film.getId()));
            }
            if (films.values().stream()
                    .filter(f -> !f.getId().equals(film.getId()))
                    .anyMatch(f -> f.getName().equals(film.getName()) &&
                            f.getReleaseDate().isEqual(film.getReleaseDate()))) {
                throw new AlreadyExistsException(
                        String.format("Film with name '%s' and release date '%s' already exist!",
                                film.getName(), film.getReleaseDate().toString()));
            }
            films.put(film.getId(), film);
        }
    }

    @Override
    public void remove(Film film) {
        if (film != null && film.getId() != null) {
            films.remove(film.getId());
        }
    }

    @Override
    public Film findFilmById(int filmId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException(String.format("Film with ID = %d not found", filmId));
        }
        return films.get(filmId);
    }

    private void throwExceptionIfExist(Film film) {

    }
}
