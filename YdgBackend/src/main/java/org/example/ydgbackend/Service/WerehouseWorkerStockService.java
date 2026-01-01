package org.example.ydgbackend.Service;

import org.example.ydgbackend.Dto.Stock.DetailedTransactionDto;
import org.example.ydgbackend.Dto.Stock.ProductTransactionDto;
import org.example.ydgbackend.Dto.WerehouseWorker.ApproveTransactionDto;
import org.example.ydgbackend.Entity.ProductTransaction;
import org.example.ydgbackend.Entity.ProductWerehouse;
import org.example.ydgbackend.Entity.Transaction;
import org.example.ydgbackend.Entity.WerehouseWorker;
import org.example.ydgbackend.Repository.ProductTransactionRepo;
import org.example.ydgbackend.Repository.ProductWerehouseRepo;
import org.example.ydgbackend.Repository.TransactionRepo;
import org.example.ydgbackend.Repository.WerehouseWorkerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WerehouseWorkerStockService {

    private final TransactionRepo transactionRepo;
    private final ProductTransactionRepo productTransactionRepo;
    private final ProductWerehouseRepo productWerehouseRepo;
    private final WerehouseWorkerRepo werehouseWorkerRepo;

    @Autowired
    public WerehouseWorkerStockService(TransactionRepo transactionRepo, ProductTransactionRepo productTransactionRepo, ProductWerehouseRepo productWerehouseRepo, WerehouseWorkerRepo werehouseWorkerRepo) {
        this.transactionRepo = transactionRepo;
        this.productTransactionRepo = productTransactionRepo;
        this.productWerehouseRepo = productWerehouseRepo;
        this.werehouseWorkerRepo = werehouseWorkerRepo;
    }

    @Transactional
    public DetailedTransactionDto getDetailedTransaction(Long transactionId, Long werehouseWorkerId) {
        try{
            Transaction transaction = transactionRepo.findTransactionByTransactionId(transactionId);
            WerehouseWorker werehouseWorker = findWerehouseWorkerByWerehouseWorkerId(werehouseWorkerId);

            if (transaction==null || werehouseWorker==null)
            {
                throw new RuntimeException("Transaction or werehouse worker not found");
            }

            if (!transaction.getWerehouse().equals(werehouseWorker.getWerehouse()))
            {
                throw new RuntimeException("This werehouse worker does not have this transaction.");
            }

            List<ProductTransaction> productTransactions = productTransactionRepo.findProductTransactionsByTransaction(transaction);
            List<ProductTransactionDto> productTransactionDtos = new ArrayList<>();

            for (ProductTransaction productTransaction : productTransactions)
            {
                productTransactionDtos.add(new ProductTransactionDto(productTransaction.getProductTransactionId(), productTransaction.getQuantityReceived(), productTransaction.getExpectedQuantity()));
            }

            return new DetailedTransactionDto(transaction.getTransactionId(), transaction.getTransactionType(), transaction.getWerehouse().getWerehouseName(), transaction.isSituation(), productTransactionDtos);
        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public boolean approveTransaction(ApproveTransactionDto approveTransactionDto, Long werehouseWorkerId) {
        try {
            Transaction transaction = transactionRepo.findTransactionByTransactionId(approveTransactionDto.getTransactionId());
            WerehouseWorker werehouseWorker = findWerehouseWorkerByWerehouseWorkerId(werehouseWorkerId);

            if (transaction==null || werehouseWorker==null)
            {
                throw new RuntimeException("Transaction or werehouse worker not found");
            }

            if (!transaction.getWerehouse().equals(werehouseWorker.getWerehouse()))
            {
                throw new RuntimeException("This werehouse worker does not have this transaction.");
            }

            List<ProductTransaction> productTransactions = productTransactionRepo.findProductTransactionsByTransaction(transaction);

            //admin stok işlemlerinde received quantity yi 0 olarak başlat null olamasın!! aynı şekilde stuation kısmını da false olarak başlat null olamasın.
            //stuation kısmı filled, partially filled ve ready olarak değiştir. enum yap
            for (int i = 0; i < productTransactions.size(); i++) {
                for (int j = 0; j < approveTransactionDto.getProductTransactionDtos().size(); j++)
                {
                    if (productTransactions.get(i).getProduct().getProductId().equals(approveTransactionDto.getProductTransactionDtos().get(j).getProductId()))
                    {
                        productTransactions.get(i).setQuantityReceived(productTransactions.get(i).getQuantityReceived()+approveTransactionDto.getProductTransactionDtos().get(j).getQuantityReceived());

                        ProductWerehouse productWerehouse = productWerehouseRepo.findProductWerehousesByProductAndWerehouse(productTransactions.get(i).getProduct(), werehouseWorker.getWerehouse());
                        productWerehouse.setQuantity(productWerehouse.getQuantity()+approveTransactionDto.getProductTransactionDtos().get(j).getQuantityReceived());

                        productWerehouseRepo.save(productWerehouse);
                        productTransactionRepo.save(productTransactions.get(i));
                    }
                }
            }

            //String message = "deneme";
            //WebSocketForStockUpdate.sendMessage(message, werehouseWorker.getWerehouse().getWerehouseId());
            return true;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private WerehouseWorker findWerehouseWorkerByWerehouseWorkerId(Long werehouseWorkerId)
    {
        return werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(werehouseWorkerId);
    }
}