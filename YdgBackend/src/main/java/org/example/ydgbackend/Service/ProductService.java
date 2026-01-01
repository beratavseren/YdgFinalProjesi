package org.example.ydgbackend.Service;

import org.example.ydgbackend.Dto.Brand.BrandDto;
import org.example.ydgbackend.Dto.Product.*;
import org.example.ydgbackend.Entity.Brand;
import org.example.ydgbackend.Entity.Product;
import org.example.ydgbackend.Entity.ProductWerehouse;
import org.example.ydgbackend.Entity.Werehouse;
import org.example.ydgbackend.Repository.BrandRepo;
import org.example.ydgbackend.Repository.ProductRepo;
import org.example.ydgbackend.Repository.ProductWerehouseRepo;
import org.example.ydgbackend.Repository.WerehouseRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//update yok çünkü productta update edilebilecek bir şey yok(1 brand var, id değişmez, isim değişmek mantıksız, barcode değişimi veri bütünlüğünü tehlikeye atabilir(veri tabanı triigerı veya backend triggerı ile yapılabilir ama gerek var mı barcode u ürün üreticisi yapar))
@Service
public class ProductService {
    ProductRepo productRepo;
    WerehouseRepo werehouseRepo;
    ProductWerehouseRepo productWerehouseRepo;
    BrandRepo brandRepo;

    @Autowired
    public ProductService(ProductRepo productRepo, BrandRepo brandRepo, WerehouseRepo werehouseRepo, ProductWerehouseRepo productWerehouseRepo)
    {
        this.productRepo=productRepo;
        this.werehouseRepo=werehouseRepo;
        this.brandRepo=brandRepo;
        this.productWerehouseRepo=productWerehouseRepo;
    }

    @Transactional
    public boolean addProduct(AddProductDto addProductDto)
    {
        try {

            Brand brand = brandRepo.findByBrandId(addProductDto.getBrandDto().getBrandId());
            Product product = new Product(addProductDto.getProductName(), addProductDto.getBarcodeNumber(), addProductDto.getWeigth(), addProductDto.getVolume(), brand);
            product = productRepo.save(product);

            List<Werehouse> werehouses = werehouseRepo.findAll();

            for (Werehouse werehouse:werehouses) {
                productWerehouseRepo.save(new ProductWerehouse(product, werehouse ,0));
            }

            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public boolean deleteProduct(Long productId) throws RuntimeException
    {
        try {

            Product product = productRepo.findByProductId(productId);
            List<ProductWerehouse> productWerehouses = productWerehouseRepo.findProductWerehousesByProduct(product);

            for (ProductWerehouse productWerehouse : productWerehouses) {

                if (productWerehouse.getQuantity()!=0)
                {
                    throw new RuntimeException("This product exists in a warehouse with quantity. Please empty the product quantity first.");
                }
                
                productWerehouseRepo.delete(productWerehouse);
            }
            
            productRepo.deleteById(productId);
            
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<ProductDto> getProducts()
    {
        try {
            List<Product> products = productRepo.findAll();
            List<ProductDto> productDtos = new ArrayList<>();

            for (Product product:products)
            {
                productDtos.add(new ProductDto(product.getProductId(), product.getProductName()));
            }

            return productDtos;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //Ürün detay ekranı (mobil) buraya istatistikleri de ekle
    @Transactional
    public DetailedProductDto getDetailedProduct(Long productId)
    {
        try {
            Product product = productRepo.findByProductId(productId);

            List<ProductWerehouse> productWerehouses = productWerehouseRepo.findProductWerehousesByProduct(product);

            List<WerehouseQuantityDtoForDetailedProductDto> werehouseQuantityDtoForDetailedProductDtos = new ArrayList<>();

            for (ProductWerehouse productWerehouse:productWerehouses)
            {
                werehouseQuantityDtoForDetailedProductDtos.add(new WerehouseQuantityDtoForDetailedProductDto(productWerehouse.getWerehouse().getWerehouseName(), productWerehouse.getQuantity()));
            }

            return new DetailedProductDto(product.getProductId(), product.getProductName(), product.getBarcodeNumber(), product.getComment(), product.getWeight(), product.getVolume(), werehouseQuantityDtoForDetailedProductDtos, new BrandDto(product.getBrand().getBrandId(), product.getBrand().getBrandName()));
        }catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public DetailedProductDto getDetailedProductByBarcodeNumber(Long barcodeNumber)
    {
        try {

            Product product = productRepo.findByBarcodeNumber(barcodeNumber);

            List<ProductWerehouse> productWerehouses = productWerehouseRepo.findProductWerehousesByProduct(product);

            List<WerehouseQuantityDtoForDetailedProductDto> werehouseQuantityDtoForDetailedProductDtos = new ArrayList<>();

            for (ProductWerehouse productWerehouse:productWerehouses)
            {
                werehouseQuantityDtoForDetailedProductDtos.add(new WerehouseQuantityDtoForDetailedProductDto(productWerehouse.getWerehouse().getWerehouseName(), productWerehouse.getQuantity()));
            }

            return new DetailedProductDto(product.getProductId(), product.getProductName(), product.getBarcodeNumber(), product.getComment(), product.getWeight(), product.getVolume(), werehouseQuantityDtoForDetailedProductDtos, new BrandDto(product.getBrand().getBrandId(), product.getBrand().getBrandName()));

        }catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<GetProductListForProductPageDto> getProductListForProductPage(){
        try {
            List<GetProductListForProductPageDto> getProductListForProductPageDtos = new ArrayList<>();

            List<Product> products = productRepo.findAll();

            int totalQuantity;

            for (Product product:products)
            {
                List<ProductWerehouse> productWerehouses = productWerehouseRepo.findProductWerehousesByProduct(product);
                totalQuantity=0;
                for (ProductWerehouse productWerehouse:productWerehouses)
                {
                    totalQuantity = productWerehouse.getQuantity()+totalQuantity;
                }

                getProductListForProductPageDtos.add(new GetProductListForProductPageDto(product.getProductId(), product.getProductName(), totalQuantity,new BrandDto(product.getBrand().getBrandId(), product.getBrand().getBrandName())));
            }

            return getProductListForProductPageDtos;

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public boolean updateProduct(UpdateProductDto updateProductDto) throws RuntimeException
    {
        try {
            Product product = productRepo.findByProductId(updateProductDto.getProductId());
            if (product == null)
            {
                throw new RuntimeException("The product you want to update does not exist.");
            }
            product.setComment(updateProductDto.getComment());
            productRepo.save(product);
            return true;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
