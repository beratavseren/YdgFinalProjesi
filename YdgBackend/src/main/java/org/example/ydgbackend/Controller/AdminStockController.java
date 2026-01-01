package org.example.ydgbackend.Controller;

import org.example.ydgbackend.Dto.Stock.*;
import org.example.ydgbackend.Entity.TransactionType;
import org.example.ydgbackend.Service.AdminStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class AdminStockController {

    AdminStockService adminStockService;

    @Autowired
    public AdminStockController(AdminStockService adminStockService)
    {
        this.adminStockService = adminStockService;
    }

    @PostMapping("/in")
    public boolean buyProduct(@RequestBody AddTransactionDto addTransactionDto)
    {
        return adminStockService.inProduct(addTransactionDto);
    }

    @PostMapping("/out")
    public boolean sellProduct(@RequestBody AddTransactionDto addTransactionDto) {
        return adminStockService.outProduct(addTransactionDto);
    }

    @PostMapping("/betweenWerehouses")
    public boolean betweenWerehouses(@RequestBody TransactionBetweenWerehouseDto transactionBetweenWerehouseDto)
    {
        return adminStockService.betweenWerehouses(transactionBetweenWerehouseDto);
    }

    @GetMapping("/getDetailedTransaction/{transactionId}")
    public DetailedTransactionDto getDetailedTransaction(@PathVariable Long transactionId)
    {
        return adminStockService.getDetailedTransaction(transactionId);
    }


    //Transactionlar için filtreleri belirle
    @GetMapping("/getTransactionsWithFilter")
    public List<TransactionDto> getTransactionsWithFilter(
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) Long werehouseId,
            @RequestParam(required = false) Boolean transactionStuation)
    {
        return adminStockService.getTransactionsWithFilter(transactionType, werehouseId, transactionStuation);
    }

    @GetMapping(value = "/getReceipt/{transactionId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getReceipt(@PathVariable Long transactionId)
    {
        return adminStockService.receipts(transactionId);
    }

    @GetMapping(value = "/getReceipt", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getReceipt()
    {
        return adminStockService.receipts();
    }

    @GetMapping(value = "/searchByTransactionId")
    public List<TransactionDto> searchByTransactionId(@RequestParam Long transactionId)
    {
        return adminStockService.searchByTransactionId(transactionId);
    }

    @PutMapping(value = "/setCriticalStockLevel")
    public boolean setCriticalStockLevelForProductWerehouse(@RequestBody SetCriticalStockLevelDto setCriticalStockLevelDto)
    {
        return adminStockService.setCriticalStockLevelForProductWerehouse(setCriticalStockLevelDto);
    }
}