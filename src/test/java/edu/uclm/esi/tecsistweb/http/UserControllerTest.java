package edu.uclm.esi.tecsistweb.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    private static String registeredUserId;

    @BeforeEach
    public void setUp() {
        session = new MockHttpSession();
    }


    @BeforeAll
    public static void init() {
        registeredUserId = null; // Initialize the static variable here
    }

    @Test
    @DisplayName("Register")
    @Order(1)
    public void register() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "user-controller.tsyweb");
        requestBody.put("email", "user-controller@alu.uclm.es");
        requestBody.put("pwd1", "123456");
        requestBody.put("pwd2", "123456");

        String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user-controller.tsyweb")) // Assert the name
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user-controller@alu.uclm.es")) // Assert the email
                .andExpect(MockMvcResultMatchers.jsonPath("$.pwd").doesNotExist()) // Assert that pwd1 is not in the response
                .andDo(result -> {
                    // Extract and store the user's ID from the response
                    String jsonResponse = result.getResponse().getContentAsString();
                    registeredUserId = new ObjectMapper().readTree(jsonResponse).get("id").asText();
                });
    }


    @Test
    @DisplayName("Register - Emtpy Email - Expect TySWebException - Email cannot be emtpy")
    @Order(2)
    public void loginEmptyEmail() throws Exception {
        // Create a request body with an empty email
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", ""); // Empty email
        requestBody.put("pwd", "123456");

        String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email cannot be empty"));
    }

    @Test
    @DisplayName("Register - Null Email - Expect TySWebException - Email cannot be emtpy")
    @Order(3)
    public void loginNullEmail() throws Exception {
        // Create a request body with an empty email
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("pwd", "123456");

        String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email cannot be empty"));
    }

    @Test
    @DisplayName("Register - Null Pwd - Expect TySWebException - Password cannot be emtpy")
    @Order(4)
    public void loginNullPwd() throws Exception {
        // Create a request body with an empty email
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "user-controller@alu.uclm.es");

        String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Password cannot be empty"));
    }

    @Test
    @DisplayName("Register - Empty Pwd - Expect TySWebException - Password cannot be emtpy")
    @Order(5)
    public void loginEmptyPwd() throws Exception {
        // Create a request body with an empty email
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "user-controller@alu.uclm.es");
        requestBody.put("pwd", "");

        String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Password cannot be empty"));
    }


    @Test
    @DisplayName("Login")
    @Order(6)
    public void login() throws Exception {
        // Create a request body with an empty email
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "user-controller@alu.uclm.es");
        requestBody.put("pwd", "123456");

        String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
    }


    @Test
    @DisplayName("Request Delete Account - Ara you sure you want to delete your account?")
    @Order(7)
    public void testRequestDeleteEndpoint() throws Exception {
        // Create a user and set it in the session

        // Perform a GET request to the endpoint with email and pwd parameters
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/request-delete/user-controller@alu.uclm.es")
                .param("pwd", "123456")
                .session(session);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Are you sure you want to delete your account?"));
    }


    @Test
    @DisplayName("Delete - False - User not deleted")
    @Order(8)
    public void testDeleteAccountWithResponseFalse() throws Exception {
        // Perform a DELETE request to the endpoint with response=false
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/users/delete")
                .param("response", "false")
                .session(session);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User not deleted"));
    }

    @Test
    @DisplayName("Delete - True - User deleted")
    @Order(9)
    public void testDeleteAccountWithResponseTrue() throws Exception {

        if (StringUtils.isBlank(registeredUserId)) {
            throw new RuntimeException("User ID is missing.");
        }

        session.setAttribute("user_id", registeredUserId);


        // Perform a DELETE request to the endpoint with response=true
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/users/delete")
                .param("response", "true")
                .session(session);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().string("User deleted"));
    }


}
