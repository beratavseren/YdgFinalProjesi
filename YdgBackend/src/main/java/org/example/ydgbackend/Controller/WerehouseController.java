package org.example.ydgbackend.Controller;

import org.example.ydgbackend.Dto.WereHouse.AddWerehouseDto;
import org.example.ydgbackend.Dto.WereHouse.DetailedWerehouseDto;
import org.example.ydgbackend.Dto.WereHouse.UpdateWerehouseDto;
import org.example.ydgbackend.Dto.WereHouse.WerehouseDto;
import org.example.ydgbackend.Service.WerehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/werehouse")
public class WerehouseController {
    WerehouseService werehouseService;

    @Autowired
    public WerehouseController(WerehouseService wereHouseService)
    {
        this.werehouseService = wereHouseService;
    }

    @PostMapping("/add")
    public boolean addWerehouse(@RequestBody AddWerehouseDto addWerehousedto)
    {
        return werehouseService.addWerehouse(addWerehousedto);
    }

    @PutMapping("/update")
    public boolean updateWerehouse(@RequestBody UpdateWerehouseDto updateWerehouseDto)
    {
        return werehouseService.updateWerehouse(updateWerehouseDto);
    }

    @DeleteMapping("/delete/{werehouseId}")
    public boolean deleteWerehouse(@PathVariable Long werehouseId)
    {
        return werehouseService.deleteWerehouse(werehouseId);
    }

    @GetMapping("/getWerehouse/{werehouseId}")
    public DetailedWerehouseDto getWerehouse(@PathVariable Long werehouseId)
    {
        return werehouseService.getWerehouse(werehouseId);
    }

    @GetMapping("/getWerehouses")
    public List<WerehouseDto> getWerehouses()
    {
        return werehouseService.getWerehouses();
    }

    @GetMapping("/getWerehousesWithWeigth")
    public List<WerehouseDto> getWerehousesWithWeigth()
    {
        return werehouseService.getWerehousesWithWeigth();
    }
}
