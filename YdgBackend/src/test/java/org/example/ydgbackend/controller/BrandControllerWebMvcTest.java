package org.example.ydgbackend.controller;

import org.example.ydgbackend.Controller.BrandController;
import org.example.ydgbackend.Dto.Brand.AddBrandDto;
import org.example.ydgbackend.Dto.Brand.BrandDto;
import org.example.ydgbackend.Dto.Brand.UpdateBrandDto;
import org.example.ydgbackend.Service.BrandService;
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

class BrandControllerWebMvcTest {

    MockMvc mockMvc;

    @Mock
    BrandService brandService;

    @InjectMocks
    BrandController brandController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(brandController).build();
    }

    @Test
    void addBrand_returnsTrue() throws Exception {
        when(brandService.addBrand(any(AddBrandDto.class))).thenReturn(true);

        String body = "{\n  \"brandName\": \"Nike\",\n  \"categoryDtos\": [{\n    \"categoryId\": 1, \n    \"categoryName\": \"Shoes\"\n  }]\n}";

        mockMvc.perform(post("/brand/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void updateBrand_returnsTrue() throws Exception {
        when(brandService.updateBrand(any(UpdateBrandDto.class))).thenReturn(true);

        String body = "{\n  \"brandId\": 5,\n  \"brandName\": \"Updated\",\n  \"updatedCategoryList\": [],\n  \"addedCategoryList\": []\n}";

        mockMvc.perform(put("/brand/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteBrand_returnsTrue() throws Exception {
        when(brandService.deleteBrand(7L)).thenReturn(true);

        mockMvc.perform(delete("/brand/delete/7"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getBrand_returnsDto() throws Exception {
        when(brandService.getBrand(3L)).thenReturn(new BrandDto(3L, "Adidas"));

        mockMvc.perform(get("/brand/getBrand/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brandId").value(3))
                .andExpect(jsonPath("$.brandName").value("Adidas"));
    }

    @Test
    void getBrands_returnsList() throws Exception {
        List<BrandDto> list = Arrays.asList(new BrandDto(1L, "A"), new BrandDto(2L, "B"));
        when(brandService.getBrands()).thenReturn(list);

        mockMvc.perform(get("/brand/getBrands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].brandName").value("A"))
                .andExpect(jsonPath("$[1].brandId").value(2))
                .andExpect(jsonPath("$[1].brandName").value("B"));
    }

    @Test
    void addBrand_whenServiceReturnsFalse_returnsFalse() throws Exception {
        when(brandService.addBrand(any(AddBrandDto.class))).thenReturn(false);

        String body = "{\n  \"brandName\": \"X\",\n  \"categoryDtos\": []\n}";

        mockMvc.perform(post("/brand/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
