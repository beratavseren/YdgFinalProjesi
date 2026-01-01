package org.example.ydgbackend.controller;

import org.example.ydgbackend.Controller.ProductController;
import org.example.ydgbackend.Dto.Product.DetailedProductDto;
import org.example.ydgbackend.Dto.Product.ProductDto;
import org.example.ydgbackend.Service.ProductService;
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

class ProductControllerWebMvcTest {

    MockMvc mockMvc;

    @Mock
    ProductService productService;

    @InjectMocks
    ProductController productController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void addProduct_returnsTrue() throws Exception {
        when(productService.addProduct(any())).thenReturn(true);

        String body = "{\n" +
                "  \"productName\": \"Phone\",\n" +
                "  \"barcodeNumber\": 123456789,\n" +
                "  \"weigth\": 200,\n" +
                "  \"volume\": 500,\n" +
                "  \"brandDto\": { \"brandId\": 1, \"brandName\": \"Brand\" }\n" +
                "}";

        mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteProduct_returnsTrue() throws Exception {
        when(productService.deleteProduct(10L)).thenReturn(true);

        mockMvc.perform(delete("/product/delete/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getProducts_returnsList() throws Exception {
        List<ProductDto> list = Arrays.asList(new ProductDto(1L, "A"), new ProductDto(2L, "B") );
        when(productService.getProducts()).thenReturn(list);

        mockMvc.perform(get("/product/getProducts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].productName").value("A"))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].productName").value("B"));
    }

    @Test
    void getDetailedProduct_returnsDto() throws Exception {
        when(productService.getDetailedProduct(3L)).thenReturn(new DetailedProductDto());

        mockMvc.perform(get("/product/getProduct/3"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductByBarcode_returnsDto() throws Exception {
        when(productService.getDetailedProductByBarcodeNumber(999L)).thenReturn(new DetailedProductDto());

        mockMvc.perform(get("/product/getProductByBarcodeNumber/999"))
                .andExpect(status().isOk());
    }
}
