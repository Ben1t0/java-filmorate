package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.FilmDTO;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public Collection<FilmDTO> getAll() {
        return storage.getAll().stream().map(Film::toDTO).collect(Collectors.toList());
    }

    public FilmDTO add(FilmDTO filmDTO) {
        validate(filmDTO);

        storage.create(Film.fromDTO(filmDTO));
        return filmDTO;
    }

    public FilmDTO update(FilmDTO filmDTO){
        if (filmDTO.getId() == null) {
            log.warn("Film id wasn't present");
            throw new ValidationException("Film id should be present", "id");
        }
        validate(filmDTO);
        storage.update(Film.fromDTO(filmDTO));
        return filmDTO;
    }

    public FilmDTO remove(int filmID){
        if(filmID < 0){
            log.warn("Film ID below 0");
            throw new ValidationException("Film id should be positive", "id");
        }
        Film f = storage.findFilmById(filmID);
        storage.remove(f);
        return f.toDTO();
    }

    public FilmDTO findFilmById(int filmId){
        return storage.findFilmById(filmId).toDTO();
    }

    public Collection<String> getWhoLikesFilm(int filmID){
        return storage.findFilmById(filmID).getWhoLikes();
    }

    private void validate(FilmDTO filmDTO) {
        if (filmDTO.getId() != null && filmDTO.getId() < 0) {
            log.warn("Film ID below 0");
            throw new ValidationException("Film id should be positive", "id");
        }
        if (filmDTO.getName() == null || filmDTO.getName().isBlank()) {
            log.warn("Film name is blank");
            throw new ValidationException("Film name can't be blank", "name");
        }
        if (filmDTO.getDescription() != null && filmDTO.getDescription().length() > 200) {
            log.warn("Film description greater than 200 symbols");
            throw new ValidationException("Film description must be shorter than 200 symbols", "description");
        }
        if (filmDTO.getReleaseDate() != null
                && filmDTO.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Film release date before 28-12-1985");
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
