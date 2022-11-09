DROP TABLE IF EXISTS PUBLIC."filmorate.film_likes" CASCADE;
DROP TABLE IF EXISTS PUBLIC."filmorate.film_genres" CASCADE;
DROP TABLE IF EXISTS PUBLIC."filmorate.friends" CASCADE;
DROP TABLE IF EXISTS PUBLIC."filmorate.films" CASCADE;
DROP TABLE IF EXISTS PUBLIC."filmorate.users" CASCADE;
DROP TABLE IF EXISTS PUBLIC."filmorate.genres" CASCADE;
DROP TABLE IF EXISTS PUBLIC."filmorate.ratings" CASCADE;

CREATE TABLE IF NOT EXISTS PUBLIC."filmorate.films" (
                                                        ID INTEGER NOT NULL AUTO_INCREMENT,
                                                        NAME CHARACTER VARYING(100) NOT NULL,
                                                        DESCRIPTION CHARACTER VARYING(200),
                                                        DURATION INTEGER DEFAULT 0 NOT NULL,
                                                        RELEASE_DATE DATE NOT NULL,
                                                        RATING_ID INTEGER NOT NULL,
                                                        CONSTRAINT FILMORATE_FILMS_PK PRIMARY KEY (ID),
                                                        CONSTRAINT filmorate_filmname_uniqe
                                                            UNIQUE (NAME)
);


CREATE TABLE IF NOT EXISTS PUBLIC."filmorate.users" (
                                                        ID INTEGER NOT NULL AUTO_INCREMENT,
                                                        NAME CHARACTER VARYING(100),
                                                        LOGIN CHARACTER VARYING(100) NOT NULL,
                                                        EMAIL CHARACTER VARYING(100) NOT NULL,
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
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');


INSERT INTO PUBLIC."filmorate.ratings"
(NAME)
VALUES ('G'),('PG'),('PG-13'), ('R'), ('NC-17');









