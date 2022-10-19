--DROP ALL OBJECTS --delete all tables

CREATE TABLE IF NOT EXISTS PUBLIC."filmorate.films" (
                                                        ID INTEGER NOT NULL AUTO_INCREMENT,
                                                        NAME CHARACTER VARYING(100) NOT NULL,
                                                        DESCRIPTION CHARACTER VARYING(200),
                                                        DURATION INTEGER DEFAULT 0 NOT NULL,
                                                        RELEASE_DATE DATE NOT NULL,
    --RATING ENUM('G','PG','PG13','NC17') NOT NULL,
                                                        RATING_ID INTEGER NOT NULL,
                                                        CONSTRAINT FILMORATE_FILMS_PK PRIMARY KEY (ID),
                                                        CONSTRAINT filmorate_filmname_uniqe
                                                            UNIQUE (NAME)
);


CREATE TABLE IF NOT EXISTS PUBLIC."filmorate.users" (
                                                        ID INTEGER NOT NULL AUTO_INCREMENT,
                                                        NAME CHARACTER VARYING(100),
                                                        LOGIN CHARACTER VARYING(100) NOT NULL,
                                                        EMAIL VARCHAR_IGNORECASE(100) NOT NULL,
                                                        BIRTHDAY DATE NOT NULL,
                                                        CONSTRAINT FILMORATE_USERS_PK PRIMARY KEY (ID),
                                                        CONSTRAINT filmorate_users_uniqe
                                                            UNIQUE (LOGIN, EMAIL)
);

CREATE TABLE IF NOT EXISTS PUBLIC."filmorate.genres" (
                                                         ID INTEGER NOT NULL AUTO_INCREMENT,
                                                         NAME CHARACTER VARYING(100) NOT NULL,
                                                         CONSTRAINT FILMORATE_GENRES_PK PRIMARY KEY (ID),
                                                         CONSTRAINT filmorate_genre_uniqe
                                                             UNIQUE (NAME)
);

CREATE TABLE IF NOT EXISTS PUBLIC."filmorate.ratings" (
                                                          ID INTEGER NOT NULL AUTO_INCREMENT,
                                                          NAME CHARACTER VARYING(100) NOT NULL,
                                                          CONSTRAINT FILMORATE_RATINGS_PK PRIMARY KEY (ID),
                                                          CONSTRAINT filmorate_rating_uniqe
                                                              UNIQUE (NAME)
);


CREATE TABLE IF NOT EXISTS PUBLIC."filmorate.film_likes" (
                                                             USER_ID INTEGER NOT NULL,
                                                             FILM_ID INTEGER NOT NULL,
                                                             CONSTRAINT FILMORATE_FILMLIKES_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC."filmorate.films"(ID) ON DELETE CASCADE ON UPDATE CASCADE,
                                                             CONSTRAINT FILMORATE_FILMLIKES_FK_1 FOREIGN KEY (USER_ID) REFERENCES PUBLIC."filmorate.users"(ID) ON DELETE NO ACTION ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS PUBLIC."filmorate.film_genres" (
                                                              GENRE_ID INTEGER NOT NULL,
                                                              FILM_ID INTEGER NOT NULL,
                                                              CONSTRAINT FILMORATE_FILMGENRES_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC."filmorate.films"(ID) ON DELETE CASCADE ON UPDATE CASCADE,
                                                              CONSTRAINT FILMORATE_FILMGENRES_FK_1 FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC."filmorate.genres"(ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC."filmorate.friends" (
                                                          USER_ID INTEGER NOT NULL,
                                                          FOREIGN_USER_ID INTEGER NOT NULL,
                                                          STATUS TINYINT DEFAULT 0 NOT NULL,
                                                          CONSTRAINT FILMORATE_FRIENDS_FK FOREIGN KEY (FOREIGN_USER_ID) REFERENCES PUBLIC."filmorate.users"(ID) ON DELETE CASCADE ON UPDATE CASCADE,
                                                          CONSTRAINT FILMORATE_FRIENDS_FOREIGN_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC."filmorate.users"(ID) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO PUBLIC."filmorate.genres"
(NAME)
VALUES ('COMEDY'),
       ('DRAMA'),
       ('CARTOON'),
       ('THRILLER'),
       ('DOCUMENTARY'),
       ('ACTION');


INSERT INTO PUBLIC."filmorate.ratings"
(NAME)
VALUES ( 'G'),('PG'),('PG13'),('NC17');

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




INSERT INTO PUBLIC."filmorate.film_genres" -- добавляем одновременно жанр фильма и сам фильм
(GENRE_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.genres"  WHERE NAME ='DRAMA',
       SELECT ID FROM FINAL TABLE(INSERT INTO PUBLIC."filmorate.films"
          (NAME, DESCRIPTION, DURATION, RELEASE_DATE,RATING_ID)
              VALUES('Титаник', 'Грустная история', 90, '2006-01-20',1)));


INSERT INTO PUBLIC."filmorate.film_genres" -- добавляем одновременно жанр фильма и сам фильм
(GENRE_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.genres"  WHERE NAME ='CARTOON',
       SELECT ID FROM FINAL TABLE(INSERT INTO PUBLIC."filmorate.films"
          (NAME, DESCRIPTION, DURATION, RELEASE_DATE,RATING_ID)
              VALUES('Босс-молокосос', 'детский мультик про ребенка из корпорации детей', 100, '2008-02-23',2)));

INSERT INTO PUBLIC."filmorate.film_genres" -- добавляем одновременно жанр фильма и сам фильм
(GENRE_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.genres"  WHERE NAME ='ACTION',
       SELECT ID FROM FINAL TABLE(INSERT INTO PUBLIC."filmorate.films"
          (NAME, DESCRIPTION, DURATION, RELEASE_DATE,RATING_ID)
              VALUES('Агент 007', 'экшн про секретного агента', 120, '2020-05-17',2)));

INSERT INTO PUBLIC."filmorate.film_genres" -- добавляем одновременно жанр фильма и сам фильм
(GENRE_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.genres"  WHERE NAME ='DOCUMENTARY',
       SELECT ID FROM FINAL TABLE(INSERT INTO PUBLIC."filmorate.films"
          (NAME, DESCRIPTION, DURATION, RELEASE_DATE,RATING_ID)
              VALUES('Дикая природа Амазонки', 'документальный фильм про природу', 120, '2025-02-25',3)));

BEGIN TRANSACTION; -- Добавляем фильм с двумя жанрами
INSERT INTO PUBLIC."filmorate.film_genres" -- добавляем одновременно жанр фильма и сам фильм
(GENRE_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.genres"  WHERE NAME ='COMEDY',
       SELECT ID FROM FINAL TABLE(INSERT INTO PUBLIC."filmorate.films"
          (NAME, DESCRIPTION, DURATION, RELEASE_DATE,RATING_ID)
              VALUES('Я иду искать', 'фильм про невесту и ее будущих родтсвенников', 120, '2025-02-25',1)));

INSERT INTO PUBLIC."filmorate.film_genres" -- добавляем второй жанр фильма  для фильма "Я иду искать"
(GENRE_ID, FILM_ID)
VALUES(SELECT ID FROM PUBLIC."filmorate.genres"  WHERE NAME ='THRILLER',
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





