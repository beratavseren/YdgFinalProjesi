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

public class SmokeE2ETest {

    private WebDriver driver;
    private WebDriverWait wait;

    // Jenkins/Docker ortamında servis ismini çözemezse localhost dener
    private final String baseUrl = System.getProperty("e2e.baseUrl", "http://localhost:3000");

    @BeforeEach
    void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        // Docker içindeki Chrome zaten headless gibidir ama yine de ekleyelim
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        // KRİTİK NOKTA: ChromeDriver yerine RemoteWebDriver kullanıyoruz
        // Bu sayede testler, Docker içindeki 'selenium-chrome' konteynerine bağlanır.
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
    @DisplayName("Senaryo 1: Ana sayfa açılmalı ve login'e yönlendirmeli")
    void homePage_shouldRedirectToLogin() {
        driver.get(baseUrl + "/");

        // Ana sayfa giriş yapmamış kullanıcıyı login'e yönlendirmeli
        wait.until(ExpectedConditions.urlContains("/login"));
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyText).contains("Login");
    }

    @Test
    @DisplayName("Senaryo 2: Login sayfası doğru şekilde yüklenmeli")
    void loginPage_shouldLoadCorrectly() {
        driver.get(baseUrl + "/login");

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            String bodyText = driver.findElement(By.tagName("body")).getText();
            
            // Login sayfasında olmalı
            assertThat(bodyText).contains("Login");
            
            // Form elemanları olmalı
            WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='email']")
            ));
            assertThat(emailInput).isNotNull();

        } catch (Exception e) {
            System.out.println("HATA ALINDI! O anki Sayfa Kaynağı:");
            System.out.println(driver.getPageSource());
            throw e;
        }
    }

    @Test
    @DisplayName("Senaryo 3: Frontend sayfaları yüklenebilmeli")
    void frontendPages_shouldLoad() {
        driver.get(baseUrl + "/login");

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
            WebElement body = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
            String bodyText = body.getText();

            // Sayfa yüklendi, içerik olmalı
            assertThat(bodyText).isNotEmpty();
            
            // Önemli hata mesajları olmamalı
            assertThat(bodyText).doesNotContain("Cannot GET");
            assertThat(bodyText).doesNotContain("404");
            assertThat(bodyText).doesNotContain("Network Error");

        } catch (Exception e) {
            System.out.println("HATA ALINDI! Sayfa durumu:");
            System.out.println(driver.getPageSource());
            throw e;
        }
    }
}