package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    void create(Film film);

    void update(Film film);

    void remove(Film film);

    Film findFilmById(int filmId);
}
