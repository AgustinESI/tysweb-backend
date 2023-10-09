package edu.uclm.esi.tecsistweb.service;


import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    private String user_id = "";

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger("UserServiceTest");

    @BeforeEach
    @DisplayName("Register - Save User - Returns User")
    void saveUser() {
        String name = "user-service.tsyweb";
        String email = "user-service@alu.uclm.es";
        String pwd = "123456";
        String pwd2 = "123456";

        User user = new User();
        user.setName(name);
        user.setPwd(pwd);
        user.setEmail(email);
        User user_saved = userService.register(name, email, pwd, pwd2);
        user_id = user_saved.getId();
        assertEquals(user.getName(), user_saved.getName());
        assertEquals(user.getEmail(), user_saved.getEmail());
        assertEquals(user.getPwd(), user_saved.getPwd());
    }


    @Test
    @DisplayName("Register - Short Name - Expect TySWebException - The name is too short")
    void shortName() {
        String name = "u";
        String email = "user-service@alu.uclm.es";
        String pwd = "123456";
        String pwd2 = "123456";
        TySWebException exception = assertThrows(TySWebException.class, () -> {
            userService.register(name, email, pwd, pwd2);
        });

        String expectedMessage = "The name is too short";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("Register - Short Name - Expect TySWebException - The name is too short")
    void regusterEmptyName() {
        String name = "";
        String email = "user-service@alu.uclm.es";
        String pwd = "123456";
        String pwd2 = "123456";
        TySWebException exception = assertThrows(TySWebException.class, () -> {
            userService.register(name, email, pwd, pwd2);
        });

        String expectedMessage = "The name is too short";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("Register - Short pwd - Expect TySWebException - The password is too short")
    void shortPwd() {
        String name = "user-service.tsyweb";
        String email = "user-service@alu.uclm.es";
        String pwd = "1";
        String pwd2 = "1";
        TySWebException exception = assertThrows(TySWebException.class, () -> {
            userService.register(name, email, pwd, pwd2);
        });

        String expectedMessage = "The password is too short";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    @DisplayName("Register - Not equals pwd  - Expect TySWebException - Password does not match")
    void dontMatchPwd() {
        String name = "user-service.tsyweb";
        String email = "user-service@alu.uclm.es";
        String pwd = "123456";
        String pwd2 = "654321";
        TySWebException exception = assertThrows(TySWebException.class, () -> {
            userService.register(name, email, pwd, pwd2);
        });

        String expectedMessage = "Password does not match";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    @DisplayName("Register - Invalid Email - Expect TySWebException - Not valid email format")
    void invalidEmail() {
        String name = "user-service.tsyweb";
        String email = "user";
        String pwd = "123456";
        String pwd2 = "123456";
        TySWebException exception = assertThrows(TySWebException.class, () -> {
            userService.register(name, email, pwd, pwd2);
        });

        String expectedMessage = "Not valid email format";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("Login - Empty email - Expect TySWebException - Email cannot be empty")
    void loginEmptyEmail() {
        String pwd = "123456";
        String email = "";

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            userService.login(email, pwd);
        });

        String expectedMessage = "Email cannot be empty";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }


    @Test
    @DisplayName("Login - Invalid Email - Expect TySWebException - Not valid email format")
    void loginInvalidEmail() {
        String pwd = "123456";
        String email = "user";

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            userService.login(email, pwd);
        });

        String expectedMessage = "Not valid email format";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("Login - Empty pwd - Expect TySWebException - Password cannot be empty")
    void loginEmptyPwd() {
        String pwd = "";
        String email = "user-service@alu.uclm.es";

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            userService.login(email, pwd);
        });

        String expectedMessage = "Password cannot be empty";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("Login - Get User")
    void login() {
        String pwd = DigestUtils.sha512Hex("123456");
        String email = "user-service@alu.uclm.es";
        String name = "user-service.tsyweb";

        User user = userService.login(email, pwd);
        assertEquals(user.getName(), name);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getPwd(), pwd);
    }

    @Test
    @DisplayName("Login - User not found - Expect TySWebException - User nor found")
    void loginUserNotFound() {
        String pwd = "123456";
        String email = "user@alu.uclm.es";

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            userService.login(email, pwd);
        });

        String expectedMessage = "User nor found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Login - User not found - Expect TySWebException - Duplicate entry '****'")
    void registerDuplicatedKeys() {
        String name = "user-service.tsyweb";
        String email = "user-service@alu.uclm.es";
        String pwd = "123456";
        String pwd2 = "123456";

        User user = new User();
        user.setName(name);
        user.setPwd(pwd);
        user.setEmail(email);

        TySWebException exception = assertThrows(TySWebException.class, () -> {
            userService.register(name, email, pwd, pwd2);
        });

        String expectedMessage = "Duplicate entry '" + user.getEmail() + "'";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

    }

    @AfterEach
    @DisplayName("Delete - Delete - Expect TySWebException - User nor found")
    void delete() {
        userService.delete(user_id);
    }


}
