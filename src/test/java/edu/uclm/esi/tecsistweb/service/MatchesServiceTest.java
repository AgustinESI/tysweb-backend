package edu.uclm.esi.tecsistweb.service;


import edu.uclm.esi.tecsistweb.model.Battleship;
import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class MatchesServiceTest {

    @Autowired
    MatchesService matchesService;

    private Match match = null;

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger("MatchesServiceTest");


    @BeforeEach
    @DisplayName("New Match - 4 In a Line - Returns Match")
    void newMatch() {
        int numberBoards = 1;
        int numberCols = 10;
        int numberRows = 10;

        this.match = this.matchesService.newMatch(numberBoards, numberCols, numberRows);
        assertEquals(this.match.getBoardList().size(), numberBoards);
        assertEquals(this.match.getBoardList().get(0).getBoard().length, numberCols);
        assertEquals(this.match.getBoardList().get(0).getBoard()[0].length, numberRows);
    }


    @Test
    @DisplayName("Add - ID Match Null - Expect TySWebException - You have to add an ID of the game")
    void addIdMatchNull() {
        Map<String, Object> body = new HashMap<>();
        //body.put("id_match", "");
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);
        body.put("color", "R");


        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body);
        });

        String expectedMessage = "You have to add an ID of the game";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Add - ID Match Empty - Expect TySWebException - You have to add an ID of the game")
    void addIdMatchEmpty() {
        Map<String, Object> body = new HashMap<>();
        body.put("id_match", "");
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);
        body.put("color", "R");


        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body);
        });

        String expectedMessage = "You have to add an ID of the game";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }


    @Test
    @DisplayName("Add - ID Board Null - Expect TySWebException - You have to add an ID of the board")
    void addIdBoardNull() {
        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
//        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);
        body.put("color", "R");


        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body);
        });

        String expectedMessage = "You have to add an ID of the board";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Add - ID Board Empty - Expect TySWebException - You have to add an ID of the board")
    void addIdBoardEmpty() {
        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("id_board", "");
        body.put("col", 0);
        body.put("color", "R");


        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body);
        });

        String expectedMessage = "You have to add an ID of the board";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Add - ID Col Null - Expect TySWebException - You have to add a number for column")
    void addColNull() {
        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("id_board", match.getBoardList().get(0).getId_board());
//        body.put("col", 0);
        body.put("color", "R");


        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body);
        });

        String expectedMessage = "You have to add a number for column";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Add - Color Null - Expect TySWebException - You have to add a color")
    void addColorNull() {
        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);
//        body.put("color", "R");


        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body);
        });

        String expectedMessage = "You have to add a color";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }


    @Test
    @DisplayName("Add - Ilegal Movement - Expect TySWebException - Ilegal movement, you only can do 1 movement")
    void addIlegalMovement() {
        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);
        body.put("color", "R");

        this.matchesService.add(body);

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body);
        });

        String expectedMessage = "Ilegal movement, you only can do 1 movement";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }


    @Test
    @DisplayName("Add - Not valid ID Match - Expect TySWebException - There is no match with id: ...")
    void addNotValidIdMatch() {
        Map<String, Object> body = new HashMap<>();
        body.put("id_match", "1");
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);
        body.put("color", "R");

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body);
        });

        String expectedMessage = "There is no match with id: 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Add - Not valid ID Board - Expect TySWebException - There is no board with id: ...")
    void addNotValidIdBoard() {
        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("id_board", "1");
        body.put("col", 0);
        body.put("color", "R");

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body);
        });

        String expectedMessage = "There is no board with id: 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Get Match - Not valid ID Match - Expect TySWebException - There is no match with id: ...")
    void getMatchIdNotValid() {
        String id_match = "1";

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.getMatch(id_match);
        });

        String expectedMessage = "There is no match with id: 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Get Match - Empty ID Match - Expect TySWebException - Empty ID Match")
    void getMatchEmptyId() {
        String id_match = "";

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.getMatch(id_match);
        });

        String expectedMessage = "Empty ID Match";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }


    @Test
    @DisplayName("Add - Fill Column - Expect TySWebException - The column is alredy fill")
    void addFillColumn() {

        for (int i = 0; i < 5; i++) {
            Map<String, Object> body = new HashMap<>();
            body.put("id_match", match.getId_match());
            body.put("id_board", match.getBoardList().get(0).getId_board());
            body.put("col", 0);
            body.put("color", "R");
            this.matchesService.add(body);

            body = new HashMap<>();
            body.put("id_match", match.getId_match());
            body.put("id_board", match.getBoardList().get(0).getId_board());
            body.put("col", 0);
            body.put("color", "A");
            this.matchesService.add(body);
        }

        Map<String, Object> body_error = new HashMap<>();
        body_error.put("id_match", match.getId_match());
        body_error.put("id_board", match.getBoardList().get(0).getId_board());
        body_error.put("col", 0);
        body_error.put("color", "R");

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body_error);
        });

        String expectedMessage = "The column is alredy fill";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }


    public MatchesServiceTest() {
        super();
    }

    @Test
    @DisplayName("Add - Fill Column - Expect TySWebException - There is no match with id: ...")
    void addBattleShipNotValidIdMatch() {

        Battleship battleship = new Battleship();
        battleship.setAircraftCarrier(new ArrayList<>());
        battleship.setArmored(new ArrayList<>());
        battleship.setCruiser(new ArrayList<>());
        battleship.setDestroyer1(new ArrayList<>());
        battleship.setDestroyer2(new ArrayList<>());
        battleship.setSubmarine1(new ArrayList<>());
        battleship.setSubmarine2(new ArrayList<>());

        String idMatch = "1";
        String idBoard = "1";

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.addBattleShip(battleship, idMatch, idBoard);
        });

        String expectedMessage = "There is no match with id: 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Add - Fill Column - Expect TySWebException - There is no board with id: ...")
    void addBattleShipNotValidIdBoard() {

        Battleship battleship = new Battleship();
        battleship.setAircraftCarrier(new ArrayList<>());
        battleship.setArmored(new ArrayList<>());
        battleship.setCruiser(new ArrayList<>());
        battleship.setDestroyer1(new ArrayList<>());
        battleship.setDestroyer2(new ArrayList<>());
        battleship.setSubmarine1(new ArrayList<>());
        battleship.setSubmarine2(new ArrayList<>());

        String idBoard = "1";
        String idMatch = match.getId_match();

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.addBattleShip(battleship, idMatch, idBoard);
        });

        String expectedMessage = "There is no board with id: 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Add - BattleShip")
    void addBattleShip() {

        Battleship battleship = new Battleship();
        battleship.setAircraftCarrier(new ArrayList<>(Arrays.asList(new String[]{"A0", "A1", "A2", "A3", "A4"})));
        battleship.setArmored(new ArrayList<>(Arrays.asList(new String[]{"B0", "B1", "B2", "B3"})));
        battleship.setCruiser(new ArrayList<>(Arrays.asList(new String[]{"C0", "C1", "C2"})));
        battleship.setDestroyer1(new ArrayList<>(Arrays.asList(new String[]{"D0", "D1"})));
        battleship.setDestroyer2(new ArrayList<>(Arrays.asList(new String[]{"E0", "E1"})));
        battleship.setSubmarine1(new ArrayList<>(Arrays.asList(new String[]{"F0", "F1"})));
        battleship.setSubmarine2(new ArrayList<>(Arrays.asList(new String[]{"G0", "G1"})));

        String idBoard = match.getBoardList().get(0).getId_board();
        String idMatch = match.getId_match();

        this.matchesService.addBattleShip(battleship, idMatch, idBoard);


    }


}
