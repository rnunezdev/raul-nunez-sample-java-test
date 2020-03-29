package com.clip.assesment.services;

import com.clip.assesment.dto.ReportLineDTO;
import com.clip.assesment.dto.TransactionDTO;
import com.clip.assesment.dto.TransactionSumDTO;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    TransactionDTO findTransactionById(Long userId, UUID transactionId);

    List<TransactionDTO>  findAllTransactionsByUserId(Long userId);

    TransactionDTO create(TransactionDTO transaction);

    TransactionSumDTO summarizeTransactions(Long userId);

    List<ReportLineDTO> generateWeeklyTransactionReportByUserId(Long userId);

    TransactionDTO findRandomTransaction(String transactionId);

}
