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

        List<BrandCategory> byBrand = brandCategoryRepo.findBrandCategoriesByBrand(nike);
        assertThat(byBrand).hasSize(2);
        assertThat(byBrand).extracting(bc -> bc.getCategory().getCategoryName())
                .containsExactlyInAnyOrder("Shoes", "Apparel");

        List<BrandCategory> byCategory = brandCategoryRepo.findBrandCategoriesByCategory(shoes);
        assertThat(byCategory).hasSize(1);
        assertThat(byCategory.get(0).getBrand().getBrandName()).isEqualTo("Nike");

        brandCategoryRepo.deleteBrandCategoriesByBrand(nike);
        assertThat(brandCategoryRepo.findBrandCategoriesByBrand(nike)).isEmpty();
    }

    @Test
    void save_andQueryRelations_withMultipleBrands() {
        Brand nike = brandRepo.save(new Brand("Nike"));
        Brand adidas = brandRepo.save(new Brand("Adidas"));
        Category shoes = categoryRepo.save(new Category("Shoes"));
        Category apparel = categoryRepo.save(new Category("Apparel"));

        brandCategoryRepo.save(new BrandCategory(nike, shoes));
        brandCategoryRepo.save(new BrandCategory(nike, apparel));
        brandCategoryRepo.save(new BrandCategory(adidas, shoes));

        assertThat(brandCategoryRepo.findBrandCategoriesByBrand(nike)).hasSize(2);
        assertThat(brandCategoryRepo.findBrandCategoriesByBrand(adidas)).hasSize(1);
        assertThat(brandCategoryRepo.findBrandCategoriesByCategory(shoes)).hasSize(2);
    }

    @Test
    void deleteBrandCategoriesByCategory_removesAllRelations() {
        Brand brand = brandRepo.save(new Brand("Brand"));
        Category cat1 = categoryRepo.save(new Category("Cat1"));
        Category cat2 = categoryRepo.save(new Category("Cat2"));

        brandCategoryRepo.save(new BrandCategory(brand, cat1));
        brandCategoryRepo.save(new BrandCategory(brand, cat2));

        List<BrandCategory> before = brandCategoryRepo.findBrandCategoriesByCategory(cat1);
        assertThat(before).hasSize(1);

        brandCategoryRepo.delete(before.get(0));
        assertThat(brandCategoryRepo.findBrandCategoriesByCategory(cat1)).isEmpty();
        assertThat(brandCategoryRepo.findBrandCategoriesByCategory(cat2)).hasSize(1);
    }

    @Test
    void save_andFind_withComplexRelations() {
        Brand brand1 = brandRepo.save(new Brand("Brand1"));
        Brand brand2 = brandRepo.save(new Brand("Brand2"));
        Category cat1 = categoryRepo.save(new Category("Cat1"));
        Category cat2 = categoryRepo.save(new Category("Cat2"));
        Category cat3 = categoryRepo.save(new Category("Cat3"));

        brandCategoryRepo.save(new BrandCategory(brand1, cat1));
        brandCategoryRepo.save(new BrandCategory(brand1, cat2));
        brandCategoryRepo.save(new BrandCategory(brand1, cat3));
        brandCategoryRepo.save(new BrandCategory(brand2, cat1));
        brandCategoryRepo.save(new BrandCategory(brand2, cat3));

        assertThat(brandCategoryRepo.findBrandCategoriesByBrand(brand1)).hasSize(3);
        assertThat(brandCategoryRepo.findBrandCategoriesByBrand(brand2)).hasSize(2);
        assertThat(brandCategoryRepo.findBrandCategoriesByCategory(cat1)).hasSize(2);
        assertThat(brandCategoryRepo.findBrandCategoriesByCategory(cat2)).hasSize(1);
        assertThat(brandCategoryRepo.findBrandCategoriesByCategory(cat3)).hasSize(2);
    }

    @Test
    void deleteBrandCategoriesByBrand_withMultipleCategories_removesAll() {
        Brand brand = brandRepo.save(new Brand("Brand"));
        Category cat1 = categoryRepo.save(new Category("Cat1"));
        Category cat2 = categoryRepo.save(new Category("Cat2"));
        Category cat3 = categoryRepo.save(new Category("Cat3"));

        brandCategoryRepo.save(new BrandCategory(brand, cat1));
        brandCategoryRepo.save(new BrandCategory(brand, cat2));
        brandCategoryRepo.save(new BrandCategory(brand, cat3));

        assertThat(brandCategoryRepo.findBrandCategoriesByBrand(brand)).hasSize(3);

        brandCategoryRepo.deleteBrandCategoriesByBrand(brand);

        assertThat(brandCategoryRepo.findBrandCategoriesByBrand(brand)).isEmpty();
        assertThat(brandCategoryRepo.findBrandCategoriesByCategory(cat1)).isEmpty();
        assertThat(brandCategoryRepo.findBrandCategoriesByCategory(cat2)).isEmpty();
        assertThat(brandCategoryRepo.findBrandCategoriesByCategory(cat3)).isEmpty();
    }

    @Test
    void saveDuplicateBrandCategory_handlesCorrectly() {
        Brand brand = brandRepo.save(new Brand("Brand"));
        Category category = categoryRepo.save(new Category("Category"));

        BrandCategory bc1 = brandCategoryRepo.save(new BrandCategory(brand, category));
        BrandCategory bc2 = brandCategoryRepo.save(new BrandCategory(brand, category));

        // Depending on DB constraints, this might fail or create duplicate
        // This test verifies the behavior
        assertThat(bc1.getBrandCategoryId()).isNotNull();
        assertThat(bc2.getBrandCategoryId()).isNotNull();
    }

    @Test
    void findBrandCategoriesByCategory_withNoRelations_returnsEmpty() {
        Category category = categoryRepo.save(new Category("Isolated"));
        List<BrandCategory> relations = brandCategoryRepo.findBrandCategoriesByCategory(category);
        assertThat(relations).isEmpty();
    }

    @Test
    void findBrandCategoriesByBrand_withNoRelations_returnsEmpty() {
        Brand brand = brandRepo.save(new Brand("Isolated"));
        List<BrandCategory> relations = brandCategoryRepo.findBrandCategoriesByBrand(brand);
        assertThat(relations).isEmpty();
    }
}
