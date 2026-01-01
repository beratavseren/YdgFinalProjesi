package org.example.ydgbackend.Service;

import org.example.ydgbackend.Dto.Stock.TransactionDto;
import org.example.ydgbackend.Dto.WereHouse.ProductQuantityDtoForDetailedWerehouseDto;
import org.example.ydgbackend.Dto.WerehouseWorker.WerehouseWorkerLandingInfoDto;
import org.example.ydgbackend.Entity.ProductWerehouse;
import org.example.ydgbackend.Entity.Transaction;
import org.example.ydgbackend.Entity.WerehouseWorker;
import org.example.ydgbackend.Repository.ProductWerehouseRepo;
import org.example.ydgbackend.Repository.TransactionRepo;
import org.example.ydgbackend.Repository.WerehouseWorkerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WerehouseWorkerLandingService {

    private final WerehouseWorkerRepo werehouseWorkerRepo;
    private final ProductWerehouseRepo productWerehouseRepo;
    private final TransactionRepo transactionRepo;

    @Autowired
    public WerehouseWorkerLandingService(WerehouseWorkerRepo werehouseWorkerRepo, ProductWerehouseRepo productWerehouseRepo, TransactionRepo transactionRepo) {
        this.werehouseWorkerRepo = werehouseWorkerRepo;
        this.productWerehouseRepo = productWerehouseRepo;
        this.transactionRepo = transactionRepo;
    }

    @Transactional
    public WerehouseWorkerLandingInfoDto getWerehouseWorkerLandingInfo(Long werehouseWorkerId)
    {
        try {
            WerehouseWorker werehouseWorker = werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(werehouseWorkerId);

            List<ProductWerehouse> productWerehouses = productWerehouseRepo.findProductWerehousesByWerehouse(werehouseWorker.getWerehouse());

            int totalStock = 0;
            int criticalStockCount = 0;


            for (ProductWerehouse productWerehouse:productWerehouses)
            {
                totalStock+=productWerehouse.getQuantity();
                if (productWerehouse.getQuantity() <= productWerehouse.getCriticalStockLevel())
                {
                    criticalStockCount++;
                }
            }

            WerehouseWorkerLandingInfoDto werehouseWorkerLandingInfoDto = new WerehouseWorkerLandingInfoDto();

            werehouseWorkerLandingInfoDto.setTotalQuantity(totalStock);
            werehouseWorkerLandingInfoDto.setWeigthLimit(werehouseWorker.getWerehouse().getWeigthLimit());
            werehouseWorkerLandingInfoDto.setCurrentWeigth(werehouseWorkerLandingInfoDto.getCurrentWeigth());
            werehouseWorkerLandingInfoDto.setCriticalLevelProductQuantity(criticalStockCount);

            return werehouseWorkerLandingInfoDto;

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<ProductQuantityDtoForDetailedWerehouseDto> getProducts(Long werehouseWorkerId)
    {
        try {
            WerehouseWorker werehouseWorker = werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(werehouseWorkerId);
            List<ProductWerehouse> productWerehouses = productWerehouseRepo.findProductWerehousesByWerehouse(werehouseWorker.getWerehouse());

            List<ProductQuantityDtoForDetailedWerehouseDto> productQuantityDtoForDetailedWerehouseDtos = new ArrayList<>();

            for (ProductWerehouse productWerehouse:productWerehouses)
            {
                productQuantityDtoForDetailedWerehouseDtos.add(new ProductQuantityDtoForDetailedWerehouseDto(productWerehouse.getProduct().getProductName(), productWerehouse.getQuantity(), productWerehouse.getProduct().getProductId(), productWerehouse.getCriticalStockLevel()));
            }

            return productQuantityDtoForDetailedWerehouseDtos;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<ProductQuantityDtoForDetailedWerehouseDto> getCriticalStockCount(Long werehouseWorkerId)
    {
        try {
            WerehouseWorker werehouseWorker = werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(werehouseWorkerId);
            List<ProductWerehouse> productWerehouses = productWerehouseRepo.findProductWerehousesByWerehouse(werehouseWorker.getWerehouse());

            List<ProductQuantityDtoForDetailedWerehouseDto> productQuantityDtoForDetailedWerehouseDtos = new ArrayList<>();

            for (ProductWerehouse productWerehouse:productWerehouses)
            {
                if (productWerehouse.getQuantity() <= productWerehouse.getCriticalStockLevel())
                {
                    productQuantityDtoForDetailedWerehouseDtos.add(new ProductQuantityDtoForDetailedWerehouseDto(productWerehouse.getProduct().getProductName(), productWerehouse.getQuantity(), productWerehouse.getProduct().getProductId(), productWerehouse.getCriticalStockLevel()));
                }
            }

            return productQuantityDtoForDetailedWerehouseDtos;

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<TransactionDto> getTransactions(Long werehouseWorkerId)
    {
        try {
            WerehouseWorker werehouseWorker = werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(werehouseWorkerId);

            List<Transaction> transactions = transactionRepo.findTransactionsByWerehouse(werehouseWorker.getWerehouse());
            List<TransactionDto> transactionDtos = new ArrayList<>();

            for (Transaction transaction:transactions)
            {
                transactionDtos.add(new TransactionDto(transaction.getTransactionId(), transaction.getTransactionType(), transaction.getWerehouse().getWerehouseName()));
            }

            return transactionDtos;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
