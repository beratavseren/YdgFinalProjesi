package org.example.ydgbackend.it;

import jakarta.transaction.Transactional;
import org.example.ydgbackend.Entity.Brand;
import org.example.ydgbackend.Entity.BrandCategory;
import org.example.ydgbackend.Entity.Category;
import org.example.ydgbackend.Repository.BrandCategoryRepo;
import org.example.ydgbackend.Repository.BrandRepo;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "DOCKER_AVAILABLE", matches = "true")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class BrandCategoryRepositoryIT {

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

        // Eğer hibernate dialect hatası alırsanız bunu da ekleyebilirsiniz:
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Autowired
    BrandRepo brandRepo;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    BrandCategoryRepo brandCategoryRepo;

    @Test
    void save_andQueryRelations_thenDeleteByBrand() {
        Brand nike = brandRepo.save(new Brand("Nike"));
        Category shoes = categoryRepo.save(new Category("Shoes"));
        Category apparel = categoryRepo.save(new Category("Apparel"));

        brandCategoryRepo.save(new BrandCategory(nike, shoes));
        brandCategoryRepo.save(new BrandCategory(nike, apparel));

        // Query by brand
        List<BrandCategory> byBrand = brandCategoryRepo.findBrandCategoriesByBrand(nike);
        assertThat(byBrand).hasSize(2);
        assertThat(byBrand).extracting(bc -> bc.getCategory().getCategoryName())
                .containsExactlyInAnyOrder("Shoes", "Apparel");

        // Query by category
        List<BrandCategory> byCategory = brandCategoryRepo.findBrandCategoriesByCategory(shoes);
        assertThat(byCategory).hasSize(1);
        assertThat(byCategory.get(0).getBrand().getBrandName()).isEqualTo("Nike");

        // Delete by brand and verify
        brandCategoryRepo.deleteBrandCategoriesByBrand(nike);
        assertThat(brandCategoryRepo.findBrandCategoriesByBrand(nike)).isEmpty();
    }
}
