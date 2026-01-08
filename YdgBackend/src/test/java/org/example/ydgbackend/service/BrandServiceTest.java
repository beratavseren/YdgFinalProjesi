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

        List<BrandCategory> existing = new ArrayList<>();
        existing.add(new BrandCategory(100L, brand, new Category(1L, "A")));
        existing.add(new BrandCategory(101L, brand, new Category(2L, "B")));
        when(brandCategoryRepo.findBrandCategoriesByBrand(brand)).thenReturn(existing);

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

    @Test
    void addBrand_withMultipleCategories_createsAllRelations() {
        AddBrandDto dto = new AddBrandDto();
        dto.setBrandName("Samsung");
        dto.setCategoryDtos(Arrays.asList(
                new CategoryDto(1L, "Electronics"),
                new CategoryDto(2L, "Mobile"),
                new CategoryDto(3L, "Appliances")
        ));

        Brand saved = new Brand(20L, "Samsung");
        when(brandRepo.save(any(Brand.class))).thenReturn(saved);
        when(categoryRepo.findCategoryByCategoryId(1L)).thenReturn(new Category(1L, "Electronics"));
        when(categoryRepo.findCategoryByCategoryId(2L)).thenReturn(new Category(2L, "Mobile"));
        when(categoryRepo.findCategoryByCategoryId(3L)).thenReturn(new Category(3L, "Appliances"));

        boolean ok = brandService.addBrand(dto);

        assertThat(ok).isTrue();
        verify(brandRepo).save(any(Brand.class));
        verify(brandCategoryRepo, times(3)).save(any(BrandCategory.class));
    }

    @Test
    void addBrand_withEmptyCategories_createsBrandOnly() {
        AddBrandDto dto = new AddBrandDto();
        dto.setBrandName("Generic");
        dto.setCategoryDtos(Collections.emptyList());

        Brand saved = new Brand(21L, "Generic");
        when(brandRepo.save(any(Brand.class))).thenReturn(saved);

        boolean ok = brandService.addBrand(dto);

        assertThat(ok).isTrue();
        verify(brandRepo).save(any(Brand.class));
        verify(brandCategoryRepo, never()).save(any(BrandCategory.class));
    }

    @Test
    void addBrand_handlesException_returnsFalse() {
        AddBrandDto dto = new AddBrandDto();
        dto.setBrandName("Failing");
        dto.setCategoryDtos(Arrays.asList(new CategoryDto(1L, "Cat")));

        when(brandRepo.save(any(Brand.class))).thenThrow(new jakarta.transaction.TransactionalException("DB error", new RuntimeException("DB error")));

        boolean ok = brandService.addBrand(dto);

        assertThat(ok).isFalse();
    }

    @Test
    void updateBrand_withComplexCategoryChanges_handlesCorrectly() {
        Brand brand = new Brand(10L, "Original");
        when(brandRepo.findByBrandId(10L)).thenReturn(brand);

        BrandCategory bc1 = new BrandCategory(1L, brand, new Category(1L, "Cat1"));
        BrandCategory bc2 = new BrandCategory(2L, brand, new Category(2L, "Cat2"));
        BrandCategory bc3 = new BrandCategory(3L, brand, new Category(3L, "Cat3"));
        List<BrandCategory> existing = new ArrayList<>();
        existing.add(bc1);
        existing.add(bc2);
        existing.add(bc3);
        when(brandCategoryRepo.findBrandCategoriesByBrand(brand)).thenReturn(existing);

        UpdateBrandDto dto = new UpdateBrandDto();
        dto.setBrandId(10L);
        dto.setBrandName("Updated Complex");
        dto.setUpdatedCategoryList(Collections.singletonList(new CategoryDto(1L, "Cat1")));
        dto.setAddedCategoryList(Arrays.asList(
                new CategoryDto(4L, "Cat4"),
                new CategoryDto(5L, "Cat5")
        ));

        when(categoryRepo.findCategoryByCategoryId(4L)).thenReturn(new Category(4L, "Cat4"));
        when(categoryRepo.findCategoryByCategoryId(5L)).thenReturn(new Category(5L, "Cat5"));

        boolean ok = brandService.updateBrand(dto);

        assertThat(ok).isTrue();
        verify(brandCategoryRepo, atLeast(1)).delete(any(BrandCategory.class));
        verify(brandCategoryRepo, times(2)).save(any(BrandCategory.class));
        verify(brandRepo).save(brand);
        assertThat(brand.getBrandName()).isEqualTo("Updated Complex");
    }

    @Test
    void updateBrand_withNoCategoryChanges_onlyUpdatesName() {
        Brand brand = new Brand(11L, "Old Name");
        when(brandRepo.findByBrandId(11L)).thenReturn(brand);

        BrandCategory bc1 = new BrandCategory(1L, brand, new Category(1L, "Cat1"));
        List<BrandCategory> existing = new ArrayList<>();
        existing.add(bc1);
        when(brandCategoryRepo.findBrandCategoriesByBrand(brand)).thenReturn(existing);

        UpdateBrandDto dto = new UpdateBrandDto();
        dto.setBrandId(11L);
        dto.setBrandName("New Name");
        dto.setUpdatedCategoryList(Collections.singletonList(new CategoryDto(1L, "Cat1")));
        dto.setAddedCategoryList(Collections.emptyList());

        boolean ok = brandService.updateBrand(dto);

        assertThat(ok).isTrue();
        verify(brandCategoryRepo, never()).delete(any(BrandCategory.class));
        verify(brandCategoryRepo, never()).save(any(BrandCategory.class));
        verify(brandRepo).save(brand);
        assertThat(brand.getBrandName()).isEqualTo("New Name");
    }

    @Test
    void updateBrand_handlesException_returnsFalse() {
        when(brandRepo.findByBrandId(12L)).thenThrow(new RuntimeException("DB error"));

        UpdateBrandDto dto = new UpdateBrandDto();
        dto.setBrandId(12L);
        dto.setBrandName("Fail");

        boolean ok = brandService.updateBrand(dto);

        assertThat(ok).isFalse();
    }

    @Test
    void deleteBrand_withMultipleProducts_returnsFalse() {
        Brand brand = new Brand(13L, "Popular");
        when(brandRepo.findByBrandId(13L)).thenReturn(brand);
        when(productRepo.findProductsByBrand(brand)).thenReturn(Arrays.asList(
                new Product(), new Product(), new Product()
        ));

        boolean ok = brandService.deleteBrand(13L);

        assertThat(ok).isFalse();
        verify(brandRepo, never()).delete(any());
        verify(brandCategoryRepo, never()).deleteBrandCategoriesByBrand(any());
    }

    @Test
    void deleteBrand_handlesException_returnsFalse() {
        when(brandRepo.findByBrandId(14L)).thenThrow(new RuntimeException("DB error"));

        boolean ok = brandService.deleteBrand(14L);

        assertThat(ok).isFalse();
    }

    @Test
    void getBrands_returnsEmptyList_whenNoBrands() {
        when(brandRepo.findAll()).thenReturn(Collections.emptyList());

        List<BrandDto> result = brandService.getBrands();

        assertThat(result).isEmpty();
    }

    @Test
    void getBrands_returnsMultipleBrands_correctlyMapped() {
        List<Brand> brands = Arrays.asList(
                new Brand(1L, "Brand1"),
                new Brand(2L, "Brand2"),
                new Brand(3L, "Brand3")
        );
        when(brandRepo.findAll()).thenReturn(brands);

        List<BrandDto> result = brandService.getBrands();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getBrandId()).isEqualTo(1L);
        assertThat(result.get(0).getBrandName()).isEqualTo("Brand1");
        assertThat(result.get(1).getBrandId()).isEqualTo(2L);
        assertThat(result.get(1).getBrandName()).isEqualTo("Brand2");
        assertThat(result.get(2).getBrandId()).isEqualTo(3L);
        assertThat(result.get(2).getBrandName()).isEqualTo("Brand3");
    }

    @Test
    void getBrands_handlesLargeDataset() {
        List<Brand> brands = new ArrayList<>();
        for (long i = 1; i <= 100; i++) {
            brands.add(new Brand(i, "Brand" + i));
        }
        when(brandRepo.findAll()).thenReturn(brands);

        List<BrandDto> result = brandService.getBrands();

        assertThat(result).hasSize(100);
        assertThat(result.get(0).getBrandId()).isEqualTo(1L);
        assertThat(result.get(99).getBrandId()).isEqualTo(100L);
    }
}
