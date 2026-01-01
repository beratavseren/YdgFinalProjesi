package org.example.ydgbackend.Controller;

import org.example.ydgbackend.Dto.Stock.TransactionDto;
import org.example.ydgbackend.Dto.WereHouse.ProductQuantityDtoForDetailedWerehouseDto;
import org.example.ydgbackend.Dto.WerehouseWorker.WerehouseWorkerLandingInfoDto;
import org.example.ydgbackend.Service.WerehouseWorkerLandingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/werehouseWorkerLanding")
public class WerehouseWorkerLandingController {

    WerehouseWorkerLandingService werehouseWorkerLandingService;

    @Autowired
    public WerehouseWorkerLandingController(WerehouseWorkerLandingService werehouseWorkerLandingService)
    {
        this.werehouseWorkerLandingService = werehouseWorkerLandingService;
    }

    //get werehouseWorkerId from session
    @GetMapping(value = "/getWerehouseWorkerLandingInfo")
    public WerehouseWorkerLandingInfoDto getWerehouseWorkerLandingInfo()
    {
        Long werehouseWorkerId = 1L;
        return werehouseWorkerLandingService.getWerehouseWorkerLandingInfo(werehouseWorkerId);
    }

    @GetMapping(value = "/getProducts",  name = "test")
    public List<ProductQuantityDtoForDetailedWerehouseDto> getProducts()
    {
        Long werehouseWorkerId = 1L;
        return werehouseWorkerLandingService.getProducts(werehouseWorkerId);
    }

    @GetMapping(value = "/getCriticalStockCount")
    public List<ProductQuantityDtoForDetailedWerehouseDto> getCriticalStockCount()
    {
        Long werehouseWorkerId = 1L;
        return werehouseWorkerLandingService.getCriticalStockCount(werehouseWorkerId);
    }

    @GetMapping(value = "/getTransactions")
    public List<TransactionDto> getTransactions()
    {
        Long werehouseWorkerId = 1L;
        return werehouseWorkerLandingService.getTransactions(werehouseWorkerId);
    }
}
