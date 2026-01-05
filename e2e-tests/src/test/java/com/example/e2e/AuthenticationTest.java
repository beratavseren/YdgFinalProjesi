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

public class AuthenticationTest {

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
    @DisplayName("Login sayfası açılmalı ve form görünmeli")
    void loginPage_shouldDisplay() {
        driver.get(baseUrl + "/login");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='email']")));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        assertThat(emailInput).isNotNull();
        assertThat(passwordInput).isNotNull();
        assertThat(loginButton).isNotNull();
        assertThat(driver.getPageSource()).contains("Login");
    }

    @Test
    @DisplayName("SignUp sayfası açılmalı ve form görünmeli")
    void signUpPage_shouldDisplay() {
        driver.get(baseUrl + "/signup");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        WebElement nameInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='nameSurname']")));
        WebElement emailInput = driver.findElement(By.cssSelector("input[name='email']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[name='password']"));
        WebElement signUpButton = driver.findElement(By.cssSelector("button[type='submit']"));

        assertThat(nameInput).isNotNull();
        assertThat(emailInput).isNotNull();
        assertThat(passwordInput).isNotNull();
        assertThat(signUpButton).isNotNull();
        assertThat(driver.getPageSource()).contains("Sign Up");
    }

    @Test
    @DisplayName("Login sayfasından SignUp sayfasına link ile geçiş yapılabilmeli")
    void navigation_fromLoginToSignUp_shouldWork() {
        driver.get(baseUrl + "/login");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        WebElement signUpLink = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Sign up")));
        signUpLink.click();

        wait.until(ExpectedConditions.urlContains("/signup"));
        assertThat(driver.getCurrentUrl()).contains("/signup");
        assertThat(driver.getPageSource()).contains("Sign Up");
    }

    @Test
    @DisplayName("SignUp sayfasından Login sayfasına link ile geçiş yapılabilmeli")
    void navigation_fromSignUpToLogin_shouldWork() {
        driver.get(baseUrl + "/signup");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Login")));
        loginLink.click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertThat(driver.getCurrentUrl()).contains("/login");
        assertThat(driver.getPageSource()).contains("Login");
    }

    @Test
    @DisplayName("Giriş yapmadan protected sayfaya erişim login'e yönlendirmeli")
    void unauthenticatedAccess_shouldRedirectToLogin() {
        driver.get(baseUrl + "/admin/dashboard");

        wait.until(ExpectedConditions.urlContains("/login"));
        assertThat(driver.getCurrentUrl()).contains("/login");
    }

    @Test
    @DisplayName("Ana sayfa giriş yapmamış kullanıcıyı login'e yönlendirmeli")
    void homePage_unauthenticated_shouldRedirectToLogin() {
        driver.get(baseUrl + "/");

        wait.until(ExpectedConditions.urlContains("/login"));
        assertThat(driver.getCurrentUrl()).contains("/login");
    }
}

