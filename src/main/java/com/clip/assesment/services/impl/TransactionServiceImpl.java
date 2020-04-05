package com.clip.assesment.services.impl;

import ch.qos.logback.classic.Logger;
import com.clip.assesment.dao.TransactionDao;
import com.clip.assesment.dao.UserDao;
import com.clip.assesment.dto.ReportLineDTO;
import com.clip.assesment.dto.TransactionDTO;
import com.clip.assesment.dto.TransactionSumDTO;
import com.clip.assesment.exceptions.NotFoundException;
import com.clip.assesment.model.Transaction;
import com.clip.assesment.model.User;
import com.clip.assesment.services.TransactionService;
import com.clip.assesment.utils.CustomWeekFinishTemporalAdjuster;
import com.clip.assesment.utils.CustomWeeklyTemporalAdjuster;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
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
        Transaction entityTransaction;
        try {
            entityTransaction = this.toEntity(user, transaction);
        } catch (ParseException ex) {
            logger.error("Error parsing date while creating a transaction: " + transaction.getDate());
            throw new RuntimeException("Error parsing date while creating a transaction");
        }
        return this.toDTO(transactionDao.save(entityTransaction));
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
    public List<ReportLineDTO> generateWeeklyTransactionReportByUserId(Long userId) { WeekFields week = WeekFields.of(DayOfWeek.FRIDAY, 1);
        CustomWeeklyTemporalAdjuster temporalAdjuster = new CustomWeeklyTemporalAdjuster();
        CustomWeekFinishTemporalAdjuster customWeekFinishTemporalAdjuster = new CustomWeekFinishTemporalAdjuster();
        User user = userDao.findById(userId).orElse(null);
        List<ReportLineDTO> result = transactionDao.findAllByUserOrderByDate(user).stream()
                .collect(Collectors.groupingBy( item -> this.convertToLocalDate(item.getDate()).with(temporalAdjuster) ))
                .entrySet().stream() // Second stream for ordering the Map
                .collect(Collectors.toMap(Map.Entry::getKey,
                                          Map.Entry::getValue,
                                          (oldValue, newValue) -> oldValue,
                                          TreeMap::new) // We order the Map by writing in an ordered TreeMap implementation
                ).entrySet().stream()
                .map(entry -> {
                    ReportLineDTO line = new ReportLineDTO();
                    LocalDate endDate = entry.getKey().with(customWeekFinishTemporalAdjuster);
                    line.setUserId(userId);
                    line.setWeekStart(entry.getKey().toString() + " " + entry.getKey().getDayOfWeek());
                    line.setAmount(entry.getValue().stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                    line.setQuantity(entry.getValue().size());
                    line.setWeekEnd(endDate.toString() + " " + endDate.getDayOfWeek());
                    return line;
                }).collect(Collectors.toList());

        BigDecimal counter = BigDecimal.ZERO;
        for (ReportLineDTO item : result) {
            item.setTotalAmount(counter);
            counter = counter.add(item.getAmount());
        }

        return result;
    }

    @Override
    public TransactionDTO findRandomTransaction() {
        List<Transaction> allTransactions = transactionDao.findAll();
        Random r = new Random();
        return toDTO(allTransactions.get(r.nextInt(allTransactions.size())));
    }

    private TransactionDTO toDTO(Transaction entityTransaction) {
        TransactionDTO transaction = new TransactionDTO();
        transaction.setTransactionId(entityTransaction.getId());
        transaction.setUserId(entityTransaction.getUser().getId());
        transaction.setDescription(entityTransaction.getDescription());
        transaction.setAmount(entityTransaction.getAmount());
        transaction.setDate(entityTransaction.getDate(), TimeZone.getDefault().getID());
        return transaction;
    }

    private Transaction toEntity(User user, TransactionDTO transaction) throws ParseException {
        Transaction entityTransaction = new Transaction();

        entityTransaction.setUser(user);
        entityTransaction.setDescription(transaction.getDescription());
        entityTransaction.setAmount(transaction.getAmount());
        entityTransaction.setDate(transaction.getDateConverted(TimeZone.getDefault().getID()));
        return entityTransaction;
    }

    public LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
