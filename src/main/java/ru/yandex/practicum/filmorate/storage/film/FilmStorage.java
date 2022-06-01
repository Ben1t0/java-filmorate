package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    Film create(Film film);

    void update(Film film);

    void remove(Film film);

    Film getFilmById(int filmId);

    Collection<Integer> getWhoLikedFilm(int filmId);

    void likeFilm(int filmId, int userId);

    void dislikeFilm(int filmId, int userId);

    void throwIfFilmNotFound(int filmId);

    Collection<Film> getPopular(int count);
}
