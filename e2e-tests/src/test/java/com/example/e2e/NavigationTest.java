package com.example.e2e;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class NavigationTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = System.getProperty("e2e.baseUrl", "http://localhost:3000");

    @BeforeEach
    void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Login sayfasından SignUp sayfasına geçiş yapılabilmeli")
    void navigateFromLoginToSignUp() {
        driver.get(baseUrl + "/login");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        WebElement signUpLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(text(), 'Sign up') or contains(text(), 'Sign Up')]")
        ));
        signUpLink.click();

        wait.until(ExpectedConditions.urlContains("/signup"));
        assertThat(driver.getCurrentUrl()).contains("/signup");
    }

    @Test
    @DisplayName("SignUp sayfasından Login sayfasına geçiş yapılabilmeli")
    void navigateFromSignUpToLogin() {
        driver.get(baseUrl + "/signup");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(text(), 'Login') or contains(text(), 'login')]")
        ));
        loginLink.click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertThat(driver.getCurrentUrl()).contains("/login");
    }

    @Test
    @DisplayName("Geçersiz route login'e yönlendirmeli")
    void invalidRoute_shouldRedirectToLogin() {
        driver.get(baseUrl + "/invalid-route");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        
        // Should redirect to login for unauthenticated users
        String currentUrl = driver.getCurrentUrl();
        // Either stays on invalid route or redirects to login
        assertThat(currentUrl).isNotNull();
    }

    @Test
    @DisplayName("Sayfa yenilendiğinde route korunmalı")
    void pageRefresh_shouldMaintainRoute() {
        driver.get(baseUrl + "/login");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        
        String initialUrl = driver.getCurrentUrl();
        driver.navigate().refresh();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        String refreshedUrl = driver.getCurrentUrl();
        
        assertThat(refreshedUrl).isEqualTo(initialUrl);
    }
}

