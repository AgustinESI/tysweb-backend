package edu.uclm.esi.tecsistweb.service;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RandomTest {
    private WebDriver driver;
    @BeforeAll
    public void setup(){
        System.setProperty("webdriver.edge.driver","/C:/Users/USUARIO/Desktop/geckodriver.exe/");
        driver = new FirefoxDriver();
    }
    @Test
    public void buscarRector() throws Exception{
        driver.get("https://directorio.uclm.es");
        //WebElement caja = driver.findElement(By.xpath(//*[@id="CPH_CajaCentro_tb_busqueda"]);
        //caja.click();
        //caja.sendKeys("garde");
        //webElement boton = driver.findElement(By.id("pinga"));
        //webElement tabla = driver.findElement(By.tagName());

    }
}
