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

import java.util.ArrayList;
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

    @Test
    void addProduct_withNoWarehouses_createsProductOnly() {
        Brand brand = new Brand(2L, "Brand");
        when(brandRepo.findByBrandId(2L)).thenReturn(brand);
        when(werehouseRepo.findAll()).thenReturn(Collections.emptyList());
        when(productRepo.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setProductId(200L);
            return p;
        });

        org.example.ydgbackend.Dto.Product.AddProductDto dto = new org.example.ydgbackend.Dto.Product.AddProductDto();
        dto.setBrandDto(new org.example.ydgbackend.Dto.Brand.BrandDto(2L, "Brand"));
        dto.setProductName("Laptop");
        dto.setBarcodeNumber(456L);
        dto.setWeigth(3000);
        dto.setVolume(10000);

        boolean ok = productService.addProduct(dto);

        assertThat(ok).isTrue();
        verify(productRepo).save(any(Product.class));
        verify(productWerehouseRepo, never()).save(any(ProductWerehouse.class));
    }

    @Test
    void addProduct_handlesException_throwsRuntimeException() {
        when(brandRepo.findByBrandId(3L)).thenThrow(new RuntimeException("Brand not found"));

        org.example.ydgbackend.Dto.Product.AddProductDto dto = new org.example.ydgbackend.Dto.Product.AddProductDto();
        dto.setBrandDto(new org.example.ydgbackend.Dto.Brand.BrandDto(3L, "Missing"));
        dto.setProductName("Product");

        assertThatThrownBy(() -> productService.addProduct(dto))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void deleteProduct_withMultipleWarehouses_allQuantitiesZero_success() {
        Product product = new Product();
        product.setProductId(7L);
        when(productRepo.findByProductId(7L)).thenReturn(product);

        ProductWerehouse pw1 = new ProductWerehouse();
        pw1.setProduct(product);
        pw1.setQuantity(0);
        ProductWerehouse pw2 = new ProductWerehouse();
        pw2.setProduct(product);
        pw2.setQuantity(0);
        ProductWerehouse pw3 = new ProductWerehouse();
        pw3.setProduct(product);
        pw3.setQuantity(0);

        when(productWerehouseRepo.findProductWerehousesByProduct(product))
                .thenReturn(Arrays.asList(pw1, pw2, pw3));

        boolean ok = productService.deleteProduct(7L);

        assertThat(ok).isTrue();
        verify(productWerehouseRepo, times(3)).delete(any(ProductWerehouse.class));
        verify(productRepo).deleteById(7L);
    }

    @Test
    void deleteProduct_withOneNonZeroQuantity_throwsException() {
        Product product = new Product();
        product.setProductId(8L);
        when(productRepo.findByProductId(8L)).thenReturn(product);

        ProductWerehouse pw1 = new ProductWerehouse();
        pw1.setProduct(product);
        pw1.setQuantity(0);
        ProductWerehouse pw2 = new ProductWerehouse();
        pw2.setProduct(product);
        pw2.setQuantity(5); // Non-zero!

        when(productWerehouseRepo.findProductWerehousesByProduct(product))
                .thenReturn(Arrays.asList(pw1, pw2));

        assertThatThrownBy(() -> productService.deleteProduct(8L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("quantity");
        verify(productRepo, never()).deleteById(anyLong());
    }

    @Test
    void deleteProduct_handlesException_throwsRuntimeException() {
        when(productRepo.findByProductId(9L)).thenThrow(new RuntimeException("Not found"));

        assertThatThrownBy(() -> productService.deleteProduct(9L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getProducts_withEmptyDatabase_returnsEmptyList() {
        when(productRepo.findAll()).thenReturn(Collections.emptyList());

        List<ProductDto> result = productService.getProducts();

        assertThat(result).isEmpty();
    }

    @Test
    void getProducts_withLargeDataset_handlesCorrectly() {
        List<Product> products = new ArrayList<>();
        for (long i = 1; i <= 100; i++) {
            Product p = new Product();
            p.setProductId(i);
            p.setProductName("Product" + i);
            products.add(p);
        }
        when(productRepo.findAll()).thenReturn(products);

        List<ProductDto> result = productService.getProducts();

        assertThat(result).hasSize(100);
        assertThat(result.get(0).getProductId()).isEqualTo(1L);
        assertThat(result.get(99).getProductId()).isEqualTo(100L);
    }

    @Test
    void getProducts_handlesException_throwsRuntimeException() {
        when(productRepo.findAll()).thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() -> productService.getProducts())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getDetailedProduct_withMultipleWarehouses_assemblesCorrectly() {
        Brand brand = new Brand(10L, "Brand");
        Product p = new Product();
        p.setProductId(100L);
        p.setProductName("Item");
        p.setBarcodeNumber(111L);
        p.setComment("Comment");
        p.setWeight(50);
        p.setVolume(100);
        p.setBrand(brand);
        when(productRepo.findByProductId(100L)).thenReturn(p);

        Werehouse w1 = new Werehouse();
        w1.setWerehouseName("Warehouse1");
        Werehouse w2 = new Werehouse();
        w2.setWerehouseName("Warehouse2");
        Werehouse w3 = new Werehouse();
        w3.setWerehouseName("Warehouse3");

        ProductWerehouse pw1 = new ProductWerehouse();
        pw1.setWerehouse(w1);
        pw1.setProduct(p);
        pw1.setQuantity(10);
        ProductWerehouse pw2 = new ProductWerehouse();
        pw2.setWerehouse(w2);
        pw2.setProduct(p);
        pw2.setQuantity(20);
        ProductWerehouse pw3 = new ProductWerehouse();
        pw3.setWerehouse(w3);
        pw3.setProduct(p);
        pw3.setQuantity(30);

        when(productWerehouseRepo.findProductWerehousesByProduct(p))
                .thenReturn(Arrays.asList(pw1, pw2, pw3));

        DetailedProductDto dto = productService.getDetailedProduct(100L);

        assertThat(dto.getProductId()).isEqualTo(100L);
        assertThat(dto.getProductName()).isEqualTo("Item");
        assertThat(dto.getBarcodeNumber()).isEqualTo(111L);
        assertThat(dto.getComment()).isEqualTo("Comment");
        assertThat(dto.getWeigth()).isEqualTo(50);
        assertThat(dto.getVolume()).isEqualTo(100);
        assertThat(dto.getWerehouseQuantityDtoForDetailedProductDtos()).hasSize(3);
        assertThat(dto.getWerehouseQuantityDtoForDetailedProductDtos())
                .extracting(WerehouseQuantityDtoForDetailedProductDto::getWerehouseName)
                .containsExactly("Warehouse1", "Warehouse2", "Warehouse3");
        assertThat(dto.getWerehouseQuantityDtoForDetailedProductDtos())
                .extracting(WerehouseQuantityDtoForDetailedProductDto::getQuantity)
                .containsExactly(10, 20, 30);
    }

    @Test
    void getDetailedProduct_withNoWarehouses_returnsEmptyWarehouseList() {
        Brand brand = new Brand(11L, "Brand");
        Product p = new Product();
        p.setProductId(101L);
        p.setProductName("Item");
        p.setBrand(brand);
        when(productRepo.findByProductId(101L)).thenReturn(p);
        when(productWerehouseRepo.findProductWerehousesByProduct(p))
                .thenReturn(Collections.emptyList());

        DetailedProductDto dto = productService.getDetailedProduct(101L);

        assertThat(dto.getWerehouseQuantityDtoForDetailedProductDtos()).isEmpty();
    }

    @Test
    void getDetailedProduct_handlesException_throwsRuntimeException() {
        when(productRepo.findByProductId(102L)).thenThrow(new RuntimeException("Not found"));

        assertThatThrownBy(() -> productService.getDetailedProduct(102L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getDetailedProductByBarcodeNumber_findsProductCorrectly() {
        Brand brand = new Brand(12L, "Brand");
        Product p = new Product();
        p.setProductId(103L);
        p.setProductName("Item");
        p.setBarcodeNumber(999999L);
        p.setBrand(brand);
        when(productRepo.findByBarcodeNumber(999999L)).thenReturn(p);

        Werehouse w = new Werehouse();
        w.setWerehouseName("W");
        ProductWerehouse pw = new ProductWerehouse();
        pw.setWerehouse(w);
        pw.setProduct(p);
        pw.setQuantity(5);
        when(productWerehouseRepo.findProductWerehousesByProduct(p))
                .thenReturn(Collections.singletonList(pw));

        DetailedProductDto dto = productService.getDetailedProductByBarcodeNumber(999999L);

        assertThat(dto.getProductId()).isEqualTo(103L);
        assertThat(dto.getBarcodeNumber()).isEqualTo(999999L);
    }

    @Test
    void getDetailedProductByBarcodeNumber_handlesException_throwsRuntimeException() {
        when(productRepo.findByBarcodeNumber(888888L)).thenThrow(new RuntimeException("Not found"));

        assertThatThrownBy(() -> productService.getDetailedProductByBarcodeNumber(888888L))
                .isInstanceOf(RuntimeException.class);
    }
}
