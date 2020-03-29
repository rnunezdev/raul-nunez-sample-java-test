package com.clip.assesment.services.impl;

import com.clip.assesment.dao.TransactionDao;
import com.clip.assesment.dao.UserDao;
import com.clip.assesment.dto.ReportLineDTO;
import com.clip.assesment.dto.TransactionDTO;
import com.clip.assesment.dto.TransactionSumDTO;
import com.clip.assesment.exceptions.NotFoundException;
import com.clip.assesment.model.Transaction;
import com.clip.assesment.model.User;
import com.clip.assesment.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private UserDao userDao;


    @Override
    public TransactionDTO findTransactionById(Long userId, UUID transactionId) {
        Transaction entityTransaction = transactionDao.findById(transactionId).orElse(null);
        if(entityTransaction == null || !entityTransaction.getUser().getId().equals(userId)){
            throw new NotFoundException("Transaction not found");
        }
        return this.toDTO(entityTransaction);
    }

    @Override
    public List<TransactionDTO> findAllTransactionsByUserId(Long userId) {
        List<TransactionDTO> transactionList = new ArrayList<>();
        User user = userDao.findById(userId).orElse(null);
        if (user != null) {
            transactionList = transactionDao.findAllByUserOrderByDate(user).stream().map(tx -> this.toDTO(tx)).collect(Collectors.toList());
        }

        return transactionList;
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
        transaction.setTransactionId(entityTransaction.getId());
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
