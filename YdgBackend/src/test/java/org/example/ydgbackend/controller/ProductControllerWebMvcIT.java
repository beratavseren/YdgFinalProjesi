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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerWebMvcIT {

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

    @Test
    void addProduct_whenServiceReturnsFalse_returnsFalse() throws Exception {
        when(productService.addProduct(any())).thenReturn(false);

        String body = "{\n" +
                "  \"productName\": \"Fail\",\n" +
                "  \"barcodeNumber\": 123,\n" +
                "  \"weigth\": 100,\n" +
                "  \"volume\": 200,\n" +
                "  \"brandDto\": { \"brandId\": 1, \"brandName\": \"Brand\" }\n" +
                "}";

        mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void getProducts_withEmptyList_returnsEmptyArray() throws Exception {
        when(productService.getProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/product/getProducts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getProducts_withLargeList_returnsAll() throws Exception {
        List<ProductDto> products = new ArrayList<>();
        for (long i = 1; i <= 100; i++) {
            products.add(new ProductDto(i, "Product" + i));
        }
        when(productService.getProducts()).thenReturn(products);

        mockMvc.perform(get("/product/getProducts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(100))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[99].productId").value(100));
    }

    @Test
    void getDetailedProduct_withFullDetails_returnsCompleteDto() throws Exception {
        DetailedProductDto dto = new DetailedProductDto();
        dto.setProductId(50L);
        dto.setProductName("Detailed Product");
        dto.setBarcodeNumber(123456L);
        when(productService.getDetailedProduct(50L)).thenReturn(dto);

        mockMvc.perform(get("/product/getProduct/50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(50))
                .andExpect(jsonPath("$.productName").value("Detailed Product"))
                .andExpect(jsonPath("$.barcodeNumber").value(123456));
    }


    @Test
    void addProduct_withInvalidJson_returns400() throws Exception {
        String invalidBody = "{\n  \"productName\": invalid\n}";

        mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addProduct_withMissingFields_handlesGracefully() throws Exception {
        when(productService.addProduct(any())).thenReturn(true);

        String body = "{\n" +
                "  \"productName\": \"Minimal\"\n" +
                "}";

        mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void getProductByBarcode_withLargeBarcode_handlesCorrectly() throws Exception {
        DetailedProductDto dto = new DetailedProductDto();
        dto.setBarcodeNumber(999999999999L);
        when(productService.getDetailedProductByBarcodeNumber(999999999999L)).thenReturn(dto);

        mockMvc.perform(get("/product/getProductByBarcodeNumber/999999999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barcodeNumber").value(999999999999L));
    }
}

