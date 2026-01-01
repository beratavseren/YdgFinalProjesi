
package org.example.ydgbackend.Service;

import org.example.ydgbackend.Dto.Brand.AddBrandDto;
import org.example.ydgbackend.Dto.Brand.BrandDto;
import org.example.ydgbackend.Dto.Brand.CategoryDto;
import org.example.ydgbackend.Dto.Brand.UpdateBrandDto;
import org.example.ydgbackend.Entity.Brand;
import org.example.ydgbackend.Entity.BrandCategory;
import org.example.ydgbackend.Entity.Product;
import org.example.ydgbackend.Repository.BrandCategoryRepo;
import org.example.ydgbackend.Repository.BrandRepo;
import org.example.ydgbackend.Repository.CategoryRepo;
import org.example.ydgbackend.Repository.ProductRepo;
import jakarta.transaction.Transactional;
import jakarta.transaction.TransactionalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BrandService {
    BrandRepo brandRepo;
    CategoryRepo categoryRepo;
    BrandCategoryRepo brandCategoryRepo;
    ProductRepo productRepo;

    @Autowired
    public BrandService(BrandRepo brandRepo, BrandCategoryRepo brandCategoryRepo, CategoryRepo categoryRepo, ProductRepo productRepo)
    {
        this.brandRepo=brandRepo;
        this.brandCategoryRepo=brandCategoryRepo;
        this.categoryRepo=categoryRepo;
        this.productRepo=productRepo;
    }

    @Transactional
    public boolean addBrand(AddBrandDto addBrandDto)
    {
        try {
            Brand brand=brandRepo.save(new Brand(addBrandDto.getBrandName()));

            for (CategoryDto categoryDto: addBrandDto.getCategoryDtos())
            {
                brandCategoryRepo.save(new BrandCategory(brand, categoryRepo.findCategoryByCategoryId(categoryDto.getCategoryId())));
            }

            return true;
        }catch (TransactionalException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //test et
    @Transactional
    public boolean updateBrand(UpdateBrandDto updateBrandDto)
    {
        try {
            Brand brand = brandRepo.findByBrandId(updateBrandDto.getBrandId());

            List<CategoryDto> categoryDtos = updateBrandDto.getUpdatedCategoryList();
            System.out.println("size: "+categoryDtos.size());
            List<BrandCategory> brandCategories = brandCategoryRepo.findBrandCategoriesByBrand(brand);
            System.out.println("size: "+brandCategories.size());

            for (int i=0;i< categoryDtos.size();i++)
            {
                for (int j=0;j< brandCategories.size();j++)
                {
                    System.out.println(brandCategories.get(j).getBrandCategoryId());
                    if (Objects.equals(categoryDtos.get(i).getCategoryId(), brandCategories.get(j).getCategory().getCategoryId()))
                    {
                        brandCategories.remove(brandCategories.get(j));
                    }
                }
            }
            for (BrandCategory brandCategory:brandCategories)
            {
                brandCategoryRepo.delete(brandCategory);
            }
            for (CategoryDto categoryDto: updateBrandDto.getAddedCategoryList())
            {
                brandCategoryRepo.save(new BrandCategory(brand,categoryRepo.findCategoryByCategoryId(categoryDto.getCategoryId())));
            }
            brand.setBrandName(updateBrandDto.getBrandName());
            brandRepo.save(brand);
            return true;

        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    @Transactional
    public boolean deleteBrand(Long brandId)
    {
        try {
            Brand brand = brandRepo.findByBrandId(brandId);

            List<Product> products = productRepo.findProductsByBrand(brand);

            if (products.isEmpty())
            {
                brandCategoryRepo.deleteBrandCategoriesByBrand(brand);
                brandRepo.delete(brand);
                return true;
            }else {
                return false;
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Transactional
    public BrandDto getBrand(Long brandId)
    {
        Optional<Brand> brand = brandRepo.findById(brandId);
        if (brand.isPresent()){
            return new BrandDto(brand.get().getBrandId(), brand.get().getBrandName());
        }else {
            throw new RuntimeException("Brand not found");
        }
    }

    @Transactional
    public List<BrandDto> getBrands()
    {
        List<Brand> brands = brandRepo.findAll();

        List<BrandDto> brandDtos = new ArrayList<>();

        for (Brand brand:brands)
        {
            brandDtos.add(new BrandDto(brand.getBrandId(), brand.getBrandName()));
        }

        return brandDtos;
    }
}