package com.clip.assesment.services.impl;

import com.clip.assesment.dao.TransactionDao;
import com.clip.assesment.dao.UserDao;
import com.clip.assesment.dto.ReportLineDTO;
import com.clip.assesment.dto.TransactionDTO;
import com.clip.assesment.dto.TransactionSumDTO;
import com.clip.assesment.model.Transaction;
import com.clip.assesment.model.User;
import com.clip.assesment.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private UserDao userDao;


    @Override
    public TransactionDTO findTransactionById(String transactionId) {
        return null;
    }

    @Override
    public List<TransactionDTO> findAllTransactionsByUserId(Long userId) {
        return null;
    }

    @Override
    public TransactionDTO create(TransactionDTO transaction) {
        return this.toDTO(transactionDao.save(this.toEntity(transaction)));
    }

    @Override
    public TransactionSumDTO summarizeTransactions(Long userId) {
        return null;
    }

    @Override
    public List<ReportLineDTO> generateWeeklyTransactionReportByUserId(Long userId) {
        return null;
    }

    @Override
    public TransactionDTO findRandomTransaction(String transactionId) {
        return null;
    }

    private TransactionDTO toDTO(Transaction entityTransaction) {
        TransactionDTO transaction = new TransactionDTO();
        transaction.setId(entityTransaction.getId());
        transaction.setUserId(entityTransaction.getUser().getId());
        transaction.setDescription(entityTransaction.getDescription());
        transaction.setAmount(entityTransaction.getAmount());
        transaction.setDate(entityTransaction.getDate());
        return transaction;
    }

    private Transaction toEntity(TransactionDTO transaction) {
        User user = userDao.findById(transaction.getUserId()).orElse(null);
        Transaction entityTransaction = new Transaction();

        entityTransaction.setUser(user);
        entityTransaction.setDescription(transaction.getDescription());
        entityTransaction.setAmount(transaction.getAmount());
        entityTransaction.setDate(transaction.getDate());
        return entityTransaction;
    }
}
