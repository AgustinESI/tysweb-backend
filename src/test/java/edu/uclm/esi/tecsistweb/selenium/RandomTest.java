//package edu.uclm.esi.tecsistweb.service;
//
//
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.openqa.selenium.*;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//
//import org.openqa.selenium.firefox.FirefoxDriver;
//
//import java.time.Duration;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ExtendWith(SpringExtension.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class RandomTest {
//
//    @Value("${selenium.driver.path}")
//    private String seleniumDriverPath;
//
//    private WebDriver driver1, driver2;
//    private WebDriverWait wait1, wait2;
//    @BeforeAll
//    public void setup(){
//        //System.setProperty("webdriver.gecko.driver", "C:/Users/USUARIO/Desktop/geckodriver.exe");
//        System.setProperty("webdriver.gecko.driver", seleniumDriverPath);
//
//        driver1 = new FirefoxDriver();
////        driver2 = new FirefoxDriver();
//
//        wait1 = new WebDriverWait(driver1, Duration.ofSeconds(5));
////        wait2 = new WebDriverWait(driver2, Duration.ofSeconds(5));
//
//
//        driver1.manage().window().setPosition(new Point(0,0));
//        driver1.manage().window().setSize(new Dimension(800, 600));
//        driver1.manage().window().setPosition(new Point(800,0));
//        driver1.manage().window().setSize(new Dimension(800, 600));
//    }
//
//    private void login(WebDriver driver, String name, String pwd){
//        //Esto sería mejor hacerlo con id.
//        WebElement tbxName = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[1]/div[1]/input"));
//        WebElement tbxPWD = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[1]/div[2]/input"));
//
//        tbxName.click(); tbxName.clear(); tbxName.sendKeys(name);
//        tbxPWD.click(); tbxPWD.clear(); tbxPWD.sendKeys(pwd);
//    }
//    @Test
//    public void testEscenario() throws Exception{
//        driver1.get("http://alarcosj.esi.uclm.es/fakeBank");
//        driver2.get("http://alarcosj.esi.uclm.es/fakeBank");
//
//        this.register(driver1,wait1, "agustin98","agustin98@gmail.com","Agustin98","Agustin98");
//        this.register(driver2,wait2, "agustin981","agustin981@gmail.com","Agustin981","Agustin981");
//        this.deleteUser(driver1, wait1,"agustin98");
//        this.deleteUser(driver2, wait2,"agustin981");
//
//    }
//    private void deleteUser(WebDriver driver, WebDriverWait driverWait, String name) {
//        driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[1]/div[1]/input")));
//
//
//        WebElement tbxName = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[1]/div[1]/input"));
//        WebElement deleteLink = driver.findElement(By.xpath(" /html/body/div/oj-module/div[1]/div[2]/div/div/div/div[4]/div/a"));
//
//        tbxName.click(); tbxName.clear(); tbxName.sendKeys(name);
//        deleteLink.click();
//    }
//    private void register(WebDriver driver, WebDriverWait driverWait, String name, String mail, String pwd1, String pwd2) {
//        driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[2]/div/oj-navigation-list/div/div/ul/li[1]/a\"")));
//
//        WebElement link = driver.findElement(By.xpath("/html/body/div/div[2]/div/oj-navigation-list/div/div/ul/li[1]/a"));
//        link.click();
//
//        WebElement createAccount = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[3]/div/a"));
//        createAccount.click();
//
//        WebElement tbxName = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/input[1]"));
//        WebElement tbxEmail = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/input[2]"));
//        WebElement tbxPWD1 = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[1]/input[1]"));
//        WebElement tbxPWD2 = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[1]/input[2]"));
//        WebElement btnCreate = driver.findElement(By.id("btnCrearCuenta"));
//
//        tbxName.click(); tbxName.clear(); tbxName.sendKeys(name);
//        tbxEmail.click(); tbxEmail.clear(); tbxEmail.sendKeys(name);
//        tbxPWD1.click(); tbxPWD1.clear(); tbxPWD1.sendKeys(name);
//        tbxPWD2.click(); tbxPWD2.clear(); tbxPWD2.sendKeys(name);
//        btnCreate.click();
//    }
//}
