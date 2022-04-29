package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer global_id = 0;

    private Integer getNextID() {
        return global_id++;
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public void create(Film film) {
        if (film != null) {
            if (film.getId() != null && films.containsKey(film.getId())) {
                throw new AlreadyExistsException(String.format("Film with ID = %d already exist!", film.getId()));
            }
            film.setId(getNextID());
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
        }
    }

    @Override
    public Film findById(int filmId) {
        return films.get(filmId);
    }
}
