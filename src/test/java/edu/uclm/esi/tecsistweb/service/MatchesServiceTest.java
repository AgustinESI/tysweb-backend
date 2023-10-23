package edu.uclm.esi.tecsistweb.service;


import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.repository.UserDAO;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MatchesServiceTest {

    @Autowired
    private MatchesService matchesService;

    @Autowired
    private UserDAO userDAO;

    private Match match = null;

    private static User user1;
    private static User user2;

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger("MatchesServiceTest");


    @BeforeEach
    @DisplayName("New Match - 4 In a Line - Returns Match")
    void newMatch() {


        String name = "matches1-service.tsyweb";
        String email = "matches1-service@alu.uclm.es";
        String pwd = "123456";

        User _user = new User();
        _user.setName(name);
        _user.setPwd(pwd);
        _user.setEmail(email);
        user1 = userDAO.save(_user);

        name = "matches2-service.tsyweb";
        email = "matches2-service@alu.uclm.es";
        pwd = "123456";

        _user = new User();
        _user.setName(name);
        _user.setPwd(pwd);
        _user.setEmail(email);
        user2 = userDAO.save(_user);


        int numBoards = 1;
        int col = 6;
        int row = 7;
        String id_user = user1.getId();


        match = this.matchesService.newMatch(id_user, numBoards, col, row);
        assertTrue(!match.getId_match().isBlank());
        assertTrue(match.getPlayers().contains(user1));

    }


    @Test
    @Order(1)
    @DisplayName("New match - User not found")
    void test1() {

        int numBoards = 1;
        int col = 6;
        int row = 7;
        String id_user = "1";

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.newMatch(id_user, numBoards, col, row);
        });

        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(2)
    @DisplayName("New match - Add second User")
    void test2() {

        int numBoards = 1;
        int col = 6;
        int row = 7;
        String id_user = user2.getId();


        Match match = this.matchesService.newMatch(id_user, numBoards, col, row);

        assertTrue(!match.getId_match().isBlank());
        assertTrue(match.getPlayers().contains(user2));
        assertTrue(match.getPlayers().contains(user1));
        assertTrue(match.getPlayers().size() == 2);
    }

    @Test
    @Order(3)
    @DisplayName("Add - Null ID Match - You have to add an ID of the game")
    void test3() {


        Match match = this.matchesService.newMatch(user2.getId(), 1, 6, 7);


        Map<String, Object> body = new HashMap<>();
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);
        body.put("color", "R");

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body, user1.getId());
        });

        String expectedMessage = "You have to add an ID of the game";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }


    @Test
    @Order(4)
    @DisplayName("Add - Empty ID Match - You have to add an ID of the game")
    void test4() {

        Match match = this.matchesService.newMatch(user2.getId(), 1, 6, 7);


        Map<String, Object> body = new HashMap<>();
        body.put("id_match", "");
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);
        body.put("color", "R");

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body, user1.getId());
        });

        String expectedMessage = "You have to add an ID of the game";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }


    @Test
    @Order(5)
    @DisplayName("Add - Null ID Board - You have to add an ID of the board")
    void test5() {

        Match match = this.matchesService.newMatch(user2.getId(), 1, 6, 7);


        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("col", 0);
        body.put("color", "R");

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body, user1.getId());
        });

        String expectedMessage = "You have to add an ID of the board";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }


    @Test
    @Order(6)
    @DisplayName("Add - Empty ID Board - You have to add an ID of the board")
    void test6() {

        Match match = this.matchesService.newMatch(user2.getId(), 1, 6, 7);

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("id_board", "");
        body.put("col", 0);
        body.put("color", "R");

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body, user1.getId());
        });

        String expectedMessage = "You have to add an ID of the board";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(7)
    @DisplayName("Add - Null Cols - You have to add a number for column")
    void test7() {

        Match match = this.matchesService.newMatch(user2.getId(), 1, 6, 7);

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("color", "R");

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body, user1.getId());
        });

        String expectedMessage = "You have to add a number for column";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(8)
    @DisplayName("Add - Null Color - You have to add a color")
    void test8() {

        Match match = this.matchesService.newMatch(user2.getId(), 1, 6, 7);

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body, user1.getId());
        });

        String expectedMessage = "You have to add a color";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }


    @Test
    @Order(9)
    @DisplayName("Add - Ilegal Movement - Ilegal movement, not your turn")
    void test9() {

        Match match = this.matchesService.newMatch(user2.getId(), 1, 6, 7);

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);
        body.put("color", "R");

        User user_fail;
        if (match.getCurrentUser().getId().equals(user1.getId())) {
            user_fail = user2;
        } else {
            user_fail = user1;
        }

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body, user_fail.getId());
        });

        String expectedMessage = "Ilegal movement, not your turn";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }


    @Test
    @Order(10)
    @DisplayName("Add - Match not found - You have to add an ID of the game")
    void test10() {


        Match match = this.matchesService.newMatch(user2.getId(), 1, 6, 7);


        Map<String, Object> body = new HashMap<>();
        body.put("id_match", "1");
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);
        body.put("color", "R");

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body, user1.getId());
        });

        String expectedMessage = "There is no match with id: 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(11)
    @DisplayName("Add - Board not found - There is no board with id: 1")
    void test11() {

        Match match = this.matchesService.newMatch(user2.getId(), 1, 6, 7);

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("id_board", "1");
        body.put("col", 0);
        body.put("color", "R");


        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body, match.getCurrentUser().getId());
        });

        String expectedMessage = "There is no board with id: 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }


    @Test
    @Order(12)
    @DisplayName("Add")
    void test12() {

        Match match = this.matchesService.newMatch(user2.getId(), 1, 6, 7);

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);
        body.put("color", "R");


        boolean out = this.matchesService.add(body, match.getCurrentUser().getId());
        assertFalse(out);
    }


    @Test
    @Order(13)
    @DisplayName("Add - Fill column - The column is alredy fill")
    void test13() {

        Match match = this.matchesService.newMatch(user2.getId(), 1, 6, 7);

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);
        body.put("color", "R");

        char[][] board = match.getBoardList().get(0).getBoard();

        board[0][0] = 'A';
        board[1][0] = 'R';
        board[2][0] = 'A';
        board[3][0] = 'R';
        board[4][0] = 'A';
        board[5][0] = 'R';

        match.getBoardList().get(0).setBoard(board);

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.add(body, match.getCurrentUser().getId());
        });

        String expectedMessage = "The column is alredy fill";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    @Order(14)
    @DisplayName("Add - End game")
    void test14() {

        Match match = this.matchesService.newMatch(user2.getId(), 1, 6, 7);

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("id_board", match.getBoardList().get(0).getId_board());
        body.put("col", 0);
        body.put("color", "R");

        char[][] board = match.getBoardList().get(0).getBoard();

        board[3][0] = 'R';
        board[4][0] = 'R';
        board[5][0] = 'R';

        match.getBoardList().get(0).setBoard(board);


        boolean out = this.matchesService.add(body, match.getCurrentUser().getId());
        assertTrue(out);
    }


    @Test
    @Order(15)
    @DisplayName("Get Match - ID Match null - Empty ID Match")
    void test15() {

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.getMatch(null);
        });

        String expectedMessage = "Empty ID Match";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @Order(16)
    @DisplayName("Get Match - ID Match empty - Empty ID Match")
    void test16() {

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.getMatch("");
        });

        String expectedMessage = "Empty ID Match";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @Order(17)
    @DisplayName("Get Match - Match not found - There is no match with id: ...")
    void test17() {

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            this.matchesService.getMatch("1");
        });

        String expectedMessage = "There is no match with id: 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(18)
    @DisplayName("Get Match")
    void test18() {


        Match match = this.matchesService.newMatch(user2.getId(), 1, 6, 7);

        Match _match = this.matchesService.getMatch(match.getId_match());

        assertTrue(!_match.getId_match().isBlank());
        assertTrue(_match.getPlayers().contains(user2));
        assertTrue(_match.getPlayers().contains(user1));
        assertTrue(_match.getPlayers().size() == 2);
    }


    @AfterEach
    @DisplayName("")
    void deleteUsers() {
        this.userDAO.deleteAll();
        this.matchesService.setMatchs(new HashMap<>());
        this.matchesService.setPending_matchs(new ArrayList<>());
    }

}
