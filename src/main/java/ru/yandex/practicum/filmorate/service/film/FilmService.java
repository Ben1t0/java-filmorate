package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.FilmDTO;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LocalDate MUST_BEFORE_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<FilmDTO> getAll() {
        return filmStorage.getAll().stream().map(FilmDTO::fromFilm).collect(Collectors.toList());
    }

    public FilmDTO create(FilmDTO filmDTO) {
        validate(filmDTO);
        return FilmDTO.fromFilm(filmStorage.create(filmDTO.asFilm()));
    }

    public FilmDTO update(FilmDTO filmDTO) {
        if (filmDTO.getId() == null) {
            log.warn("Film id wasn't present");
            throw new ValidationException("Film id should be present", "id");
        }
        validate(filmDTO);
        filmStorage.update(filmDTO.asFilm());
        return filmDTO;
    }

    public FilmDTO remove(int filmID) {
        if (filmID < 0) {
            log.warn("Film ID below 0");
            throw new ValidationException("Film id should be positive", "id");
        }
        Film film = filmStorage.getFilmById(filmID);
        filmStorage.remove(film);
        return FilmDTO.fromFilm(film);
    }

    public FilmDTO findFilmById(int filmId) {
        return FilmDTO.fromFilm(filmStorage.getFilmById(filmId));
    }

    public Collection<String> getWhoLikesFilm(int filmID) {
        return userStorage.getUsersByIds(filmStorage.getWhoLikedFilm(filmID)).stream()
                .map(User::getName).collect(Collectors.toList());
    }

    public Collection<String> addLike(int filmId, int userId) {
        userStorage.throwIfUserNotFound(userId);
        filmStorage.likeFilm(filmId, userId);
        return getWhoLikesFilm(filmId);
    }

    public Collection<String> removeLike(int filmId, int userId) {
        userStorage.throwIfUserNotFound(userId);
        filmStorage.dislikeFilm(filmId, userId);
        return getWhoLikesFilm(filmId);
    }

    public Collection<FilmDTO> getPopular(int count) {
        if (count < 0) {
            log.warn("count below 0");
            throw new ValidationException("Count should be positive", "count");
        }
        return filmStorage.getPopular(count).stream()
                .map(FilmDTO::fromFilm)
                .collect(Collectors.toList());
    }

    private void validate(FilmDTO filmDTO) {
        if (filmDTO.getName() == null || filmDTO.getName().isBlank()) {
            log.warn("Film name is blank");
            throw new ValidationException("Film name can't be blank", "name");
        }
        if (filmDTO.getDescription() != null && filmDTO.getDescription().length() > 200) {
            log.warn("Film description greater than 200 symbols");
            throw new ValidationException("Film description must be shorter than 200 symbols", "description");
        }
        if (filmDTO.getReleaseDate() != null
                && filmDTO.getReleaseDate().isBefore(MUST_BEFORE_DATE)) {
            log.warn("Film release date before 28-12-1895");
            throw new ValidationException("Film release date must be after 28-12-1895", "releaseDate");
        }
        if (filmDTO.getDuration() == null) {
            log.warn("Film duration wasn't present");
            throw new ValidationException("Film duration should be present", "duration");
        }
        if (filmDTO.getDuration() <= 0) {
            log.warn("Film duration is below 0");
            throw new ValidationException("Film duration must be greater than 0", "duration");
        }
    }


}
