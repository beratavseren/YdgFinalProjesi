package com.example.e2e;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class SmokeE2E {

    private WebDriver driver;
    private WebDriverWait wait;

    // Jenkins/Docker ortamında servis ismini çözemezse localhost dener
    private final String baseUrl = System.getProperty("e2e.baseUrl", "http://localhost:3000");

    @BeforeEach
    void setup() {
        // HtmlUnitDriver Ayarları
        driver = new HtmlUnitDriver(true) {
            // JavaScript hatalarını (React uyarılarını) görmezden gelmesi için ayar
            // Bu kısım Override edilerek WebClient ayarları gevşetilir
            @Override
            protected com.gargoylesoftware.htmlunit.WebClient modifyWebClient(com.gargoylesoftware.htmlunit.WebClient client) {
                final com.gargoylesoftware.htmlunit.WebClient webClient = super.modifyWebClient(client);
                webClient.getOptions().setThrowExceptionOnScriptError(false); // JS hatasında patlama
                webClient.getOptions().setCssEnabled(false); // CSS render etmeye çalışma (Hızlanır)
                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                return webClient;
            }
        };

        // Akıllı Bekleme (Explicit Wait): Maksimum 10 saniye bekle
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

        // App.js içinde backend'den veri çekip ekrana basıyoruz.
        // Eğer bağlantı hatası varsa ekranda "iletişim kurulamadı" yazar.
        // Biz hatasız olduğunu doğrulamak istiyoruz.
        String bodyText = driver.findElement(By.tagName("body")).getText();

        // Basitçe: Sayfada "hata" veya "Error" kelimesi OLMAMALI
        assertThat(bodyText).doesNotContain("kurulamadı");
    }
}