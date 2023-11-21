package edu.uclm.esi.tecsistweb.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uclm.esi.tecsistweb.repository.UserDAO;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
//@SpringBootTestsession
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {


    @Autowired
    private MockMvc server;

    @Autowired
    private UserDAO userDAO;

    private MockHttpSession session;


    @BeforeEach
    public void setUp() {
        session = new MockHttpSession();
    }


    @ParameterizedTest
    @CsvSource({
            "u, user-controller@alu.uclm.es, 123456, 123456",
            ", user-controller@alu.uclm.es, 123456, 123456",
            "user-controller.tsyweb, user-controller, 123456, 123456",
    })
    @DisplayName("Register - Name short/empty & wrong email format - TySWebException")
    @Order(1)
    void tes1(String name, String email, String pwd1, String pwd2) throws Exception {

        JSONObject jso = new JSONObject().
                put("name", name).
                put("pwd1", pwd1).
                put("pwd2", pwd2).
                put("email", email);

        RequestBuilder request = MockMvcRequestBuilders.
                post("/users/register").
                contentType(MediaType.APPLICATION_JSON).
                content(jso.toString());
        this.server.perform(request).
                andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource({
            "user-controller.tsyweb, user-controller@alu.uclm.es, 1, 1",
            "user-controller.tsyweb, user-controller@alu.uclm.es, 654321, 123456",
    })
    @DisplayName("Contraseñas mal o email inválido")
    @Order(2)
    void tes2(String name, String email, String pwd1, String pwd2) throws Exception {

        JSONObject jso = new JSONObject().
                put("name", name).
                put("pwd1", pwd1).
                put("pwd2", pwd2).
                put("email", email);

        RequestBuilder request = MockMvcRequestBuilders.
                post("/users/register").
                contentType(MediaType.APPLICATION_JSON).
                content(jso.toString());
        this.server.perform(request).
                andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @CsvSource({
            "user-controller.tsyweb, user-controller@alu.uclm.es, 123456, 123456",
    })
    @DisplayName("Contraseñas mal o email inválido")
    @Order(3)
    void tes3(String name, String email, String pwd1, String pwd2) throws Exception {

        JSONObject jso = new JSONObject().
                put("name", name).
                put("pwd1", pwd1).
                put("pwd2", pwd2).
                put("email", email);

        RequestBuilder request = MockMvcRequestBuilders.
                post("/users/register").
                contentType(MediaType.APPLICATION_JSON).
                content(jso.toString());
        this.server.perform(request).
                andExpect(status().isCreated());
    }

    @ParameterizedTest
    @CsvSource({
            "   , 123456",
            "user-controller@alu.uclm.es, ",
    })
    @DisplayName("Contraseñas mal o email inválido")
    @Order(4)
    void tes4(String email, String pwd) throws Exception {

        JSONObject jso = new JSONObject().
                put("pwd", pwd).
                put("email", email);

        RequestBuilder request = MockMvcRequestBuilders.
                put("/users/login").
                contentType(MediaType.APPLICATION_JSON).
                content(jso.toString());
        this.server.perform(request).
                andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource({
            "user-controller.tsyweb, user-controller@alu.uclm.es, 123456, 123456",
    })
    @DisplayName("Contraseñas mal o email inválido")
    @Order(5)
    void tes5(String name, String email, String pwd1, String pwd2) throws Exception {


        JSONObject jso = new JSONObject().
                put("name", name).
                put("pwd1", pwd1).
                put("pwd2", pwd2).
                put("email", email);

        RequestBuilder request = MockMvcRequestBuilders.
                post("/users/register").
                contentType(MediaType.APPLICATION_JSON).
                content(jso.toString());
        this.server.perform(request).
                andExpect(status().isCreated());

        jso = new JSONObject().
                put("pwd1", pwd1).
                put("email", email);

        request = MockMvcRequestBuilders.
                put("/users/login").
                contentType(MediaType.APPLICATION_JSON).
                content(jso.toString());
        this.server.perform(request).
                andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource({
            "user-controller.tsyweb, user-controller@alu.uclm.es, 123456, 123456",
    })
    @DisplayName("Contraseñas mal o email inválido")
    @Order(6)
    void tes6(String name, String email, String pwd1, String pwd2) throws Exception {


        JSONObject jso = new JSONObject().
                put("name", name).
                put("pwd1", pwd1).
                put("pwd2", pwd2).
                put("email", email);

        RequestBuilder request = MockMvcRequestBuilders.
                post("/users/register").
                contentType(MediaType.APPLICATION_JSON).
                content(jso.toString());
        this.server.perform(request).
                andExpect(status().isCreated());


        request = MockMvcRequestBuilders.
                get("/users/request-delete/" + email).
                contentType(MediaType.APPLICATION_JSON).
                param("pwd", pwd1);
        this.server.perform(request).
                andExpect(status().isOk());

        request = MockMvcRequestBuilders.
                delete("/users/delete").
                contentType(MediaType.APPLICATION_JSON).
                param("response", "false");
        this.server.perform(request).
                andExpect(status().isOk());

    }

    @ParameterizedTest
    @CsvSource({
            "user-controller.tsyweb, user-controller@alu.uclm.es, 123456, 123456",
    })
    @DisplayName("Contraseñas mal o email inválido")
    @Order(7)
    void tes7(String name, String email, String pwd1, String pwd2) throws Exception {


        JSONObject jso = new JSONObject().
                put("name", name).
                put("pwd1", pwd1).
                put("pwd2", pwd2).
                put("email", email);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jso.toString());

        MvcResult result = this.server.perform(request)
                .andExpect(status().isCreated()) // Assert the status code as needed
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonResponse);

        session.setAttribute("id_user", jsonNode.get("id").asText());


        request = MockMvcRequestBuilders.
                get("/users/request-delete/" + email).
                contentType(MediaType.APPLICATION_JSON).
                param("pwd", pwd1).session(session);
        this.server.perform(request).
                andExpect(status().isOk());


        request = MockMvcRequestBuilders.
                delete("/users/delete").
                contentType(MediaType.APPLICATION_JSON).
                param("response", "true").session(session);
        this.server.perform(request).
                andExpect(status().isAccepted());
    }


    @AfterEach
    @DisplayName("")
    void end() {
        this.userDAO.deleteAll();
    }


}
