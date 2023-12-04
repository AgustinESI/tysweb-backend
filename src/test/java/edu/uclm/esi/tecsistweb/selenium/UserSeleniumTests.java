package edu.uclm.esi.tecsistweb.selenium;


import edu.uclm.esi.tecsistweb.repository.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.manager.SeleniumManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class UserSeleniumTests {


    @Autowired
    private UserDAO userDAO;

    private static final String TSYWEB_FRONT_URL = "http://localhost:4200/";
    private static final String TSYWEB_FRONT_URL_REGISTER = "/register";
    private static final String TSTWEB_FRONT_URL_4INLINE = "/4inline";
    private static final String TSTWEB_FRONT_URL_MASTERMIND = "/mastermind";
    private static final String TSYWEB_FRONT_URL_LOGIN = "/login";

    private static boolean executeTest = false;

    private WebDriver driver1;
    private WebDriver driver2;


    @BeforeAll
    public void beforeTestsClass() {
        userDAO.deleteAll();
    }

    @BeforeEach
    public void setup() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--test-type");
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");

        String driverPath = SeleniumManager.getInstance().getDriverPath(options, false).getDriverPath();

        System.setProperty("webdriver.chrome.driver", driverPath);

        driver1 = new ChromeDriver(options);
        driver2 = new ChromeDriver(options);

        executeTest = SeleniumUtils.isURLAvailable(TSYWEB_FRONT_URL);
        if (!executeTest) {
            log.error("Front Service not available on path: " + TSYWEB_FRONT_URL);
        }

    }

    @ParameterizedTest
    @CsvSource({
            "selenium-user.tsyweb, selenium-user@alu.uclm.es, 123456, 123456",
    })
    @Order(1)
    @DisplayName("Registers a new User")
    public void register(String name, String email, String pwd1, String pwd2) {
        Assumptions.assumeTrue(executeTest);
        this.registerUser(driver1, name, email, pwd1, pwd2);

        String nameUser = SeleniumUtils.findElement(driver1, "name-user", SeleniumUtils.FindBy.ID).getText();
        String titleAbout = SeleniumUtils.findElement(driver1, "about-title", SeleniumUtils.FindBy.ID).getText();

        assertEquals(nameUser, name);
        assertEquals("About this project", titleAbout);
        assertEquals(TSYWEB_FRONT_URL, driver1.getCurrentUrl());
    }

    @ParameterizedTest
    @CsvSource({
            "selenium-user.tsyweb, selenium-user@alu.uclm.es, 123456, 123456",
    })
    @DisplayName("Registers a new User")
    @Order(2)
    public void login(String name, String email, String pwd1, String pwd2) {
        Assumptions.assumeTrue(executeTest);
        this.loginUser(driver1, email, pwd1);

        String nameUser = SeleniumUtils.findElement(driver1, "name-user", SeleniumUtils.FindBy.ID).getText();
        String titleAbout = SeleniumUtils.findElement(driver1, "about-title", SeleniumUtils.FindBy.ID).getText();

        assertEquals(nameUser, name);
        assertEquals("About this project", titleAbout);
        assertEquals(TSYWEB_FRONT_URL, driver1.getCurrentUrl());
    }

    @ParameterizedTest
    @CsvSource({
            "selenium-user.tsyweb, selenium-user@alu.uclm.es, 123456, 123456"
    })
    @DisplayName("Play 4 in Line")
    @Order(3)
    public void playFourInLine(String name, String email, String pwd1, String pwd2) {
        Assumptions.assumeTrue(executeTest);

        this.loginUser(driver1, email, pwd1);
        this.registerUser(driver2, "2" + name, "2" + email, pwd1, pwd2);

        driver1.get(TSYWEB_FRONT_URL + TSTWEB_FRONT_URL_4INLINE);
        driver2.get(TSYWEB_FRONT_URL + TSTWEB_FRONT_URL_4INLINE);


        WebElement user1 = SeleniumUtils.findElement(driver1, "user_0", SeleniumUtils.FindBy.ID);
        String backgroundColor = user1.getCssValue("background-color");

        if (backgroundColor.equals("rgba(150, 219, 168, 1)")) {
            for (int i = 0; i < 10; i++) {
                SeleniumUtils.click(driver1, "button2_" + new Random().nextInt(7), SeleniumUtils.FindBy.ID);
                SeleniumUtils.click(driver2, "button2_" + new Random().nextInt(7), SeleniumUtils.FindBy.ID);
            }
        } else {
            for (int i = 0; i < 10; i++) {
                SeleniumUtils.click(driver2, "button2_" + new Random().nextInt(7), SeleniumUtils.FindBy.ID);
                SeleniumUtils.click(driver1, "button2_" + new Random().nextInt(7), SeleniumUtils.FindBy.ID);
            }
        }

    }

    @ParameterizedTest
    @CsvSource({
            "selenium-user.tsyweb, selenium-user@alu.uclm.es, 123456, 123456"
    })
    @DisplayName("Play MasterMind")
    @Order(4)
    public void playMasterMind(String name, String email, String pwd1, String pwd2) {
        Assumptions.assumeTrue(executeTest);
        this.loginUser(driver1, email, pwd1);
        this.loginUser(driver2, "2" + email, pwd1);

        driver1.get(TSYWEB_FRONT_URL + TSTWEB_FRONT_URL_MASTERMIND);
        driver2.get(TSYWEB_FRONT_URL + TSTWEB_FRONT_URL_MASTERMIND);

        String[] buttons = {"button_K", "button_B", "button_M", "button_G", "button_O", "button_R", "button_P", "button_Y"};

        WebElement user1 = SeleniumUtils.findElement(driver1, "user_0", SeleniumUtils.FindBy.ID);
        String backgroundColor = user1.getCssValue("background-color");

        if (backgroundColor.equals("rgba(150, 219, 168, 1)")) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    SeleniumUtils.click(driver1, buttons[new Random().nextInt(buttons.length)], SeleniumUtils.FindBy.ID);
                }

                for (int j = 0; j < 6; j++) {
                    SeleniumUtils.click(driver2, buttons[new Random().nextInt(buttons.length)], SeleniumUtils.FindBy.ID);
                }
            }
        } else {
            for (int j = 0; j < 6; j++) {
                SeleniumUtils.click(driver1, buttons[new Random().nextInt(buttons.length)], SeleniumUtils.FindBy.ID);
            }

            for (int j = 0; j < 6; j++) {
                SeleniumUtils.click(driver2, buttons[new Random().nextInt(buttons.length)], SeleniumUtils.FindBy.ID);
            }
        }
    }

    private void loginUser(WebDriver driver, String email, String pwd1) {
        driver.get(TSYWEB_FRONT_URL + TSYWEB_FRONT_URL_LOGIN);

        SeleniumUtils.sendKeys(driver, "floatingInputEmail", email, SeleniumUtils.FindBy.ID);
        SeleniumUtils.sendKeys(driver, "floatingPassword", pwd1, SeleniumUtils.FindBy.ID);
        SeleniumUtils.click(driver, "login", SeleniumUtils.FindBy.ID);
    }


    private void registerUser(WebDriver driver, String name, String email, String pwd1, String pwd2) {
        driver.get(TSYWEB_FRONT_URL + TSYWEB_FRONT_URL_REGISTER);

        SeleniumUtils.sendKeys(driver, "floatingInputName", name, SeleniumUtils.FindBy.ID);
        SeleniumUtils.sendKeys(driver, "floatingInputEmail", email, SeleniumUtils.FindBy.ID);
        SeleniumUtils.sendKeys(driver, "floatingPassword1", pwd1, SeleniumUtils.FindBy.ID);
        SeleniumUtils.sendKeys(driver, "floatingPassword2", pwd2, SeleniumUtils.FindBy.ID);
        SeleniumUtils.click(driver, "register", SeleniumUtils.FindBy.ID);
    }

    @AfterEach
    public void endTest() {
        driver1.close();
        driver1.quit();
        driver2.close();
        driver2.quit();
    }

    @AfterAll
    public void endTestsClass() {
        userDAO.deleteAll();
    }

}
