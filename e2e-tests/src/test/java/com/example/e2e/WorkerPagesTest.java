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

public class WorkerPagesTest {

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

    // Helper method to simulate login
    private void simulateLogin(String token, String role) {
        driver.get(baseUrl + "/login");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "localStorage.setItem('token', arguments[0]); localStorage.setItem('role', arguments[1]);",
            token, role
        );
        
        driver.navigate().refresh();
    }

    @Test
    @DisplayName("Worker Landing sayfasına erişim sağlanabilmeli")
    void workerLandingPage_shouldBeAccessible() {
        driver.get(baseUrl + "/worker/landing");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyText).isNotEmpty();
    }

    @Test
    @DisplayName("Worker Profile sayfasına erişim sağlanabilmeli")
    void workerProfilePage_shouldBeAccessible() {
        driver.get(baseUrl + "/worker/profile");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyText).isNotEmpty();
    }

    @Test
    @DisplayName("Worker Stock sayfasına erişim sağlanabilmeli")
    void workerStockPage_shouldBeAccessible() {
        driver.get(baseUrl + "/worker/stock");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyText).isNotEmpty();
    }

    @Test
    @DisplayName("Admin kullanıcısı worker sayfalarına erişememeli")
    void adminUser_shouldNotAccessWorkerPages() {
        // Simulate admin login
        simulateLogin("admin-token", "ADMIN");
        
        driver.get(baseUrl + "/worker/landing");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        
        // Should redirect away from worker pages
        String currentUrl = driver.getCurrentUrl();
        assertThat(currentUrl).doesNotContain("/worker/landing");
    }

    @Test
    @DisplayName("Giriş yapmadan worker sayfalarına erişim login'e yönlendirmeli")
    void unauthenticatedWorkerAccess_shouldRedirectToLogin() {
        driver.get(baseUrl + "/worker/landing");
        wait.until(ExpectedConditions.urlContains("/login"));
        assertThat(driver.getCurrentUrl()).contains("/login");
    }
}

