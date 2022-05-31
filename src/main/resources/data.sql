MERGE INTO mpaa_rates (id, name, description)
    VALUES (1, 'G', 'у фильма нет возрастных ограничений'),
           (2, 'PG', 'детям рекомендуется смотреть фильм с родителями'),
           (3, 'PG-13', 'детям до 13 лет просмотр не желателен'),
           (4, 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
           (5, 'NC-17', 'лицам до 18 лет просмотр запрещён');

MERGE INTO genres (id, name)
    VALUES (1, 'COMEDY'),
           (2, 'DRAMA'),
           (3, 'DOCUMENTARY'),
           (4, 'ACTION'),
           (5, 'THRILLER'),
           (6, 'HORROR'),
           (7, 'Sci-Fi'),
           (8, 'WESTERN'),
           (9, 'ROMANTIC');
/*
MERGE INTO users (id, login, name, email, birth_date)
    values (1, 'asd1', 'asddsa1', 'asd1@asd.com', '1999-07-14'),
           (2, 'asd2', 'asddsa2', 'asd2@asd.com', '1999-07-15'),
           (3, 'asd3', 'asddsa3', 'asd3@asd.com', '1999-07-12');

MERGE INTO films (id, name, release_date, description, duration, mpaa_rate_id)
    values (1, 'iron man 2', '1999-07-14', 'awesome movie', 100, 1),
           (2, 'iron man', '1999-07-14', 'awesome movie', 100, 2),
           (3, 'avengers', '1999-07-14', 'awesome movie', 100, 1);

MERGE INTO likes (user_id, film_id)
    VALUES (1, 1),
           (1, 2),
           (1, 3),
           (2, 1),
           (2, 2),
           (3, 1);
*/
