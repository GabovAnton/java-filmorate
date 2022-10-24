package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

@Sql(scripts = {"classpath:/fillsampledata.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userStorage.getById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "agabov")
                );
    }

    @Test
    public void testCreateUser() {

        User userLocalOne =
                new User(1L, null, "userOne@mail.ru", "userOneLogin",
                        "User", LocalDate.of(1986, 10, 11));
        Optional<User> userOne = userStorage.create(userLocalOne);
        assertThat(userOne)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "userOneLogin")
                );

    }

    @Test
    public void testUpdateUser() {
        User agabov =
                new User(1L, null, "agabov@gmail.com", "newlogin",
                        "User", LocalDate.of(1986, 10, 11));
        Optional<User> updatedUser = userStorage.update(agabov);

        assertThat(updatedUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "newlogin")
                );

    }

    @Test
    public void testUserGetByEmail() {
        Optional<User> userOptional = userStorage.getByEmail("agabov@gmail.com");
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "agabov")
                );
    }

    @Test
    public void testGetUserFriends() {
        List<User> getUserFriends = userStorage.getUserFriends(2L);

        assertThat(getUserFriends)
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    public void testGetAllUsers() {
        List<User> allUsers = userStorage.getAll();

        assertThat(allUsers)
                .isNotEmpty()
                .hasSize(5);
    }

    @Test
    public void testAddFriend() {
        boolean addUserResult = userStorage.addFriend(1l, 4L);
        assertThat(addUserResult)
                .isTrue();

        List<User> getUserFriends = userStorage.getUserFriends(1L);

        assertThat(getUserFriends)
                .isNotEmpty()
                .hasSize(1);

    }

    @Test
    public void testGetMutualFriends() {
        List<User> friends = userStorage.getMutualFriends(2L, 4L);
        assertThat(friends)
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void testDeleteUserFriend() {
        boolean deleteUserResult = userStorage.removeFriend(2L, 1L);
        assertThat(deleteUserResult)
                .isTrue();
        List<User> getUserFriends = userStorage.getUserFriends(2L);

        assertThat(getUserFriends)
                .isNotEmpty()
                .hasSize(1)
                .extracting(User::getLogin)
                .doesNotContain("agabov");
    }

    @Test
    public void testGetAllFilms() {
        List<Film> allFilms = filmDbStorage.getAll();

        assertThat(allFilms)
                .isNotEmpty()
                .hasSize(5);

    }

    @Test
    public void testCreateFilm() {
        Film localFilm = new Film();
        localFilm.setName("NewFilm");
        localFilm.setDescription("newdescription");
        localFilm.setDuration(120);
        localFilm.setRate(5);
        localFilm.setReleaseDate(LocalDate.of(2000, 05, 20));
        Mpa mpa = new Mpa(1, MpaDictionary.getMPA(1));
        localFilm.setMpa(mpa);
        Optional<Film> newFilm = filmDbStorage.create(localFilm);

        assertThat(newFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "NewFilm")
                );
    }

    @Test
    public void testFilmUpdate() {
        Film localFilm = new Film();
        localFilm.setName("NewFilm");
        localFilm.setDescription("newdescription");
        localFilm.setDuration(120);
        localFilm.setRate(5);
        localFilm.setId(1);
        localFilm.setReleaseDate(LocalDate.of(2000, 05, 20));
        Mpa mpa = new Mpa(1, MpaDictionary.getMPA(1));
        localFilm.setMpa(mpa);
        Optional<Film> updatedFilm = filmDbStorage.update(localFilm);

        assertThat(updatedFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "NewFilm")
                );

    }

    @Test
    public void testGetFilmById() {
        Optional<Film> filmFromDB = filmDbStorage.getByID(1);
        assertThat(filmFromDB)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Титаник")
                );
    }

    @Test
    public void testAddLikeToFilm() {
        Optional<Film> filmDB = filmDbStorage.getByID(1);
        assertThat(filmDB)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Титаник")
                );

        boolean result = filmDbStorage.addLike(3L, filmDB.get());
        assertThat(result)
                .isTrue();

        int likesCount = filmDbStorage.getFilmLikes(1);
        assertThat(likesCount)
                .isEqualTo(3);

    }

    @Test
    public void removeLikeFromFilm() {
        boolean result = filmDbStorage.removeLike(4L, 1);
        assertThat(result)
                .isTrue();

        int likesCount = filmDbStorage.getFilmLikes(1);
        assertThat(likesCount)
                .isEqualTo(1);
    }

    @Test
    public void testGetTopFilms() {
        List<Film> TopFilms = filmDbStorage.getTopFilms(3);
        List<String> TopExpectedFilms = List.of("Титаник", "Я иду искать", "Дикая природа Амазонки");

        assertThat(TopFilms)
                .isNotEmpty()
                .hasSize(3)
                .extracting(Film::getName)
                .containsExactlyElementsOf(TopExpectedFilms);

    }

    @Test
    public void testDeleteFilm() {
        boolean result = filmDbStorage.deleteFilm(1);


        assertThat(result)
                .isTrue();
        ;

        Optional<Film> filmFromDB = filmDbStorage.getByID(1);
        assertThat(filmFromDB)
                .isEmpty();
    }

    @Test
    public void testGetGenreById() {

        String genre = filmDbStorage.getGenreById(1).name;

        assertThat(genre)
                .isEqualTo("Комедия");
    }

    @Test
    public void testGetAllGenres() {
        List<Genre> genres = filmDbStorage.getAllGenres();
        assertThat(genres)
                .isNotEmpty()
                .hasSize(6);
    }

    @Test
    public void testGetMPAById() {
        String mpa = filmDbStorage.getMPAById(1).name;
        assertThat(mpa)
                .isEqualTo("G");
    }

    @Test
    public void testGetAllMpa() {

        List<Mpa> allMpa = filmDbStorage.getAllMPA();
        assertThat(allMpa)
                .isNotEmpty()
                .hasSize(5);
    }


}