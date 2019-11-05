package pw.react.backend.reactbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pw.react.backend.reactbackend.models.User;
import pw.react.backend.reactbackend.repositories.UserRepository;
import pw.react.backend.reactbackend.services.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserControllerTest {
    private UserController userController;

    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    //@Mock
    //UserService userService;
    /*
    private static User[] users = {
            new User("login123", "Pawel", "Kowalski", LocalDate.of(2015, 12, 31), true),
            new User("login124", "Gawel", "Nowak", LocalDate.of(2013, 12, 31), false),
            new User("login125", "Tomek", "Mrugala", LocalDate.of(2035, 12, 31), true),
            new User("login126", "Marek", "Zawadka", LocalDate.of(2045, 12, 31), true),
            new User("login127", "Mariusz", "Zyla", LocalDate.of(2017, 12, 31), false)
    };*/

    private static List<User> users = Arrays.asList(
            new User("login123", "Pawel", "Kowalski", LocalDate.of(2015, 12, 31), true),
            new User("login124", "Gawel", "Nowak", LocalDate.of(2013, 12, 31), false),
            new User("login125", "Tomek", "Mrugala", LocalDate.of(2035, 12, 31), true),
            new User("login126", "Marek", "Zawadka", LocalDate.of(2045, 12, 31), true),
            new User("login127", "Mariusz", "Zyla", LocalDate.of(2017, 12, 31), false)
    );

    @Before
    public void before() {
        userRepository = Mockito.mock(UserRepository.class);
    }

    @After
    public void after() {
        userRepository = null;
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void getUsers() throws Exception {
        this.mockMvc.perform(get("/user").content(objectMapper.writeValueAsString(users.get(0)))).andExpect(status().is2xxSuccessful());
    }

    @Test
    void getUser() {
    }

    @Test
    void createUser() throws Exception{
        mockMvc.perform(post("/user/")
                .contentType("application/json")
                .param("login", "test1")
                .content(objectMapper.writeValueAsString(users.get(0))))
                .andExpect(status().isOk());
    }

    @Test
    void givenUsers_whenPostIsRequested_thenReturnCorrectResponse() throws Exception {
        this.mockMvc.perform(post("/user/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(users.get(0)))).andExpect(status().is2xxSuccessful());
    }

    @Test
    void givenUser_whenRequestForUserIsIssued_thenReturnUser() throws Exception {
        this.mockMvc.perform(post("/user/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(users.get(1)))).andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(get("/user?login=" + users.get(1).getLogin())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(users.get(1).getLastName())));
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}