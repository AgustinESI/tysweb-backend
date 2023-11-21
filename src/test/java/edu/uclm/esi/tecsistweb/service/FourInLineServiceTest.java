package edu.uclm.esi.tecsistweb.service;


import edu.uclm.esi.tecsistweb.model.FourInLine;
import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.repository.UserDAO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FourInLineServiceTest {

    @Autowired
    private MatchesService matchesService;

    @Autowired
    private UserDAO userDAO;

    private static User user1;
    private static User user2;


    @BeforeEach
    @DisplayName("New Match - 4 In a Line - Returns Match")
    void start() {


        String name = "fourinline1-service.tsyweb";
        String email = "fourinline1-service@alu.uclm.es";
        String pwd = "123456";

        User _user = new User();
        _user.setName(name);
        _user.setPwd(pwd);
        _user.setEmail(email);
        user1 = userDAO.save(_user);
        user1.setColor("R");

        name = "fourinline2-service.tsyweb";
        email = "fourinline2-service@alu.uclm.es";
        pwd = "123456";

        _user = new User();
        _user.setName(name);
        _user.setPwd(pwd);
        _user.setEmail(email);
        user2 = userDAO.save(_user);
        user2.setColor("Y");

        String id_user = user1.getId();
        Match match = this.matchesService.start(id_user, FourInLine.class.getSimpleName());
        assertFalse(match.getId_match().isBlank());
        assertTrue(match.getPlayers().contains(user1));

    }


    @Test
    @Order(1)
    @DisplayName("New match - User not found")
    void test1() {

        String id_user = "1";

        TySWebException exception = assertThrows(TySWebException.class, () -> this.matchesService.start(id_user, FourInLine.class.getSimpleName()));

        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(2)
    @DisplayName("New match - Add second User")
    void test2() {

        String id_user = user2.getId();

        Match match = this.matchesService.start(id_user, FourInLine.class.getSimpleName());

        assertFalse(match.getId_match().isBlank());
        assertTrue(match.getPlayers().contains(user2));
        assertTrue(match.getPlayers().contains(user1));
        assertEquals(2, match.getPlayers().size());
    }

    @Test
    @Order(3)
    @DisplayName("Add - Null ID Match - You have to add an ID of the game")
    void test3() {

        Match match = this.matchesService.start(user2.getId(), FourInLine.class.getSimpleName());

        Map<String, Object> body = new HashMap<>();
        body.put("col", 0);

        TySWebException exception = assertThrows(TySWebException.class, () -> this.matchesService.add(body, user1.getId()));

        String expectedMessage = "You have to add an ID of the game";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }


    @Test
    @Order(4)
    @DisplayName("Add - Empty ID Match - You have to add an ID of the game")
    void test4() {

        Match match = this.matchesService.start(user2.getId(), FourInLine.class.getSimpleName());

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", "");
        body.put("col", 0);

        TySWebException exception = assertThrows(TySWebException.class, () -> this.matchesService.add(body, user1.getId()));

        String expectedMessage = "You have to add an ID of the game";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(7)
    @DisplayName("Add - Null Cols - You have to add a number for column")
    void test7() {

        Match match = this.matchesService.start(user2.getId(), FourInLine.class.getSimpleName());

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());

        TySWebException exception = assertThrows(TySWebException.class, () -> this.matchesService.add(body, match.getCurrentUser().getId()));

        String expectedMessage = "You have to add combination of the game";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(8)
    @DisplayName("Add - Null Color - You have to add a color")
    void test8() {

        Match match = this.matchesService.start(user2.getId(), FourInLine.class.getSimpleName());

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("combination", null);

        TySWebException exception = assertThrows(TySWebException.class, () -> this.matchesService.add(body, user1.getId()));

        String expectedMessage = "You have to add combination of the game";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }


    @Test
    @Order(9)
    @DisplayName("Add - Ilegal Movement - Ilegal movement, not your turn")
    void test9() {

        Match match = this.matchesService.start(user2.getId(), FourInLine.class.getSimpleName());

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("combination", 0);

        User user_fail;
        if (match.getCurrentUser().getId().equals(user1.getId())) {
            user_fail = user2;
        } else {
            user_fail = user1;
        }

        TySWebException exception = assertThrows(TySWebException.class, () -> this.matchesService.add(body, user_fail.getId()));

        String expectedMessage = "Ilegal movement, not your turn";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }


    @Test
    @Order(10)
    @DisplayName("Add - Match not found - You have to add an ID of the game")
    void test10() {


        Match match = this.matchesService.start(user2.getId(), FourInLine.class.getSimpleName());


        Map<String, Object> body = new HashMap<>();
        body.put("id_match", "1");
        body.put("combination", 0);

        TySWebException exception = assertThrows(TySWebException.class, () -> this.matchesService.add(body, match.getCurrentUser().getId()));

        String expectedMessage = "There is no match with id: 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(12)
    @DisplayName("Add")
    void test12() {

        Match match = this.matchesService.start(user2.getId(), FourInLine.class.getSimpleName());

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("combination", 0);


        this.matchesService.add(body, match.getCurrentUser().getId());
        assertNull(match.getWinner());
    }


    @Test
    @Order(13)
    @DisplayName("Add - Fill column - The column is alredy fill")
    void test13() {

        Match match = this.matchesService.start(user2.getId(), FourInLine.class.getSimpleName());

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("combination", 0);

        char[][] board = match.getBoardList().get(0).getBoard();

        board[0][0] = 'A';
        board[1][0] = 'R';
        board[2][0] = 'A';
        board[3][0] = 'R';
        board[4][0] = 'A';
        board[5][0] = 'R';

        match.getBoardList().get(0).setBoard(board);

        TySWebException exception = assertThrows(TySWebException.class, () -> this.matchesService.add(body, match.getCurrentUser().getId()));

        String expectedMessage = "The column is alredy fill";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    @Order(14)
    @DisplayName("Add - End game")
    void test14() {

        Match match = this.matchesService.start(user2.getId(), FourInLine.class.getSimpleName());

        Map<String, Object> body = new HashMap<>();
        body.put("id_match", match.getId_match());
        body.put("combination", 0);

        char[][] board = match.getBoardList().get(0).getBoard();

        char color = match.getCurrentUser().getColor().charAt(0);
        board[3][0] = color;
        board[4][0] = color;
        board[5][0] = color;

        match.getBoardList().get(0).setBoard(board);

        this.matchesService.add(body, match.getCurrentUser().getId());
        assertNotNull(match.getWinner());
    }


    @Test
    @Order(15)
    @DisplayName("Get Match - ID Match null - Empty ID Match")
    void test15() {

        TySWebException exception = assertThrows(TySWebException.class, () -> this.matchesService.getMatch(null));

        String expectedMessage = "Empty ID Match";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @Order(16)
    @DisplayName("Get Match - ID Match empty - Empty ID Match")
    void test16() {

        TySWebException exception = assertThrows(TySWebException.class, () -> this.matchesService.getMatch(""));

        String expectedMessage = "Empty ID Match";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @Order(17)
    @DisplayName("Get Match - Match not found - There is no match with id: ...")
    void test17() {

        TySWebException exception = assertThrows(TySWebException.class, () -> this.matchesService.getMatch("1"));

        String expectedMessage = "There is no match with id: 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(18)
    @DisplayName("Get Match")
    void test18() {

        Match match = this.matchesService.start(user2.getId(), FourInLine.class.getSimpleName());
        Match _match = this.matchesService.getMatch(match.getId_match());

        assertFalse(_match.getId_match().isBlank());
        assertTrue(_match.getPlayers().contains(user2));
        assertTrue(_match.getPlayers().contains(user1));
        assertEquals(2, _match.getPlayers().size());
    }

    @AfterEach
    @Order(19)
    @DisplayName("Delete Users")
    void deleteUsers() {
        this.userDAO.deleteAll();
        this.matchesService.getWaittingRoom().setPending_matchs(new ArrayList<>());
        this.matchesService.getWaittingRoom().setCurrent_matchs(new HashMap<>());
    }


}
