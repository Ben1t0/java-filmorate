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
INSERT INTO films ( name, release_date, description, duration, mpaa_rate_id)
    values ('Test film1', '2000-12-10', 'film 1 for test', 100, 1),
           ('Test film2', '2000-12-10', 'film 2 for test', 100, 2),
           ('Test film3', '2000-12-10', 'film 3 for test', 100, 3),
           ('Test film4', '2000-12-10', 'film 4 for test', 100, 3),
           ('Test film5', '2000-12-10', 'film 5 for test', 100, 3);*/
