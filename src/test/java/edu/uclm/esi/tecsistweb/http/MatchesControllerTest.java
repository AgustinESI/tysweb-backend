package edu.uclm.esi.tecsistweb.http;

import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.repository.UserDAO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MatchesControllerTest {


    @Autowired
    private MockMvc server;

    @Autowired
    private UserDAO userDAO;

    private MockHttpSession session;


    @BeforeEach
    public void setUp() throws Exception {
        session = new MockHttpSession();
        String name = "matches-controller.tsyweb";
        String email = "matches-controller@alu.uclm.es";
        String pwd1 = "123456";
        String pwd2 = "123456";

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

        session = new MockHttpSession();

    }

    @Test
    @DisplayName("/matches/4line/start - User null - TySWebException")
    @Order(1)
    void tes1() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.
                get("/matches/4line/start").
                contentType(MediaType.APPLICATION_JSON)
                .session(session);
        this.server.perform(request).
                andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("/matches/4line/start - User not found - TySWebException")
    @Order(2)
    void tes2() throws Exception {
        session.setAttribute("id_user", "1");

        RequestBuilder request = MockMvcRequestBuilders.
                get("/matches/4line/start").
                contentType(MediaType.APPLICATION_JSON)
                .session(session);
        this.server.perform(request).
                andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @CsvSource({
            "matches-controller@alu.uclm.es, 123456",
    })
    @DisplayName("/matches/4line/start - User not found - TySWebException")
    @Order(3)
    void tes3(String email, String pwd) throws Exception {

        User u = userDAO.findByEmailAndPwd(email, DigestUtils.sha512Hex(pwd));

        session.setAttribute("id_user", u.getId());

        RequestBuilder request = MockMvcRequestBuilders.
                get("/matches/4line/start").
                contentType(MediaType.APPLICATION_JSON)
                .session(session);
        this.server.perform(request).
                andExpect(status().isOk());
    }

    @Test
    @DisplayName("/matches/4line/start - User id null - TySWebException")
    @Order(4)
    void tes4() throws Exception {


        JSONObject jso = new JSONObject().
                put("id_match", "").
                put("id_board", "").
                put("col", "").
                put("color", "");

        RequestBuilder request = MockMvcRequestBuilders.
                post("/matches/4line/add").
                contentType(MediaType.APPLICATION_JSON)
                .content(jso.toString())
                .session(session);
        this.server.perform(request).
                andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("/matches/4line/start - User not found - TySWebException")
    @Order(5)
    void tes5() throws Exception {


        JSONObject jso = new JSONObject().
                put("id_match", "").
                put("id_board", "").
                put("col", "").
                put("color", "");

        session.setAttribute("id_user", "1");

        RequestBuilder request = MockMvcRequestBuilders.
                post("/matches/4line/add").
                contentType(MediaType.APPLICATION_JSON)
                .session(session)
                .content(jso.toString());
        this.server.perform(request).
                andExpect(status().isNotFound());
    }


    @ParameterizedTest
    @CsvSource({
            ", 1, 0, R",
            "1, , 0, R",
            "1, 1, , R",
            "1, 1, 0, ",
    })
    @DisplayName("/matches/4line/start - User not found - TySWebException")
    @Order(6)
    void tes6(String id_match, String id_board, String col, String color) throws Exception {


        User u = userDAO.findByEmailAndPwd("matches-controller@alu.uclm.es", DigestUtils.sha512Hex("123456"));
        session.setAttribute("id_user", u.getId());

        JSONObject jso = new JSONObject().
                put("id_match", id_match).
                put("id_board", id_board);

        if (StringUtils.isNotBlank(col)) {
            jso.put("col", Integer.parseInt(col));
        }

        if (StringUtils.isNotBlank(color)) {
            jso.put("color", color.charAt(0));
        }

        RequestBuilder request = MockMvcRequestBuilders.
                post("/matches/4line/add").
                contentType(MediaType.APPLICATION_JSON)
                .content(jso.toString())
                .session(session);
        this.server.perform(request).
                andExpect(status().isNotFound());


    }

    @AfterEach
    @DisplayName("")
    void end() {
        this.userDAO.deleteAll();
    }


}
