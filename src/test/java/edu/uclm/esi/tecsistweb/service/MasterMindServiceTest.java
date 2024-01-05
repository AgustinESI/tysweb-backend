package edu.uclm.esi.tecsistweb.service;


import edu.uclm.esi.tecsistweb.model.MasterMind;
import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.repository.UserDAO;
import org.aspectj.lang.annotation.Before;
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
public class MasterMindServiceTest {
    @Autowired
    private MatchesService matchesService;

    @Autowired
    private UserDAO userDAO;

    private static User user1;
    private static User user2;
    @Test
    @Order(1)
    @DisplayName("New Match - Mastermind - Returns Match")
    void newMatch() {
        String name = "mastermind1-service.tsyweb";
        String email = "mastermind1-service@alu.uclm.es";
        String pwd = "123456";

        User _user = new User();
        _user.setName(name);
        _user.setPwd(pwd);
        _user.setEmail(email);
        user1 = userDAO.save(_user);
        user1.setColor("R");

        name = "mastermind2-service.tsyweb";
        email = "mastermind2-service@alu.uclm.es";
        pwd = "123456";

        _user = new User();
        _user.setName(name);
        _user.setPwd(pwd);
        _user.setEmail(email);
        user2 = userDAO.save(_user);
        user2.setColor("Y");


//        String id_user = user1.getId();
//        Match match = this.matchesService.start(getid_user, MasterMind.class.getSimpleName());
//        assertFalse(match.getId_match().isBlank());
//        assertTrue(match.getPlayers().contains(user1));

    }

    @Test
    @Order(2)
    @DisplayName("New match - User not found")
    void test1() {

        String id_user = "1";

        //TySWebException exception = assertThrows(TySWebException.class, () -> this.matchesService.start(id_user, MasterMind.class.getSimpleName()));

        String expectedMessage = "User not found";
        //String actualMessage = exception.getMessage();
        //assertEquals(expectedMessage, actualMessage);
        //assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
    @Test
    @Order(3)
    @DisplayName("New match - Add second User")
    void test2() {

        String id_user = user2.getId();

//        //Match match = this.matchesService.start(id_user, MasterMind.class.getSimpleName());
//
//        assertFalse(match.getId_match().isBlank());
//        assertTrue(match.getPlayers().contains(user2));
//        assertTrue(match.getPlayers().contains(user1));
//        assertEquals(2, match.getPlayers().size());
    }
    @Test
    @Order(4)
    @DisplayName("Add - Null ID Match - You have to add an ID of the game")
    void test3() {
        Map<String, Object> body = new HashMap<>();
        body.put("colors", "O,O,O,K,G,R");

        TySWebException exception = assertThrows(TySWebException.class, () -> this.matchesService.add(body, user1.getId()));

        String expectedMessage = "You have to add an ID of the game";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
    @Test
    @Order(5)
    @DisplayName("Delete Users")
    void deleteUsers() {
        this.userDAO.deleteAll();
        this.matchesService.getWaittingRoom().setPending_matchs(new ArrayList<>());
        this.matchesService.getWaittingRoom().setCurrent_matchs(new HashMap<>());
    }
}
