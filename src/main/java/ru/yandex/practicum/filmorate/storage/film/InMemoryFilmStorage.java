package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Random ran = new Random();
    private final Set<Integer> usedIDs = new HashSet<>();

    private Integer getNextID() {
        Integer id = ran.nextInt(Integer.MAX_VALUE);
        while (usedIDs.contains(id)) {
            id = ran.nextInt(Integer.MAX_VALUE);
        }
        usedIDs.add(id);
        return id;
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public void create(Film film) {
        if (film != null) {
            if (film.getId() == null) {
                film.setId(getNextID());
            } else {
                if (films.containsKey(film.getId())) {
                    throw new AlreadyExistsException(String.format("Film with ID = %d already exist!", film.getId()));
                }
            }
            films.put(film.getId(), film);
        }
    }

    @Override
    public void update(Film film) {
        if (film != null && film.getId() != null) {
            films.put(film.getId(), film);
        }
    }

    @Override
    public void remove(Film film) {
        if (film != null && film.getId() != null) {
            films.remove(film.getId());
            usedIDs.remove(film.getId());
        }
    }

    @Override
    public Film findFilmById(int filmId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException(String.format("Film with ID = %d not found", filmId));
        }
        return films.get(filmId);
    }
}
