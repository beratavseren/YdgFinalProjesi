package org.example.ydgbackend.service;

import org.example.ydgbackend.Dto.Product.DetailedProductDto;
import org.example.ydgbackend.Dto.Product.ProductDto;
import org.example.ydgbackend.Dto.Product.WerehouseQuantityDtoForDetailedProductDto;
import org.example.ydgbackend.Entity.Brand;
import org.example.ydgbackend.Entity.Product;
import org.example.ydgbackend.Entity.ProductWerehouse;
import org.example.ydgbackend.Entity.Werehouse;
import org.example.ydgbackend.Repository.BrandRepo;
import org.example.ydgbackend.Repository.ProductRepo;
import org.example.ydgbackend.Repository.ProductWerehouseRepo;
import org.example.ydgbackend.Repository.WerehouseRepo;
import org.example.ydgbackend.Service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock ProductRepo productRepo;
    @Mock WerehouseRepo werehouseRepo;
    @Mock ProductWerehouseRepo productWerehouseRepo;
    @Mock BrandRepo brandRepo;

    @InjectMocks ProductService productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProduct_createsProductAndWerehouseEntries_returnsTrue() {
        Brand brand = new Brand(1L, "Brand");
        when(brandRepo.findByBrandId(1L)).thenReturn(brand);

        // werehouses present
        Werehouse w1 = new Werehouse(); w1.setWerehouseId(10L); w1.setWerehouseName("W1");
        Werehouse w2 = new Werehouse(); w2.setWerehouseId(11L); w2.setWerehouseName("W2");
        when(werehouseRepo.findAll()).thenReturn(Arrays.asList(w1, w2));

        when(productRepo.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setProductId(100L);
            return p;
        });

        // Build minimal AddProductDto JSON via map-like stub isn't needed; call through reflection not possible.
        // Instead, create a simple DTO object using anonymous class to satisfy fields via setters in real DTO.
        org.example.ydgbackend.Dto.Product.AddProductDto dto = new org.example.ydgbackend.Dto.Product.AddProductDto();
        org.example.ydgbackend.Dto.Brand.BrandDto brandDto = new org.example.ydgbackend.Dto.Brand.BrandDto(1L, "Brand");
        dto.setBrandDto(brandDto);
        dto.setProductName("Phone");
        dto.setBarcodeNumber(123L);
        dto.setWeigth(200);
        dto.setVolume(500);

        boolean ok = productService.addProduct(dto);

        assertThat(ok).isTrue();
        verify(productRepo).save(any(Product.class));
        verify(productWerehouseRepo, times(2)).save(any(ProductWerehouse.class));
    }

    @Test
    void deleteProduct_throws_whenAnyWarehouseHasQuantity() {
        Product product = new Product(); product.setProductId(5L);
        when(productRepo.findByProductId(5L)).thenReturn(product);
        ProductWerehouse pw = new ProductWerehouse();
        pw.setProduct(product);
        pw.setQuantity(3);
        when(productWerehouseRepo.findProductWerehousesByProduct(product)).thenReturn(Collections.singletonList(pw));

        assertThatThrownBy(() -> productService.deleteProduct(5L))
                .isInstanceOf(RuntimeException.class);
        verify(productRepo, never()).deleteById(anyLong());
    }

    @Test
    void deleteProduct_success_whenAllQuantitiesZero() {
        Product product = new Product(); product.setProductId(6L);
        when(productRepo.findByProductId(6L)).thenReturn(product);
        ProductWerehouse pw1 = new ProductWerehouse(); pw1.setProduct(product); pw1.setQuantity(0);
        ProductWerehouse pw2 = new ProductWerehouse(); pw2.setProduct(product); pw2.setQuantity(0);
        when(productWerehouseRepo.findProductWerehousesByProduct(product)).thenReturn(Arrays.asList(pw1, pw2));

        boolean ok = productService.deleteProduct(6L);

        assertThat(ok).isTrue();
        verify(productWerehouseRepo, times(2)).delete(any(ProductWerehouse.class));
        verify(productRepo).deleteById(6L);
    }

    @Test
    void getProducts_mapsEntitiesToDtos() {
        Product p1 = new Product(); p1.setProductId(1L); p1.setProductName("A");
        Product p2 = new Product(); p2.setProductId(2L); p2.setProductName("B");
        when(productRepo.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<ProductDto> list = productService.getProducts();

        assertThat(list).hasSize(2);
        assertThat(list.get(0).getProductId()).isEqualTo(1L);
        assertThat(list.get(0).getProductName()).isEqualTo("A");
        assertThat(list.get(1).getProductId()).isEqualTo(2L);
        assertThat(list.get(1).getProductName()).isEqualTo("B");
    }

    @Test
    void getDetailedProduct_assemblesDto() {
        Brand brand = new Brand(9L, "Brand");
        Product p = new Product();
        p.setProductId(77L);
        p.setProductName("Item");
        p.setBarcodeNumber(999L);
        p.setComment("c");
        p.setWeight(10);
        p.setVolume(20);
        p.setBrand(brand);
        when(productRepo.findByProductId(77L)).thenReturn(p);

        Werehouse w = new Werehouse(); w.setWerehouseName("W");
        ProductWerehouse pw = new ProductWerehouse(); pw.setWerehouse(w); pw.setProduct(p); pw.setQuantity(4);
        when(productWerehouseRepo.findProductWerehousesByProduct(p)).thenReturn(Collections.singletonList(pw));

        DetailedProductDto dto = productService.getDetailedProduct(77L);

        assertThat(dto.getProductId()).isEqualTo(77L);
        assertThat(dto.getProductName()).isEqualTo("Item");
        assertThat(dto.getBarcodeNumber()).isEqualTo(999L);
        assertThat(dto.getBrandDto().getBrandId()).isEqualTo(9L);
        assertThat(dto.getWerehouseQuantityDtoForDetailedProductDtos()).extracting(WerehouseQuantityDtoForDetailedProductDto::getWerehouseName)
                .containsExactly("W");
    }
}
