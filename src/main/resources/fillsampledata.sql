INSERT INTO PUBLIC."filmorate.users"
(NAME, LOGIN, EMAIL, BIRTHDAY)
VALUES ('Anton', 'agabov', 'agabov@gmail.com', '1984-01-21'),
       ('Dmitriy', 'dima', 'dima@gmail.com', '1985-05-15'),
       ('Ivan', 'ivan', 'ivan@gmail.com', '1986-12-02'),
       ('Konstantin', 'kostya', 'kostya@gmail.com', '1985-02-28'),
       ('Petr', 'petya', 'petya@gmail.com', '1990-08-07');

INSERT INTO PUBLIC."filmorate.friends"(USER_ID, FOREIGN_USER_ID, STATUS)
VALUES(SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'dima@gmail.com',
       SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'agabov@gmail.com',
       1);

INSERT INTO PUBLIC."filmorate.friends"(USER_ID, FOREIGN_USER_ID, STATUS)
VALUES(SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'dima@gmail.com',
       SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'kostya@gmail.com',
       1);

INSERT INTO PUBLIC."filmorate.friends"(USER_ID, FOREIGN_USER_ID, STATUS)
VALUES(SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'dima@gmail.com',
       SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'petya@gmail.com',
       0);

INSERT INTO PUBLIC."filmorate.friends"(USER_ID, FOREIGN_USER_ID, STATUS)
VALUES(SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'ivan@gmail.com',
       SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'dima@gmail.com',
       0);

INSERT INTO PUBLIC."filmorate.friends"(USER_ID, FOREIGN_USER_ID, STATUS)
VALUES(SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'kostya@gmail.com',
       SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'dima@gmail.com',
       1);

INSERT INTO PUBLIC."filmorate.friends"(USER_ID, FOREIGN_USER_ID, STATUS)
VALUES(SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'kostya@gmail.com',
          SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'agabov@gmail.com',
              1);


INSERT INTO PUBLIC."filmorate.film_genres" -- добавляем одновременно жанр фильма и сам фильм
(GENRE_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.genres"  WHERE NAME ='Драма',
       SELECT ID FROM FINAL TABLE(INSERT INTO PUBLIC."filmorate.films"
          (NAME, DESCRIPTION, DURATION, RELEASE_DATE,RATING_ID)
              VALUES('Титаник', 'Грустная история', 90, '2006-01-20',1)));


INSERT INTO PUBLIC."filmorate.film_genres" -- добавляем одновременно жанр фильма и сам фильм
(GENRE_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.genres"  WHERE NAME ='Мультфильм',
       SELECT ID FROM FINAL TABLE(INSERT INTO PUBLIC."filmorate.films"
          (NAME, DESCRIPTION, DURATION, RELEASE_DATE,RATING_ID)
              VALUES('Босс-молокосос', 'детский мультик про ребенка из корпорации детей', 100, '2008-02-23',2)));

INSERT INTO PUBLIC."filmorate.film_genres" -- добавляем одновременно жанр фильма и сам фильм
(GENRE_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.genres"  WHERE NAME ='Боевик',
       SELECT ID FROM FINAL TABLE(INSERT INTO PUBLIC."filmorate.films"
          (NAME, DESCRIPTION, DURATION, RELEASE_DATE,RATING_ID)
              VALUES('Агент 007', 'экшн про секретного агента', 120, '2020-05-17',2)));

INSERT INTO PUBLIC."filmorate.film_genres" -- добавляем одновременно жанр фильма и сам фильм
(GENRE_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.genres"  WHERE NAME ='Документальный',
       SELECT ID FROM FINAL TABLE(INSERT INTO PUBLIC."filmorate.films"
          (NAME, DESCRIPTION, DURATION, RELEASE_DATE,RATING_ID)
              VALUES('Дикая природа Амазонки', 'документальный фильм про природу', 120, '2025-02-25',3)));

BEGIN TRANSACTION; -- Добавляем фильм с двумя жанрами
INSERT INTO PUBLIC."filmorate.film_genres" -- добавляем одновременно жанр фильма и сам фильм
(GENRE_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.genres"  WHERE NAME ='Комедия',
       SELECT ID FROM FINAL TABLE(INSERT INTO PUBLIC."filmorate.films"
          (NAME, DESCRIPTION, DURATION, RELEASE_DATE,RATING_ID)
              VALUES('Я иду искать', 'фильм про невесту и ее будущих родтсвенников', 120, '2025-02-25',1)));

INSERT INTO PUBLIC."filmorate.film_genres" -- добавляем второй жанр фильма  для фильма "Я иду искать"
(GENRE_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.genres"  WHERE NAME ='Триллер',
       SELECT ID FROM PUBLIC."filmorate.films" WHERE NAME ='Я иду искать');
COMMIT;



INSERT INTO PUBLIC."filmorate.film_likes" -- добавляем лайк фильму титаник
(USER_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'kostya@gmail.com',
       SELECT ID FROM PUBLIC."filmorate.films"  WHERE NAME = 'Титаник');

INSERT INTO PUBLIC."filmorate.film_likes" -- добавляем лайк фильму Я иду искать
(USER_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'agabov@gmail.com',
       SELECT ID FROM PUBLIC."filmorate.films"  WHERE NAME = 'Я иду искать');

INSERT INTO PUBLIC."filmorate.film_likes" -- добавляем лайк фильму титаник
(USER_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'agabov@gmail.com',
       SELECT ID FROM PUBLIC."filmorate.films"  WHERE NAME = 'Титаник');

INSERT INTO PUBLIC."filmorate.film_likes" -- добавляем лайк фильму Дикая природа Амазонки
(USER_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.users"  WHERE EMAIL = 'petya@gmail.com',
          SELECT ID FROM PUBLIC."filmorate.films"  WHERE NAME = 'Дикая природа Амазонки');