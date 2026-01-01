package org.example.ydgbackend.service;

import org.example.ydgbackend.Dto.Brand.CategoryDto;
import org.example.ydgbackend.Dto.Category.AddCategoryDto;
import org.example.ydgbackend.Entity.BrandCategory;
import org.example.ydgbackend.Entity.Category;
import org.example.ydgbackend.Repository.BrandCategoryRepo;
import org.example.ydgbackend.Repository.CategoryRepo;
import org.example.ydgbackend.Service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    CategoryRepo categoryRepo;
    @Mock
    BrandCategoryRepo brandCategoryRepo;

    @InjectMocks
    CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCategory_shouldReturnTrue_onSuccess() {
        AddCategoryDto dto = new AddCategoryDto();
        dto.setCategoryName("Electronics");
        when(categoryRepo.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        boolean result = categoryService.addCategory(dto);

        assertThat(result).isTrue();
        verify(categoryRepo).save(any(Category.class));
    }

    @Test
    void getCategory_shouldMapEntityToDto() {
        Category category = new Category(1L, "Home");
        when(categoryRepo.findCategoryByCategoryId(1L)).thenReturn(category);

        CategoryDto dto = categoryService.getCategory(1L);

        assertThat(dto.getCategoryId()).isEqualTo(1L);
        assertThat(dto.getCategoryName()).isEqualTo("Home");
    }

    @Test
    void deleteCategory_shouldDelete_whenNoBrandCategories() {
        Category category = new Category(2L, "Books");
        when(categoryRepo.findCategoryByCategoryId(2L)).thenReturn(category);
        when(brandCategoryRepo.findBrandCategoriesByCategory(category)).thenReturn(Collections.emptyList());

        boolean result = categoryService.deleteCategory(2L);

        assertThat(result).isTrue();
        verify(categoryRepo).delete(category);
    }

    @Test
    void updateCategory_shouldUpdateName_andReturnTrue() {
        Category existing = new Category(10L, "Old");
        when(categoryRepo.findCategoryByCategoryId(10L)).thenReturn(existing);

        org.example.ydgbackend.Dto.Category.UpdateCategoryDto dto = new org.example.ydgbackend.Dto.Category.UpdateCategoryDto();
        dto.setCategoryId(10L);
        dto.setCategoryName("New");

        when(categoryRepo.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        boolean ok = categoryService.updateCategory(dto);

        assertThat(ok).isTrue();
        assertThat(existing.getCategoryName()).isEqualTo("New");
        verify(categoryRepo).save(existing);
    }

    @Test
    void deleteCategory_shouldReturnFalse_whenBrandCategoriesExist() {
        Category category = new Category(3L, "Toys");
        when(categoryRepo.findCategoryByCategoryId(3L)).thenReturn(category);
        when(brandCategoryRepo.findBrandCategoriesByCategory(category))
                .thenReturn(Collections.singletonList(new BrandCategory()));

        boolean result = categoryService.deleteCategory(3L);

        assertThat(result).isFalse();
        verify(categoryRepo, never()).delete(any());
    }

    @Test
    void getCategory_shouldReturnEmptyDto_onException() {
        when(categoryRepo.findCategoryByCategoryId(99L)).thenThrow(new RuntimeException("DB down"));

        CategoryDto dto = categoryService.getCategory(99L);

        assertThat(dto.getCategoryId()).isNull();
        assertThat(dto.getCategoryName()).isNull();
    }

    @Test
    void getCategories_shouldMapAllEntitiesToDtos() {
        List<Category> entities = Arrays.asList(
                new Category(1L, "A"),
                new Category(2L, "B")
        );
        when(categoryRepo.findAll()).thenReturn(entities);

        List<CategoryDto> result = categoryService.getCategories();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategoryId()).isEqualTo(1L);
        assertThat(result.get(0).getCategoryName()).isEqualTo("A");
        assertThat(result.get(1).getCategoryId()).isEqualTo(2L);
        assertThat(result.get(1).getCategoryName()).isEqualTo("B");
    }

    @Test
    void getCategories_shouldReturnEmptyList_onException() {
        when(categoryRepo.findAll()).thenThrow(new RuntimeException("oops"));

        List<CategoryDto> result = categoryService.getCategories();

        assertThat(result).isEmpty();
    }
}
