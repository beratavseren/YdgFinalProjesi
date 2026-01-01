package org.example.ydgbackend.Controller;


import org.example.ydgbackend.Dto.Stock.DetailedTransactionDto;
import org.example.ydgbackend.Dto.WerehouseWorker.ApproveTransactionDto;
import org.example.ydgbackend.Service.WerehouseWorkerStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/werehouseWorkerStock")
public class WerehouseWorkerStockController {
    WerehouseWorkerStockService werehouseWorkerStockService;

    @Autowired
    public WerehouseWorkerStockController(WerehouseWorkerStockService werehouseWorkerStockService)
    {
        this.werehouseWorkerStockService = werehouseWorkerStockService;
    }

    //get werehouseWorkerId from session
    @GetMapping(value = "/getDetailedTransaction/{transactionId}")
    public DetailedTransactionDto getDetailedTransaction(@PathVariable Long transactionId)
    {
        Long werehouseWorkerId = 1L;
        return werehouseWorkerStockService.getDetailedTransaction(transactionId, werehouseWorkerId);
    }

    //get werehouseWorkerId from session
    @PutMapping(value = "/approveTransaction")
    public boolean approveTransaction(@RequestBody ApproveTransactionDto approveTransactionDto)
    {
        Long werehouseWorkerId = 1L;
        return werehouseWorkerStockService.approveTransaction(approveTransactionDto, werehouseWorkerId);
    }
}
