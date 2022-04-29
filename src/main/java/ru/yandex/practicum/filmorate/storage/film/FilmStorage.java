package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

public interface FilmStorage {

    void create(Film film);

    void update(Film film);

    void remove(Film film);
}
