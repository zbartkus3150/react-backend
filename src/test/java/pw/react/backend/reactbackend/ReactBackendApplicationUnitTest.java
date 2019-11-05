package pw.react.backend.reactbackend;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pw.react.backend.reactbackend.controllers.UserController;
import pw.react.backend.reactbackend.errors.ErrorResponse;
import pw.react.backend.reactbackend.errors.UserAlreadyExistsException;
import pw.react.backend.reactbackend.errors.UserNotFoundException;
import pw.react.backend.reactbackend.models.User;
import pw.react.backend.reactbackend.repositories.UserRepository;
import pw.react.backend.reactbackend.services.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@ActiveProfiles("dev")
@RunWith(MockitoJUnitRunner.class)
public class ReactBackendApplicationUnitTest {
    private static User[] users = {
            new User("login123", "Pawel", "Kowalski", LocalDate.of(2015, 12, 31), true),
            new User("login124", "Gawel", "Nowak", LocalDate.of(2013, 12, 31), false),
            new User("login125", "Tomek", "Mrugala", LocalDate.of(2035, 12, 31), true),
            new User("login126", "Marek", "Zawadka", LocalDate.of(2045, 12, 31), true),
            new User("login127", "Mariusz", "Zyla", LocalDate.of(2017, 12, 31), false)
    };
    private UserController userController;
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userController = new UserController(userService);
    }

    @Test
    public void givenExistingId_whenUpdateUser_theReturnUpdatedUser() {
        given(userRepository.findById(1)).willReturn(users[0]);
        User userToUpdate = users[0];
        userToUpdate.setLogin("TestLogin");

        User result = userService.update(1, userToUpdate);
        assertThat(userRepository.findById(1)).isEqualTo(userToUpdate);
        verify(userRepository, times(2)).findById(eq(1));
        verify(userRepository, times(1)).save(eq(userToUpdate));
    }

    @Test(expected = UserNotFoundException.class)
    public void givenNonExistingId_whenUpdateUser_theReturnUserNotFoundException() {
        when(userService.update(1, new User())).thenThrow(UserNotFoundException.class);
        verify(userRepository, times(1)).findById(eq(1));
        verify(userRepository, times(0)).save(new User());
    }

    @Test
    public void givenUserFromRepository_whenGetUserIsNull_thenReturnAllUser() {
        given(userRepository.findAll()).willReturn(Arrays.asList(users));
        // when
        ResponseEntity<List<User>> response = userController.getUsers(null);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).hasSize(users.length);
        then(response.getBody()).containsExactly(users);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void givenNewUserWithExistingLogin_whenCreateUserIsInvoked_thenThrowException() {
        given(userRepository.findByLogin(users[2].getLogin())).willReturn(Collections.singletonList(users[2]));
        when(userController.createUser(users[2])).thenThrow(UserAlreadyExistsException.class);
    }

    @Test
    public void givenNewUser_whenCreateUser_thenReturnSavedUser() {
        // given
        given(userRepository.findByLogin(users[2].getLogin())).willReturn(null);
        given(userRepository.save(users[2])).willReturn(users[2]);
        // when
        ResponseEntity<User> response = userController.createUser(users[2]);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualToComparingFieldByField(users[2]);
    }

    @Test(expected = UserNotFoundException.class)
    public void givenInvalidUserId_whenGetUserByIdIsInvoked_thenThrowException() {
        // given
        given(userRepository.findById(-1)).willReturn(null);

        when(userController.getUser(-1)).thenThrow(UserNotFoundException.class);
    }

    @Test
    public void givenNewUser_whenCreateUserIsInvoked_thenReturnSavedUser() {
        // given
        given(userRepository.findByLogin(users[4].getLogin())).willReturn(null);
        given(userRepository.save(users[4])).willReturn(users[4]);

        // when
        ResponseEntity<User> response = userController.createUser(users[4]);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualToComparingFieldByField(users[4]);
    }

    @Test
    public void givenUserNotFoundException_whenNotFoundIsInvoked_thenReturnErrorResponse() {
        // given
        UserNotFoundException ex = new UserNotFoundException();

        // when
        ResponseEntity<ErrorResponse> response = userController.notFound(ex);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ErrorResponse body = response.getBody();
        then(body.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void givenUserAlreadyExistsException_whenAlreadyExistsIsInvoked_thenReturnErrorResponse() {
        // given
        UserAlreadyExistsException ex = new UserAlreadyExistsException("msg");

        // when
        ResponseEntity<ErrorResponse> response = userController.alreadyExists(ex);

        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponse body = response.getBody();
        then(body.getMessage()).isEqualTo("msg");
        then(body.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void givenUserId_whenDeleteUserIsInvoked_thenReturnValidResponse() {
        // given
        given(userRepository.findById(users[1].getId())).willReturn(users[1]);

        // when
        ResponseEntity<User> response = userController.deleteUser(users[1].getId());

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualToComparingFieldByField(users[1]);
    }

}