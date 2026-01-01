package org.example.ydgbackend.it;

import org.example.ydgbackend.Entity.Category;
import org.example.ydgbackend.Repository.CategoryRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "DOCKER_AVAILABLE", matches = "true")
class CategoryRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("demo")
            .withUsername("demo")
            .withPassword("demo");

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    CategoryRepo categoryRepo;

    @Test
    void saveAndFindCategory_roundtrip() {
        Category c = new Category("Garden");
        Category saved = categoryRepo.save(c);
        assertThat(saved.getCategoryId()).isNotNull();

        Category found = categoryRepo.findCategoryByCategoryId(saved.getCategoryId());
        assertThat(found).isNotNull();
        assertThat(found.getCategoryName()).isEqualTo("Garden");
    }

    @Test
    void findAll_andDelete_shouldWork() {
        Category c1 = categoryRepo.save(new Category("X"));
        Category c2 = categoryRepo.save(new Category("Y"));

        // findAll contains both
        assertThat(categoryRepo.findAll())
                .extracting(Category::getCategoryName)
                .contains("X", "Y");

        // delete one and verify it's gone
        categoryRepo.delete(c1);
        assertThat(categoryRepo.findById(c1.getCategoryId())).isEmpty();
        assertThat(categoryRepo.findById(c2.getCategoryId())).isPresent();
    }
}
