package edu.uclm.esi.tecsistweb.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MatchesControllerTest {


    @Autowired
    private MockMvc mockMvc;

    private static String id_board_line;
    private static String id_match_line;

    private static String id_board_battleship;
    private static String id_match_battleship;

    @BeforeAll
    public static void init() {
        id_board_line = null;
        id_match_line = null;
        id_board_battleship = null;
        id_match_battleship = null;
    }

    @Test
    @Order(1)
    public void testStart4InALine() throws Exception {
        // Perform a GET request to the endpoint
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/matches/4line/start")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id_match").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.boardList").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.boardList[0].id_board").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.boardList[0].board").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.boardList[0].board", Matchers.hasSize(6)))
                .andDo(result -> {
                    String jsonResponse = result.getResponse().getContentAsString();
                    id_match_line = new ObjectMapper().readTree(jsonResponse).get("id_match").asText();
                    id_board_line = new ObjectMapper().readTree(jsonResponse).path("boardList").get(0).path("id_board").asText();
                });

    }


    @Test
    @Order(2)
    public void testAdd4InALine() throws Exception {
        // Create a sample request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id_match", id_match_line);
        requestBody.put("id_board", id_board_line);
        requestBody.put("col", 0);
        requestBody.put("color", "R");

        String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/matches/4line/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id_match").value(id_match_line));
    }


    @Test
    @Order(3)
    public void testAdd4InALineEnd() throws Exception {

        int count = 0;
        for (int i = 0; i < 3; i++) {


            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("id_match", id_match_line);
            requestBody.put("id_board", id_board_line);
            requestBody.put("col", 1);
            requestBody.put("color", "A");

            String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/matches/4line/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson);

            mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id_match").value(id_match_line));
            count++;

            if (count == 3) {
                break;
            }

            requestBody = new HashMap<>();
            requestBody.put("id_match", id_match_line);
            requestBody.put("id_board", id_board_line);
            requestBody.put("col", 0);
            requestBody.put("color", "R");

            requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

            requestBuilder = MockMvcRequestBuilders
                    .post("/matches/4line/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBodyJson);

            mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id_match").value(id_match_line));


        }


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id_match", id_match_line);
        requestBody.put("id_board", id_board_line);
        requestBody.put("col", 0);
        requestBody.put("color", "R");


        String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/matches/4line/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("The end of the game"));
    }


    @Test
    @Order(4)
    public void testStartBattleship() throws Exception {
        // Perform a GET request to the endpoint
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/matches/battleship/start")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id_match").isNotEmpty()) // Assert that id_match is not empty
                .andExpect(MockMvcResultMatchers.jsonPath("$.boardList").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.boardList[1].id_board").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.boardList[1].board").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.boardList[1].board", Matchers.hasSize(10)))
                .andDo(result -> {
                    String jsonResponse = result.getResponse().getContentAsString();
                    id_match_battleship = new ObjectMapper().readTree(jsonResponse).get("id_match").asText();
                    id_board_battleship = new ObjectMapper().readTree(jsonResponse).path("boardList").get(0).path("id_board").asText();
                });
    }

    @Test
    @Order(5)
    public void testAdBattleship() throws Exception {

        Map<String, Object> body = new HashMap<>();

        body.put("id_match", id_match_battleship);
        body.put("id_board", id_board_battleship);

        List<String> aircraftCarrier = Arrays.asList("A0", "A1", "A2", "A3", "A4");
        body.put("aircraftCarrier", aircraftCarrier);

        List<String> armored = Arrays.asList("B0", "B1", "B2", "B3");
        body.put("armored", armored);

        List<String> cruiser = Arrays.asList("C0", "C1", "C2");
        body.put("cruiser", cruiser);

        List<String> destroyer1 = Arrays.asList("D0", "D1");
        body.put("destroyer1", destroyer1);

        List<String> destroyer2 = Arrays.asList("E0", "E1");
        body.put("destroyer2", destroyer2);

        List<String> submarine1 = Arrays.asList("F0");
        body.put("submarine1", submarine1);

        List<String> submarine2 = Arrays.asList("G0");
        body.put("submarine2", submarine2);

        String requestBodyJson = new ObjectMapper().writeValueAsString(body);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/matches/battleship/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));


    }


}
