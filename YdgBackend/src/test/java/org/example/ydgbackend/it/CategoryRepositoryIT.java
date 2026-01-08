package org.example.ydgbackend.it;

import jakarta.transaction.Transactional;
import org.example.ydgbackend.Entity.Category;
import org.example.ydgbackend.Repository.CategoryRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
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

        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
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

        assertThat(categoryRepo.findAll())
                .extracting(Category::getCategoryName)
                .contains("X", "Y");

        categoryRepo.delete(c1);
        assertThat(categoryRepo.findById(c1.getCategoryId())).isEmpty();
        assertThat(categoryRepo.findById(c2.getCategoryId())).isPresent();
    }

    @Test
    void saveCategory_withSpecialCharacters_handlesCorrectly() {
        Category c = new Category("Electronics & Appliances");
        Category saved = categoryRepo.save(c);
        assertThat(saved.getCategoryId()).isNotNull();
        assertThat(saved.getCategoryName()).isEqualTo("Electronics & Appliances");
    }

    @Test
    void saveCategory_withUnicodeCharacters_handlesCorrectly() {
        Category c = new Category("カテゴリー");
        Category saved = categoryRepo.save(c);
        assertThat(saved.getCategoryId()).isNotNull();
        assertThat(saved.getCategoryName()).isEqualTo("カテゴリー");
    }

    @Test
    void saveCategory_withLongName_handlesCorrectly() {
        String longName = "A".repeat(100);
        Category c = new Category(longName);
        Category saved = categoryRepo.save(c);
        assertThat(saved.getCategoryName()).isEqualTo(longName);
    }

    @Test
    void findCategoryByCategoryId_withNonExistentId_returnsNull() {
        Category found = categoryRepo.findCategoryByCategoryId(99999L);
        assertThat(found).isNull();
    }

    @Test
    void saveMultipleCategories_andFindAll_returnsAll() {
        categoryRepo.deleteAll();
        
        Category c1 = categoryRepo.save(new Category("Category1"));
        Category c2 = categoryRepo.save(new Category("Category2"));
        Category c3 = categoryRepo.save(new Category("Category3"));

        assertThat(categoryRepo.findAll())
                .hasSizeGreaterThanOrEqualTo(3)
                .extracting(Category::getCategoryName)
                .contains("Category1", "Category2", "Category3");
    }

    @Test
    void updateCategoryName_roundtrip() {
        Category c = categoryRepo.save(new Category("Original"));
        Long id = c.getCategoryId();
        
        c.setCategoryName("Updated");
        categoryRepo.save(c);
        
        Category found = categoryRepo.findCategoryByCategoryId(id);
        assertThat(found).isNotNull();
        assertThat(found.getCategoryName()).isEqualTo("Updated");
    }

    @Test
    void deleteAllCategories_clearsDatabase() {
        categoryRepo.save(new Category("Temp1"));
        categoryRepo.save(new Category("Temp2"));
        
        categoryRepo.deleteAll();
        
        assertThat(categoryRepo.findAll()).isEmpty();
    }

    @Test
    void saveCategory_withEmptyName_handlesCorrectly() {
        Category c = new Category("");
        Category saved = categoryRepo.save(c);
        assertThat(saved.getCategoryId()).isNotNull();
        assertThat(saved.getCategoryName()).isEqualTo("");
    }

    @Test
    void saveAndFindCategory_withWhitespace_handlesCorrectly() {
        Category c = new Category("  Category with spaces  ");
        Category saved = categoryRepo.save(c);
        Category found = categoryRepo.findCategoryByCategoryId(saved.getCategoryId());
        assertThat(found).isNotNull();
        assertThat(found.getCategoryName()).isEqualTo("  Category with spaces  ");
    }
}
