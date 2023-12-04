//package edu.uclm.esi.tecsistweb.service;
//
//
//import edu.uclm.esi.tecsistweb.model.User;
//import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
//import edu.uclm.esi.tecsistweb.repository.UserDAO;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class UserServiceTest {
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    private UserDAO userDAO;
//
//    private String user_id = "";
//
//    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger("UserServiceTest");
//
//
//    @ParameterizedTest
//    @CsvSource({
//            ", user-service@alu.uclm.es, 123456, 123456",
//            "u, user-service@alu.uclm.es, 123456, 123456",
//    })
//    @DisplayName("Register - Name null and short - TySWebException - The name is too short")
//    @Order(1)
//    void test1(String name, String email, String pwd1, String pwd2) {
//        User user = new User();
//        user.setName(name);
//        user.setPwd(pwd1);
//        user.setEmail(email);
//
//        TySWebException exception = assertThrows(TySWebException.class, () -> {
//            userService.register(name, email, pwd1, pwd2);
//        });
//
//        String expectedMessage = "The name is too short";
//        String actualMessage = exception.getMessage();
//        assertEquals(expectedMessage, actualMessage);
//        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
//    }
//
//
//    @ParameterizedTest
//    @CsvSource({
//            "user-service.tsyweb, user-service@alu.uclm.es, 1, 1",
//            "user-service.tsyweb, user-service@alu.uclm.es, 654321, 123456",
//    })
//    @DisplayName("Register - Pwd short and not equals - TySWebException")
//    @Order(2)
//    void test2(String name, String email, String pwd1, String pwd2) {
//        User user = new User();
//        user.setName(name);
//        user.setPwd(pwd1);
//        user.setEmail(email);
//
//        TySWebException exception = assertThrows(TySWebException.class, () -> {
//            userService.register(name, email, pwd1, pwd2);
//        });
//
//
//        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "user-service.tsyweb, user-service, 123456, 123456",
//    })
//    @DisplayName("Register - Pwd short and not equals - TySWebException - Not valid email format")
//    @Order(3)
//    void test3(String name, String email, String pwd1, String pwd2) {
//        User user = new User();
//        user.setName(name);
//        user.setPwd(pwd1);
//        user.setEmail(email);
//
//        TySWebException exception = assertThrows(TySWebException.class, () -> {
//            userService.register(name, email, pwd1, pwd2);
//        });
//
//        String expectedMessage = "Not valid email format";
//        String actualMessage = exception.getMessage();
//        assertEquals(actualMessage, expectedMessage);
//        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
//    }
//
//    @Test
//    @DisplayName("Register - Pwd short and not equals - TySWebException - Duplicate entry ***")
//    @Order(4)
//    void test4() {
//        String name = "user-service.tsyweb";
//        String email = "user-service@alu.uclm.es";
//        String pwd1 = "123456";
//        String pwd2 = "123456";
//
//        User user = new User();
//        user.setName(name);
//        user.setPwd(pwd1);
//        user.setEmail(email);
//
//        userService.register(name, email, pwd1, pwd2);
//
//        TySWebException exception = assertThrows(TySWebException.class, () -> {
//            userService.register(name, email, pwd1, pwd2);
//        });
//
//        String expectedMessage = "Duplicate entry '" + user.getEmail() + "'";
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "user-service@alu.uclm.es, ",
//            ", 123456",
//            "user-service, 123456",
//    })
//    @DisplayName("Login - Pwd/Email empty not valid Email,  - TySWebException")
//    @Order(5)
//    void test5(String email, String pwd) {
//
//        TySWebException exception = assertThrows(TySWebException.class, () -> {
//            userService.login(email, pwd);
//        });
//        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "user-service@alu.uclm.es, 123456",
//    })
//    @DisplayName("Login - User not found  - TySWebException - User nor found")
//    @Order(6)
//    void test6(String email, String pwd) {
//
//        TySWebException exception = assertThrows(TySWebException.class, () -> {
//            userService.login(email, pwd);
//        });
//
//        String expectedMessage = "User not found";
//        String actualMessage = exception.getMessage();
//        assertEquals(actualMessage, expectedMessage);
//        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "user-service.tsyweb, user-service@alu.uclm.es, 123456, 123456",
//    })
//    @DisplayName("Login")
//    @Order(7)
//    void test7(String name, String email, String pwd1, String pwd2) {
//        User user_saved = userService.register(name, email, pwd1, pwd2);
//        User user_find = userService.login(email, DigestUtils.sha512Hex(pwd1));
//
//        assertEquals(user_saved.getId(), user_find.getId());
//        assertEquals(user_saved.getName(), user_find.getName());
//        assertEquals(user_saved.getPwd(), user_find.getPwd());
//        assertEquals(user_saved.getEmail(), user_find.getEmail());
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "user-service.tsyweb, user-service@alu.uclm.es, 123456, 123456",
//    })
//    @DisplayName("Delete - Not found - TySWebException - User nor found")
//    @Order(8)
//    void test8(String name, String email, String pwd1, String pwd2) {
//
//        TySWebException exception = assertThrows(TySWebException.class, () -> {
//            userService.login(email, pwd1);
//        });
//
//        String expectedMessage = "User not found";
//        String actualMessage = exception.getMessage();
//        assertEquals(actualMessage, expectedMessage);
//        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
//    }
//
//
//    @AfterEach
//    void end() {
//        userDAO.deleteAll();
//    }
//}
