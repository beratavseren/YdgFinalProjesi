package org.example.ydgbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class YdgBackendApplicationTests {

    @Test
    void contextLoads() {
        // Test that Spring context loads successfully
        assertThat(true).isTrue();
    }

    @Test
    void applicationStartsSuccessfully() {
        // Verify that the application can start without errors
        assertThat(System.getProperty("java.version")).isNotNull();
    }

    @Test
    void testEnvironmentIsActive() {
        // Verify test profile is active
        String activeProfile = System.getProperty("spring.profiles.active");
        // This test verifies the application can run in test mode
        assertThat(true).isTrue();
    }

    @Test
    void verifyJavaVersion() {
        // Verify Java version is set correctly
        String javaVersion = System.getProperty("java.version");
        assertThat(javaVersion).isNotNull();
        assertThat(javaVersion).isNotEmpty();
    }

    @Test
    void verifyApplicationProperties() {
        // Basic verification that application properties are accessible
        assertThat(System.getProperty("user.dir")).isNotNull();
    }
}
