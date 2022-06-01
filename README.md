# java-filmorate

### Модель базы данных для приложения

На схеме представлена модель базы данных для приложения Filmorate

![Data base scheme](./FilmorateDataBaseModel.png)

## Запросы в базу данных, соответствующие методам контроллеров

---

### users

<details>
  <summary>GET /users</summary>

```sql
    SELECT *
    FROM users;
```

</details>  
<details>
  <summary>GET /users/{id}</summary>

```sql
    SELECT *
    FROM users
    WHERE id = {id};
```

</details>  
<details>
  <summary>GET /users/{id}/friends</summary>

```sql
    SELECT *
    FROM users
    WHERE id IN (SELECT friend_id FROM friends WHERE user_id = {id}); 
```

</details>  
<details>
  <summary>GET /users/{id}/friends/common/{otherId}</summary>

```sql
    SELECT *
    FROM users
    WHERE id IN (SELECT friend_id FROM friends WHERE user_id = {id});
    INTERSECT
    SELECT *
    FROM users
    WHERE id IN (SELECT friend_id FROM friends WHERE user_id = {otherId});
```

</details>
<details>
  <summary>POST /users</summary>

```sql
    INSERT INTO users (email, login, name, birthdate)
    VALUES ('123@ya.ru', 'test_user', 'test user name', '2000-10-13')) RETURNING id;
```

</details>
<details>
  <summary>PUT /users</summary>

```sql
    UPDATE users
    SET email     = '123@ya.ru',
        login     = 'test_user',
        name      = 'test user name',
        birthdate = TO_DATE('01022019', 'MMDDYYYY')
    WHERE id = 'id';
```

</details>
<details>
  <summary>PUT /users/{id}/friends/{friendId}</summary>

```sql
    INSERT INTO friends (user_id, friend_id)
    VALUES ({id}, {friendId});
```

</details>
<details>
  <summary>DELETE /users/{id}/friends/{friendId}</summary>

```sql
    DELETE
    FROM friends
    WHERE user_id = {id} AND friend_id = {friendId};
```

</details>
<details>
  <summary>DELETE /users/{id}</summary>

```sql
    DELETE
    FROM users
    WHERE id = {id};
```

</details>

***  

### films

<details>
    <summary>GET /films</summary>

```sql
    SELECT f.id,
           f.name,
           f.release_date,
           f.description,
           f.duration,
           f.mpaa_rate_id,
           rate.name        AS mpaa_rate_name,
           rate.description AS mpaa_rate_description
    FROM films AS f
             JOIN mpaa_rates AS rate on f.mpaa_rate_id = rate.ID;
```

</details>
<details>
    <summary>GET /films/{id}</summary>

```sql
    SELECT f.id,
           f.name,
           f.release_date,
           f.description,
           f.duration,
           f.mpaa_rate_id,
           rate.name        AS mpaa_rate_name,
           rate.description AS mpaa_rate_description
    FROM films AS f
             JOIN mpaa_rates AS rate on f.mpaa_rate_id = rate.ID
    WHERE f.id = {id};
```

</details>
<details>
    <summary>GET /films/popular?count=10</summary>

```sql
    SELECT f.id,
           f.name,
           f.release_date,
           f.description,
           f.duration,
           f.mpaa_rate_id,
           rate.name        AS mpaa_rate_name,
           rate.description AS mpaa_rate_description
    FROM films AS f
             JOIN mpaa_rates AS rate on f.mpaa_rate_id = rate.id
             LEFT JOIN likes AS l ON f.id = l.film_id
    GROUP BY f.id
    ORDER BY COUNT(l.user_id) DESC LIMIT {count};
```

</details>
<details>
    <summary>POST /films</summary>

```sql
    INSERT INTO films (description, name, releaseDate, duration, mpaa_rate_id)
    VALUES ('desc', 'Awesome film', '2010-07-15', 120, 2) RETURNING id;
```

</details>
<details>
    <summary>PUT /films</summary>

```sql
    UPDATE films
    SET description  = 'desc',
        name         = 'Awesome film',
        releaseDate  = '2010-07-15',
        duration     = 120,
        mpaa_rate_id = 2
    WHERE id = 'id';
```

</details>
<details>
    <summary>PUT /films/{id}/like/{userId}</summary>

```sql
    INSERT INTO likes (film_id, user_id)
    VALUES ({id}, {userId});
```

</details>
<details>
    <summary>DELETE /films/{id}</summary>

```sql
    DELETE
    FROM films
    WHERE id = {id};
```

</details>
<details>
    <summary>DELETE /films/{id}/like/{userId}</summary>

```sql
    DELETE
    FROM likes
    WHERE film_id = {id} AND user_id = {userId};
```

</details>
<details>
    <summary>add genre</summary>

```sql
    INSERT INTO film_genre (film_id, genre_id)
    VALUES (5, 2);
```

</details>


***
