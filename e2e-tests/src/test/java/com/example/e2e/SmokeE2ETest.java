package com.example.e2e;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
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
    @DisplayName("Senaryo 1: Ana sayfa açılmalı")
    void homePage_shouldRender() {
        driver.get(baseUrl + "/");

        // React'in yüklenmesini beklemek için gövdede bir yazı arayalım
        // 'Demo App' yazısı görünene kadar bekle
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Demo App"));

        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyText).contains("Demo App");
    }

    @Test
    @DisplayName("Senaryo 2: Hello butonuna basınca sayfa değişmeli")
    void navigation_shouldWork() {
        driver.get(baseUrl + "/");

        // HATA ÇÖZÜMÜ:
        // 1. Text ile değil, ID ile bul (Daha garantidir)
        // 2. Tıklanabilir olana kadar bekle (Wait)

        try {
            // Önce sayfanın yüklendiğinden emin ol
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Demo App"));

            // Butonun görünür ve tıklanabilir olmasını bekle
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("hello-btn")));
            button.click();

            // Sayfa geçişini bekle (Hello Page başlığının gelmesini bekle)
            WebElement helloHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

            assertThat(helloHeader.getText()).isEqualTo("Hello Page");

        } catch (Exception e) {
            // Hata alırsak, o an sayfanın ne durumda olduğunu görelim (Debug için çok önemli)
            System.out.println("HATA ALINDI! O anki Sayfa Kaynağı:");
            System.out.println(driver.getPageSource());
            throw e; // Hatayı tekrar fırlat ki test başarısız sayılsın
        }
    }

    @Test
    @DisplayName("Senaryo 3: Backend bağlantısı kontrolü (Opsiyonel)")
    void backend_connection_check() {
        driver.get(baseUrl + "/");

        try {
            // 1. Sayfanın temel öğelerinin yüklendiğinden emin ol (Örn: Başlık veya Body)
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

            // Eğer backend verisi gelene kadar ekranda "Yükleniyor..." gibi bir şey yazıyorsa
            // onun kaybolmasını beklemek en garantisidir. (Opsiyonel ama önerilir)
            // wait.until(ExpectedConditions.invisibilityOfElementWithText(By.tagName("div"), "Loading..."));

            // 2. Backend cevabının gelmesi için biraz süre tanımış oluyoruz (Wait sayesinde)
            // Body elementini al
            WebElement body = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
            String bodyText = body.getText();

            // 3. Kontrolü yap
            // Eğer backend hatası varsa ekrana basılıyor, biz bunun OLMADIĞINI doğruluyoruz.
            assertThat(bodyText).doesNotContain("kurulamadı");
            assertThat(bodyText).doesNotContain("Network Error"); // Ekstra önlem

        } catch (Exception e) {
            // Hata durumunda debug için sayfa kaynağını yazdır
            System.out.println("HATA ALINDI! Backend kontrolü sırasında sayfa durumu:");
            System.out.println(driver.getPageSource());
            throw e; // Testin başarısız olması için hatayı fırlat
        }
    }
}