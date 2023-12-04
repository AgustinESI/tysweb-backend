package edu.uclm.esi.tecsistweb.selenium;


import edu.uclm.esi.tecsistweb.repository.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.manager.SeleniumManager;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.awt.*;
import java.time.Duration;

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
    private static final String TSYWEB_FRONT_URL_LOGIN = "/login";

    private static boolean executeTest = false;

    private WebDriver driver1;
    private WebDriverWait wait1;


    @BeforeAll
    public void beforeTestsClass() throws Exception {
        userDAO.deleteAll();
    }

    @BeforeEach
    public void setup() throws Exception {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--test-type");
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");

        String driverPath = SeleniumManager.getInstance().getDriverPath(options, false).getDriverPath();

        System.setProperty("webdriver.chrome.driver", driverPath);

        driver1 = new ChromeDriver(options);
        wait1 = new WebDriverWait(driver1, Duration.ofSeconds(5));

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
    public void register(String name, String email, String pwd1, String pwd2) throws AWTException {
        Assumptions.assumeTrue(executeTest);
        driver1.get(TSYWEB_FRONT_URL + TSYWEB_FRONT_URL_REGISTER);

        SeleniumUtils.sendKeys(driver1, "floatingInputName", name, SeleniumUtils.FindBy.ID);
        SeleniumUtils.sendKeys(driver1, "floatingInputEmail", email, SeleniumUtils.FindBy.ID);
        SeleniumUtils.sendKeys(driver1, "floatingPassword1", pwd1, SeleniumUtils.FindBy.ID);
        SeleniumUtils.sendKeys(driver1, "floatingPassword2", pwd2, SeleniumUtils.FindBy.ID);
        SeleniumUtils.click(driver1, "register", SeleniumUtils.FindBy.ID);

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
    public void login(String name, String email, String pwd1, String pwd2) throws AWTException {
        Assumptions.assumeTrue(executeTest);
        driver1.get(TSYWEB_FRONT_URL + TSYWEB_FRONT_URL_LOGIN);

        SeleniumUtils.sendKeys(driver1, "floatingInputEmail", email, SeleniumUtils.FindBy.ID);
        SeleniumUtils.sendKeys(driver1, "floatingPassword", pwd1    , SeleniumUtils.FindBy.ID);

        SeleniumUtils.click(driver1, "login", SeleniumUtils.FindBy.ID);

        String nameUser = SeleniumUtils.findElement(driver1, "name-user", SeleniumUtils.FindBy.ID).getText();
        String titleAbout = SeleniumUtils.findElement(driver1, "about-title", SeleniumUtils.FindBy.ID).getText();

        assertEquals(nameUser, name);
        assertEquals("About this project", titleAbout);
        assertEquals(TSYWEB_FRONT_URL, driver1.getCurrentUrl());
    }


    @AfterEach
    public void endTest() throws InterruptedException {
        driver1.close();
        driver1.quit();
    }

    @AfterAll
    public void endTestsClass() {
        userDAO.deleteAll();
    }

}
