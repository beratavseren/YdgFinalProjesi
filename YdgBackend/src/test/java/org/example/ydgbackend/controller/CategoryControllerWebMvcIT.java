package org.example.ydgbackend.controller;

import org.example.ydgbackend.Controller.CategoryController;
import org.example.ydgbackend.Dto.Brand.CategoryDto;
import org.example.ydgbackend.Dto.Category.AddCategoryDto;
import org.example.ydgbackend.Dto.Category.UpdateCategoryDto;
import org.example.ydgbackend.Service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerWebMvcIT {

    MockMvc mockMvc;

    @Mock
    CategoryService categoryService;

    @InjectMocks
    CategoryController categoryController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void addCategory_returnsTrue() throws Exception {
        when(categoryService.addCategory(any(AddCategoryDto.class))).thenReturn(true);

        String body = "{\n  \"categoryName\": \"NewCat\"\n}";

        mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void updateCategory_returnsTrue() throws Exception {
        when(categoryService.updateCategory(any(UpdateCategoryDto.class))).thenReturn(true);

        String body = "{\n  \"categoryId\": 5,\n  \"categoryName\": \"Updated\"\n}";

        mockMvc.perform(put("/category/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteCategory_returnsTrue() throws Exception {
        when(categoryService.deleteCategory(7L)).thenReturn(true);

        mockMvc.perform(delete("/category/delete/7"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getCategory_returnsDto() throws Exception {
        when(categoryService.getCategory(3L)).thenReturn(new CategoryDto(3L, "Home"));

        mockMvc.perform(get("/category/getCategory/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(3))
                .andExpect(jsonPath("$.categoryName").value("Home"));
    }

    @Test
    void getCategories_returnsList() throws Exception {
        List<CategoryDto> list = Arrays.asList(new CategoryDto(1L, "A"), new CategoryDto(2L, "B"));
        when(categoryService.getCategories()).thenReturn(list);

        mockMvc.perform(get("/category/getCategories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryId").value(1))
                .andExpect(jsonPath("$[0].categoryName").value("A"))
                .andExpect(jsonPath("$[1].categoryId").value(2))
                .andExpect(jsonPath("$[1].categoryName").value("B"));
    }

    @Test
    void addCategory_whenServiceReturnsFalse_returnsFalse() throws Exception {
        when(categoryService.addCategory(any(AddCategoryDto.class))).thenReturn(false);

        String body = "{\n  \"categoryName\": \"Fail\"\n}";

        mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void updateCategory_whenServiceReturnsFalse_returnsFalse() throws Exception {
        when(categoryService.updateCategory(any(UpdateCategoryDto.class))).thenReturn(false);

        String body = "{\n  \"categoryId\": 99,\n  \"categoryName\": \"Fail\"\n}";

        mockMvc.perform(put("/category/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void deleteCategory_whenServiceReturnsFalse_returnsFalse() throws Exception {
        when(categoryService.deleteCategory(99L)).thenReturn(false);

        mockMvc.perform(delete("/category/delete/99"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void getCategory_withInvalidId_handlesException() throws Exception {
        when(categoryService.getCategory(999L)).thenReturn(new CategoryDto(null, null));

        mockMvc.perform(get("/category/getCategory/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").isEmpty())
                .andExpect(jsonPath("$.categoryName").isEmpty());
    }

    @Test
    void getCategories_withEmptyList_returnsEmptyArray() throws Exception {
        when(categoryService.getCategories()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/category/getCategories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getCategories_withLargeList_returnsAll() throws Exception {
        List<CategoryDto> categories = new ArrayList<>();
        for (long i = 1; i <= 30; i++) {
            categories.add(new CategoryDto(i, "Category" + i));
        }
        when(categoryService.getCategories()).thenReturn(categories);

        mockMvc.perform(get("/category/getCategories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(30))
                .andExpect(jsonPath("$[0].categoryId").value(1))
                .andExpect(jsonPath("$[29].categoryId").value(30));
    }

    @Test
    void addCategory_withSpecialCharacters_handlesCorrectly() throws Exception {
        when(categoryService.addCategory(any(AddCategoryDto.class))).thenReturn(true);

        String body = "{\n  \"categoryName\": \"Electronics & Appliances\"\n}";

        mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void addCategory_withUnicodeCharacters_handlesCorrectly() throws Exception {
        when(categoryService.addCategory(any(AddCategoryDto.class))).thenReturn(true);

        String body = "{\n  \"categoryName\": \"カテゴリー\"\n}";

        mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void updateCategory_withLongName_handlesCorrectly() throws Exception {
        when(categoryService.updateCategory(any(UpdateCategoryDto.class))).thenReturn(true);

        String longName = "A".repeat(100);
        String body = "{\n  \"categoryId\": 10,\n  \"categoryName\": \"" + longName + "\"\n}";

        mockMvc.perform(put("/category/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void addCategory_withInvalidJson_returns400() throws Exception {
        String invalidBody = "{\n  \"categoryName\": invalid\n}";

        mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }
}

