MERGE INTO users (id, login, name, email, birth_date)
values (1, 'asd1', 'Test user', 'asd1@asd.com', '2000-12-10'),
       (2, 'asd2', 'Test user', 'asd2@asd.com', '2000-12-10'),
       (3, 'asd3', 'Test user', 'asd3@asd.com', '2000-12-10'),
       (4, 'asd4', 'Test user', 'asd4@asd.com', '2000-12-10');

MERGE INTO films (id, name, release_date, description, duration, mpaa_rate_id)
values (1, 'Test film1', '2000-12-10', 'film 1 for test', 100, 1),
       (2, 'Test film2', '2000-12-10', 'film 2 for test', 100, 2),
       (3, 'Test film3', '2000-12-10', 'film 3 for test', 100, 3),
       (4, 'Test film4', '2000-12-10', 'film 4 for test', 100, 3),
       (5, 'Test film5', '2000-12-10', 'film 5 for test', 100, 3);

MERGE INTO friends (user_id, friend_id)
VALUES (1, 2),
       (1, 4);