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

import java.util.Arrays;
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
}

