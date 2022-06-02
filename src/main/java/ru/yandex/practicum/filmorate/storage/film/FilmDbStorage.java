package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.MpaaRate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository("filmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getAll() {
        String sql = "SELECT f.id, " +
                "f.name, " +
                "f.release_date, " +
                "f.description, " +
                "f.duration, " +
                "f.mpaa_rate_id,rate.name AS mpaa_rate_name, " +
                "rate.description AS mpaa_rate_description " +
                "FROM films AS f " +
                "JOIN mpaa_rates AS rate on f.mpaa_rate_id = rate.id";
        return jdbcTemplate.query(sql, this::mapRowToFilm).stream()
                .map(this::getFilmGenres)
                .collect(Collectors.toList());
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        try {
            int id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
            film.setId(id);
            updateFilmGenres(film);
            getFilmGenres(film);
        } catch (DuplicateKeyException duplicateKeyException) {
            String errorMessage = duplicateKeyException.getCause().getMessage();
            errorMessage = errorMessage.substring(errorMessage.indexOf("Unique index"), errorMessage.indexOf("\";"));
            log.warn(errorMessage);
            throw new AlreadyExistsException("Movie with this name and release date already exists: " + errorMessage);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return film;
    }

    @Override
    public void update(Film film) {
        String updateQuery = "UPDATE films SET name = ?, release_date = ?, description = ?, " +
                "duration = ?, mpaa_rate_id = ? WHERE id = ?";
        int rowNum = 0;
        try {
            rowNum = jdbcTemplate.update(updateQuery, film.getName(), film.getReleaseDate().toString(),
                    film.getDescription(), film.getDuration(), film.getMpa().getId(), film.getId());
            updateFilmGenres(film);
        } catch (DuplicateKeyException duplicateKeyException) {
            String errorMessage = duplicateKeyException.getCause().getMessage();
            errorMessage = errorMessage.substring(errorMessage.indexOf("Unique index"), errorMessage.indexOf("\";"));
            log.warn(errorMessage);
            throw new AlreadyExistsException("Movie with this name and release date already exists: " + errorMessage);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        if (rowNum != 1) {
            throw new FilmNotFoundException(String.format("Film with id = %d not found.", film.getId()));
        }
    }

    @Override
    public void remove(Film film) {
        String deleteQuery = "DELETE FROM films where id = ? ";
        if (jdbcTemplate.update(deleteQuery, film.getId()) != 1) {
            throw new FilmNotFoundException(String.format("Film with ID = %d not found", film.getId()));
        }
    }

    @Override
    public Collection<Integer> getWhoLikedFilm(int filmId) {
        throwIfFilmNotFound(filmId);
        String likesQuery = "SELECT user_id FROM likes WHERE film_id = ?";
        return jdbcTemplate.queryForList(likesQuery, Integer.class, filmId);
    }

    @Override
    public Film getFilmById(int filmId) {
        String sql = "SELECT f.id, f.name, f.release_date, f.description, f.duration, f.mpaa_rate_id, " +
                "rate.name AS mpaa_rate_name, rate.description AS mpaa_rate_description" +
                " FROM films AS f" +
                " JOIN mpaa_rates AS rate on f.mpaa_rate_id = rate.ID" +
                " WHERE f.id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, this::mapRowToFilm, filmId);
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return getFilmGenres(film);
        } catch (EmptyResultDataAccessException ex) {
            log.warn(String.format("Film with ID = %d not found", filmId));
            throw new FilmNotFoundException(String.format("Film with ID = %d not found", filmId));
        }
    }

    @Override
    public void throwIfFilmNotFound(int filmId) {
        try {
            jdbcTemplate.queryForObject("SELECT id FROM films WHERE id = ?", Integer.class, filmId);
        } catch (EmptyResultDataAccessException ex) {
            log.warn(String.format("Film with ID = %d not found", filmId));
            throw new FilmNotFoundException(String.format("Film with ID = %d not found", filmId));
        }
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        throwIfFilmNotFound(filmId);
        String likeQuery = "INSERT INTO likes (user_id, film_id) VALUES ( ?,? )";
        jdbcTemplate.update(likeQuery, userId, filmId);
    }

    @Override
    public void dislikeFilm(int filmId, int userId) {
        String dislikeQuery = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(dislikeQuery, userId, filmId);
    }

    @Override
    public Collection<Film> getPopular(int count) {
        String getPopularQuery = "SELECT f.id, f.name, f.release_date, f.description, f.duration, f.mpaa_rate_id, " +
                "rate.name AS mpaa_rate_name, rate.description AS mpaa_rate_description" +
                " FROM films AS f" +
                " JOIN mpaa_rates AS rate on f.mpaa_rate_id = rate.id" +
                " LEFT JOIN likes AS l ON f.id = l.film_id" +
                " GROUP BY f.id" +
                " ORDER BY COUNT(l.user_id) DESC" +
                " LIMIT ?";
        return jdbcTemplate.query(getPopularQuery, this::mapRowToFilm, count).stream()
                .map(this::getFilmGenres)
                .collect(Collectors.toList());
    }

    private void updateFilmGenres(Film film) {
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            throwIfFilmNotFound(film.getId());
            String deleteQuery = "DELETE FROM film_genre where FILM_ID = ?";
            jdbcTemplate.update(deleteQuery, film.getId());

            List<Object[]> batchArgsList = new ArrayList<>();

            for (Genre genre : film.getGenres()) {
                Object[] objectArray = {film.getId(), genre.getId()};
                batchArgsList.add(objectArray);
            }
            String genreAddQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES ( ?,? ) ";
            jdbcTemplate.batchUpdate(genreAddQuery, batchArgsList);
        }
    }

    private Film getFilmGenres(Film film) {
        try {
            String getFilmGenresQuery = "SELECT g.id, g.name " +
                    "FROM film_genre AS fg " +
                    "JOIN genres AS g ON g.ID = fg.GENRE_ID " +
                    "WHERE fg.film_id = ?";
            film.setGenres(jdbcTemplate.query(getFilmGenresQuery, this::mapRowToGenre, film.getId()));
        } catch (Exception ignore) {
        }
        return film;
    }

    private Film mapRowToFilm(ResultSet rowSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(rowSet.getInt("id"))
                .name(rowSet.getString("name"))
                .description(rowSet.getString("description"))
                .releaseDate(rowSet.getDate("release_date").toLocalDate())
                .duration(rowSet.getInt("duration"))
                .mpa(MpaaRate.builder()
                        .id(rowSet.getInt("mpaa_rate_id"))
                        .name(rowSet.getString("mpaa_rate_name"))
                        .description(rowSet.getString("mpaa_rate_description"))
                        .build())
                .build();
    }

    private Genre mapRowToGenre(ResultSet rowSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rowSet.getInt("id"))
                .name(rowSet.getString("name"))
                .build();
    }
}
