package org.example.ydgbackend.Controller;

import org.example.ydgbackend.Dto.Brand.AddBrandDto;
import org.example.ydgbackend.Dto.Brand.BrandDto;
import org.example.ydgbackend.Dto.Brand.UpdateBrandDto;
import org.example.ydgbackend.Service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService)
    {
        this.brandService = brandService;
    }

    @PostMapping("/add")
    public boolean addBrand(@RequestBody AddBrandDto addBrandDto)
    {
        return brandService.addBrand(addBrandDto);
    }

    @PutMapping("/update")
    public boolean updateBrand(@RequestBody UpdateBrandDto updateBrandDto)
    {
        return brandService.updateBrand(updateBrandDto);
    }

    @DeleteMapping("/delete/{brandId}")
    public boolean deleteBrand(@PathVariable Long brandId)
    {
        return brandService.deleteBrand(brandId);
    }

    @GetMapping("/getBrand/{brandId}")
    public BrandDto getBrand(@PathVariable Long brandId)
    {
        return brandService.getBrand(brandId);
    }

    @GetMapping("/getBrands")
    public List<BrandDto> getBrands()
    {
        return brandService.getBrands();
    }

}
