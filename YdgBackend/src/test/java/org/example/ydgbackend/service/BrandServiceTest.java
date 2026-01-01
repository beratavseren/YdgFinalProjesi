package org.example.ydgbackend.service;

import org.example.ydgbackend.Dto.Brand.AddBrandDto;
import org.example.ydgbackend.Dto.Brand.BrandDto;
import org.example.ydgbackend.Dto.Brand.CategoryDto;
import org.example.ydgbackend.Dto.Brand.UpdateBrandDto;
import org.example.ydgbackend.Entity.Brand;
import org.example.ydgbackend.Entity.BrandCategory;
import org.example.ydgbackend.Entity.Category;
import org.example.ydgbackend.Entity.Product;
import org.example.ydgbackend.Repository.BrandCategoryRepo;
import org.example.ydgbackend.Repository.BrandRepo;
import org.example.ydgbackend.Repository.CategoryRepo;
import org.example.ydgbackend.Repository.ProductRepo;
import org.example.ydgbackend.Service.BrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BrandServiceTest {

    @Mock BrandRepo brandRepo;
    @Mock CategoryRepo categoryRepo;
    @Mock BrandCategoryRepo brandCategoryRepo;
    @Mock ProductRepo productRepo;

    @InjectMocks BrandService brandService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addBrand_savesBrandAndRelations_returnsTrue() {
        AddBrandDto dto = new AddBrandDto();
        dto.setBrandName("Nike");
        dto.setCategoryDtos(Arrays.asList(new CategoryDto(1L, "Shoes")));

        Brand saved = new Brand(10L, "Nike");
        when(brandRepo.save(any(Brand.class))).thenReturn(saved);
        when(categoryRepo.findCategoryByCategoryId(1L)).thenReturn(new Category(1L, "Shoes"));

        boolean ok = brandService.addBrand(dto);

        assertThat(ok).isTrue();
        verify(brandRepo).save(any(Brand.class));
        verify(brandCategoryRepo).save(any(BrandCategory.class));
    }

    @Test
    void updateBrand_updatesRelations_andName_returnsTrue() {
        Brand brand = new Brand(5L, "Old");
        when(brandRepo.findByBrandId(5L)).thenReturn(brand);

        // Existing relations
        List<BrandCategory> existing = new ArrayList<>();
        existing.add(new BrandCategory(100L, brand, new Category(1L, "A")));
        existing.add(new BrandCategory(101L, brand, new Category(2L, "B")));
        when(brandCategoryRepo.findBrandCategoriesByBrand(brand)).thenReturn(existing);

        // Update keeps category 1, removes category 2, adds category 3
        UpdateBrandDto dto = new UpdateBrandDto();
        dto.setBrandId(5L);
        dto.setBrandName("New");
        dto.setUpdatedCategoryList(Collections.singletonList(new CategoryDto(1L, "A")));
        dto.setAddedCategoryList(Collections.singletonList(new CategoryDto(3L, "C")));

        when(categoryRepo.findCategoryByCategoryId(3L)).thenReturn(new Category(3L, "C"));

        boolean ok = brandService.updateBrand(dto);

        assertThat(ok).isTrue();
        verify(brandCategoryRepo, atLeastOnce()).delete(any(BrandCategory.class));
        verify(brandCategoryRepo).save(any(BrandCategory.class));
        verify(brandRepo).save(brand);
        assertThat(brand.getBrandName()).isEqualTo("New");
    }

    @Test
    void deleteBrand_returnsTrue_whenNoProducts() {
        Brand brand = new Brand(7L, "X");
        when(brandRepo.findByBrandId(7L)).thenReturn(brand);
        when(productRepo.findProductsByBrand(brand)).thenReturn(Collections.emptyList());

        boolean ok = brandService.deleteBrand(7L);

        assertThat(ok).isTrue();
        verify(brandCategoryRepo).deleteBrandCategoriesByBrand(brand);
        verify(brandRepo).delete(brand);
    }

    @Test
    void deleteBrand_returnsFalse_whenProductsExist() {
        Brand brand = new Brand(8L, "Y");
        when(brandRepo.findByBrandId(8L)).thenReturn(brand);
        when(productRepo.findProductsByBrand(brand)).thenReturn(Collections.singletonList(new Product()));

        boolean ok = brandService.deleteBrand(8L);

        assertThat(ok).isFalse();
        verify(brandRepo, never()).delete(any());
    }

    @Test
    void getBrand_returnsDto_whenPresent() {
        when(brandRepo.findById(3L)).thenReturn(Optional.of(new Brand(3L, "Adidas")));

        BrandDto dto = brandService.getBrand(3L);

        assertThat(dto.getBrandId()).isEqualTo(3L);
        assertThat(dto.getBrandName()).isEqualTo("Adidas");
    }

    @Test
    void getBrand_throws_whenNotFound() {
        when(brandRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> brandService.getBrand(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Brand not found");
    }
}
