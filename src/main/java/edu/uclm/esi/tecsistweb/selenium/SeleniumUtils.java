package edu.uclm.esi.tecsistweb.selenium;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

@Slf4j
public class SeleniumUtils {

    private static final Duration DEFAULT_WAIT = Duration.ofSeconds(1500);
    private static final int DEFAULT_WAIT_SECONDS = 1500;


    public enum FindBy {
        ID, CSS_SELECTOR, CLASS_NAME, NAME, LINK_TEXT, TAG_NAME, X_PATH;
    }

    /**
     * Finds a web element based on the specified criteria using the provided WebDriver, element name, and FindBy annotation.
     *
     * @param driver The WebDriver instance to use for locating the element.
     * @param name   The name or identifier of the element to be located.
     * @param fb     The FindBy annotation specifying the criteria for locating the element.
     * @return The WebElement representing the located element, or null if the element is not found.
     * @throws RuntimeException If the WebDriver is null, the element name is blank or null, or the FindBy annotation is null.
     *                          An exception is thrown to indicate invalid input parameters.
     * @see org.openqa.selenium.WebDriver
     * @see org.openqa.selenium.WebElement
     * @see org.openqa.selenium.support.FindBy
     */
    public static WebElement findElement(WebDriver driver, String name, FindBy fb) {

        try {
            if (driver != null && StringUtils.isNotBlank(name) && fb != null) {
                return driver.findElement(findBy(fb, name));
            } else {
                throw new IllegalArgumentException("Driver, name, or fb are null");
            }
        } catch (NoSuchElementException e) {
            log.error("[ERROR] Element not found with " + fb + " and name: " + name);
            throw new RuntimeException("Element not found", e);
        } catch (Exception e) {
            log.error("[ERROR] Error finding element with " + fb + " and name: " + name + "\n" + e.getMessage());
            throw new RuntimeException("Error finding element", e);
        }
    }


    public static void submit(WebDriver driver, String name, FindBy fb) {
        try {
            if (driver != null && StringUtils.isNotBlank(name) && fb != null) {
                Thread.sleep(DEFAULT_WAIT_SECONDS);
                driver.findElement(findBy(fb, name)).submit();
            } else {
                throw new RuntimeException("Driver, name or fb are nulls");
            }
        } catch (Exception e) {
            log.error("[ERROR] sending keys with id: " + name + "\n " + e.getMessage());
        }
    }

    /**
     * Sends the specified keys to a web element identified by the given criteria using the provided WebDriver,
     * element name, keys to be sent, and FindBy annotation.
     *
     * @param driver The WebDriver instance used for locating and interacting with the element.
     * @param name   The name or identifier of the element to which keys should be sent.
     * @param keys   The keys to be sent to the element.
     * @param fb     The FindBy annotation specifying the criteria for locating the element.
     * @throws RuntimeException If the WebDriver is null, the element name or keys are blank or null, or the FindBy annotation is null.
     *                          An exception is thrown to indicate invalid input parameters.
     * @see org.openqa.selenium.WebDriver
     * @see org.openqa.selenium.WebElement
     * @see org.openqa.selenium.support.FindBy
     */
    public static void sendKeys(WebDriver driver, String name, String keys, FindBy fb) {

        try {
            if (driver != null && StringUtils.isNotBlank(name) && StringUtils.isNotBlank(keys) && fb != null) {
                driver.findElement(findBy(fb, name)).sendKeys(keys);
            } else {
                throw new RuntimeException("Driver, name, keys or fb are nulls");
            }
        } catch (Exception e) {
            log.error("[ERROR] sending keys with id: " + name + "\n " + e.getMessage());
        }
    }

    /**
     * Clicks on a web element identified by the given criteria using the provided WebDriver, element name, and FindBy annotation.
     * Waits for the default time specified in DEFAULT_WAIT after clicking the element.
     *
     * @param driver The WebDriver instance used for locating and interacting with the element.
     * @param name   The name or identifier of the element to be clicked.
     * @param fb     The FindBy annotation specifying the criteria for locating the element.
     * @throws RuntimeException If the WebDriver is null, the element name is blank or null, or the FindBy annotation is null.
     *                          An exception is thrown to indicate invalid input parameters.
     * @see org.openqa.selenium.WebDriver
     * @see org.openqa.selenium.WebElement
     * @see org.openqa.selenium.support.FindBy
     * @see org.openqa.selenium.support.ui.WebDriverWait
     */
    public static void click(WebDriver driver, String name, FindBy fb) {

        try {
            if (driver != null && StringUtils.isNotBlank(name) && fb != null) {
                driver.findElement(findBy(fb, name)).click();
                Thread.sleep(DEFAULT_WAIT_SECONDS);
            } else {
                throw new RuntimeException("Driver, name or fb are nulls");
            }
        } catch (Exception e) {
            log.error("[ERROR] sending keys with id: " + name + "\n " + e.getMessage());
        }
    }

    /**
     * Waits until a web element identified by the given criteria becomes clickable and then clicks on it.
     * Uses the provided WebDriver, element chain, and FindBy annotation for element identification.
     *
     * @param driver The WebDriver instance used for locating and interacting with the element.
     * @param chain  The chain or identifier of the element to wait for and click.
     * @param fb     The FindBy annotation specifying the criteria for locating the element.
     * @throws RuntimeException If the WebDriver is null, the element chain is blank or null, the FindBy annotation is null,
     *                          or the element is not clickable within the default wait time.
     * @see org.openqa.selenium.WebDriver
     * @see org.openqa.selenium.support.ui.WebDriverWait
     * @see org.openqa.selenium.support.ui.ExpectedConditions#elementToBeClickable(org.openqa.selenium.By)
     */
    public static void waitUntilElementToBeClickcable(WebDriver driver, String chain, FindBy fb) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT);
            if (driver != null && wait != null && StringUtils.isNotBlank(chain) && fb != null) {
                wait.until(ExpectedConditions.elementToBeClickable(findBy(fb, chain))).click();
            } else {
                throw new RuntimeException("Driver, chain or fb are nulls");
            }
        } catch (Exception e) {
            log.error("[ERROR] in waitUntilElementToBeClickcable() chain:" + chain + "\n" + e.getMessage());
        }
    }


    /**
     * Waits until a web element identified by the given criteria becomes visible and then clicks on it.
     * Uses the provided WebDriver, element chain, and FindBy annotation for element identification.
     *
     * @param driver The WebDriver instance used for locating and interacting with the element.
     * @param chain  The chain or identifier of the element to wait for and click.
     * @param fb     The FindBy annotation specifying the criteria for locating the element.
     * @throws RuntimeException If the WebDriver is null, the element chain is blank or null, the FindBy annotation is null,
     *                          or the element is not visible within the default wait time.
     * @see org.openqa.selenium.WebDriver
     * @see org.openqa.selenium.support.ui.WebDriverWait
     * @see org.openqa.selenium.support.ui.ExpectedConditions#visibilityOfElementLocated(org.openqa.selenium.By)
     */
    public static void waitUntilVisibilityOfElementLocated(WebDriver driver, String chain, FindBy fb) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT);
            if (driver != null && wait != null && StringUtils.isNotBlank(chain) && fb != null) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(findBy(fb, chain))).click();
            } else {
                throw new RuntimeException("Driver, chain or fb are nulls");
            }
        } catch (Exception e) {
            log.error("[ERROR] in waitUntilElementToBeClickcable() chain:" + chain + "\n" + e.getMessage());
        }
    }


    /**
     * Creates a By object based on the specified FindBy annotation type and the element identifier (name).
     *
     * @param fb   The FindBy annotation specifying the type of the element locator.
     * @param name The identifier of the element (e.g., ID, CSS selector, name, link text, etc.).
     * @return A By object representing the element locator based on the specified FindBy type and identifier.
     * @throws RuntimeException If the FindBy type is not recognized, an exception is thrown to indicate an invalid locator type.
     * @see org.openqa.selenium.By
     * @see org.openqa.selenium.support.FindBy
     */
    private static By findBy(FindBy fb, String name) {
        switch (fb) {
            case ID:
                return By.id(name);
            case CSS_SELECTOR:
                return By.cssSelector(name);
            case NAME:
                return By.name(name);
            case LINK_TEXT:
                return By.linkText(name);
            case CLASS_NAME:
                return By.className(name);
            case TAG_NAME:
                return By.tagName(name);
            case X_PATH:
                return By.xpath(name);
            default:
                throw new RuntimeException("There is no FindBy of type:" + fb.name());
        }
    }

    /**
     * Checks the availability of a URL by sending an HTTP HEAD request and examining the response code.
     *
     * @param urlString The URL to be checked for availability.
     * @return {@code true} if the URL is available (returns HTTP_OK), {@code false} otherwise.
     * @throws IllegalArgumentException If the provided URL string is malformed or cannot be parsed.
     * @see java.net.URL
     * @see java.net.HttpURLConnection
     */
    public static boolean isURLAvailable(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            // Handle exceptions, such as MalformedURLException or IOException
            return false;
        }
    }

}
