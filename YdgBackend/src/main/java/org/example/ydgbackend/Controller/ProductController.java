package org.example.ydgbackend.Controller;

import org.example.ydgbackend.Dto.Product.*;
import org.example.ydgbackend.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    ProductService productService;

    @Autowired
    public ProductController(ProductService productService)
    {
        this.productService=productService;
    }

    @PostMapping("/add")
    public boolean addProduct(@RequestBody AddProductDto addProductDto)
    {
        return productService.addProduct(addProductDto);
    }

    @PutMapping("/update")
    public boolean updateProduct(@RequestBody UpdateProductDto updateProductDto)
    {
        return productService.updateProduct(updateProductDto);
    }

    @DeleteMapping("/delete/{productId}")
    public boolean deleteProduct(@PathVariable Long productId)
    {
        return productService.deleteProduct(productId);
    }

    @GetMapping("/getProducts")
    public List<ProductDto> getProducts()
    {
        return productService.getProducts();
    }

    @GetMapping("/listProduct")
    public List<GetProductListForProductPageDto> getProductListForProductPage()
    {
        return productService.getProductListForProductPage();
    }

    @GetMapping("/getProduct/{productId}")
    public DetailedProductDto getDetailedProduct(@PathVariable Long productId)
    {
        return productService.getDetailedProduct(productId);
    }

    @GetMapping("/getProductByBarcodeNumber/{barcodeNumber}")
    public DetailedProductDto getDetailedproductDtoByBarcodeNumber(@PathVariable Long barcodeNumber)
    {
        return productService.getDetailedProductByBarcodeNumber(barcodeNumber);
    }
}
