package com.clip.assesment.services.impl;

import ch.qos.logback.classic.Logger;
import com.clip.assesment.controllers.BaseController;
import com.clip.assesment.dao.TransactionDao;
import com.clip.assesment.dao.UserDao;
import com.clip.assesment.dto.ReportLineDTO;
import com.clip.assesment.dto.TransactionDTO;
import com.clip.assesment.dto.TransactionSumDTO;
import com.clip.assesment.exceptions.NotFoundException;
import com.clip.assesment.model.Transaction;
import com.clip.assesment.model.User;
import com.clip.assesment.services.TransactionService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    protected Logger logger = (Logger) LoggerFactory.getLogger(TransactionServiceImpl.class);


    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private UserDao userDao;


    @Override
    public TransactionDTO findTransactionById(Long userId, UUID transactionId) {
        Transaction entityTransaction = transactionDao.findById(transactionId).orElse(null);
        if(entityTransaction == null || entityTransaction.getUser() == null || !entityTransaction.getUser().getId().equals(userId)){
            logger.error("Either the Transaction was not found or it's not related to the user");
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
        User user = userDao.findById(transaction.getUserId()).orElse(null);
        return this.toDTO(transactionDao.save(this.toEntity(user, transaction)));
    }

    @Override
    public TransactionSumDTO summarizeTransactions(Long userId) {
        TransactionSumDTO dto = new TransactionSumDTO();
        User user = userDao.findById(userId).orElse(null);
        if (user != null) {
            BigDecimal sum = transactionDao.findAllByUser(user).stream().
                    map(tx -> tx.getAmount()).
                    reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setSum(sum);
        }
        dto.setUserId(userId);

        return dto;
    }

    @Override
    public List<ReportLineDTO> generateWeeklyTransactionReportByUserId(Long userId) {
        WeekFields week = WeekFields.of(DayOfWeek.FRIDAY, 1);
        User user = userDao.findById(userId).orElse(null);
        List<ReportLineDTO> result = transactionDao.findAllByUserOrderByDate(user).stream()
                .collect(Collectors.groupingBy( item -> this.convertToLocalDate(item.getDate()).with(TemporalAdjusters.previousOrSame(DayOfWeek.of(5))) )) // Group by Week Fri to Thu
                .entrySet().stream() // Second stream for ordering the Map
                .collect(Collectors.toMap(Map.Entry::getKey,
                                          Map.Entry::getValue,
                                          (oldValue, newValue) -> oldValue,
                                          TreeMap::new) // We order the Map by writing in an ordered TreeMap implementation
                ).entrySet().stream()
                .map(entry -> {
                    ReportLineDTO line = new ReportLineDTO();
                    line.setUserId(userId);
                    line.setWeekStart(entry.getKey().toString() + " " + entry.getKey().getDayOfWeek());
                    line.setAmount(entry.getValue().stream().map(Transaction::getAmount).reduce(new BigDecimal('0'), BigDecimal::add));
                    line.setQuantity(entry.getValue().size());
                    line.setWeekEnd("");
                    return line;
                }).collect(Collectors.toList());

        return result;
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

    private Transaction toEntity(User user, TransactionDTO transaction) {
        Transaction entityTransaction = new Transaction();

        entityTransaction.setUser(user);
        entityTransaction.setDescription(transaction.getDescription());
        entityTransaction.setAmount(transaction.getAmount());
        entityTransaction.setDate(transaction.getDate());
        return entityTransaction;
    }

    public LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
