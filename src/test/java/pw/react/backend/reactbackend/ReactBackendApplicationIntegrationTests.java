package pw.react.backend.reactbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.constraints.br.TituloEleitoral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import pw.react.backend.reactbackend.models.User;

import java.time.LocalDate;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("it")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReactBackendApplicationIntegrationTests {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;



    private static User[] users = {
            new User("login123", "Pawel", "Kowalski", LocalDate.of(2015, 12, 31), true),
            new User("login124", "Gawel", "Nowak", LocalDate.of(2013, 12, 31), false),
            new User("login125", "Tomek", "Mrugala", LocalDate.of(2035, 12, 31), true),
            new User("login126", "Marek", "Zawadka", LocalDate.of(2045, 12, 31), true),
            new User("login127", "Mariusz", "Zyla", LocalDate.of(2017, 12, 31), false)
    };

    @Test
    public void givenUsers_whenGetIsRequested_thenReturnCorrectResponse() throws Exception
    {
        mockMvc.perform(get("/users")).andExpect(status().isOk());
    }

    @Test
    public void givenNothing_whenGetIsRequestedAndIdIsNotFound_thenReturnErrorStatus() throws Exception {
        this.mockMvc.perform(get("/users/255")).andExpect(status().isNotFound());
    }

    @Test
    public void givenUser_whenPostIsRequested_thenReturnCorrectResponse() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(users[0]))).andExpect(status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void givenNothing_whenGetIsRequestedAndIdIsFound_thenReturnCorrectStatus() throws Exception {
        this.mockMvc.perform(get("/users/1")).andExpect(status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void givenNothing_whenDeleteIsRequested_thenReturnCorrectStatus() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(users[4]))).andExpect(status().is2xxSuccessful()).andReturn();
        String content = result.getResponse().getContentAsString();
        User user = objectMapper.readValue(content, User.class);
        this.mockMvc.perform(delete("/users/" + user.getId()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(users[4].getLastName())));
    }

    @Test
    public void givenUser_whenPostIsRequestedAndUserExists_thenReturnErrorStatus() throws Exception {
        this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(users[1]))).andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(users[1]))).andExpect(status().is4xxClientError());
    }

    @Test
    public void givenLogin_whenGetIsRequestedAndUserExists_thenReturnCorrectStatus() throws Exception {
        this.mockMvc.perform(post("/users?login="+users[3].getLogin()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(users[3]))).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void givenLogin_whenGetIsRequestedAndUserDoesNotExist_thenReturnErrorStatus() throws Exception {
        this.mockMvc.perform(post("/users?login=NoLog1233131").contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
    }

    @Test
    public void givenUser_whenUpdateIsRequested_thenReturnCorrectStatus() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(users[4]))).andExpect(status().is2xxSuccessful()).andReturn();
        String content = result.getResponse().getContentAsString();
        User user = objectMapper.readValue(content, User.class);
        User userToUpdate=users[4]; userToUpdate.setLogin("UpdateLogin");
        this.mockMvc.perform(put("/users/" + user.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userToUpdate))).andExpect(status().is2xxSuccessful());
    }


}