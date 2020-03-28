package com.clip.assesment.services;

import com.clip.assesment.dto.ReportLineDTO;
import com.clip.assesment.dto.TransactionDTO;
import com.clip.assesment.dto.TransactionSumDTO;

import java.util.List;

public interface TransactionService {

    TransactionDTO findTransactionById(String transactionId);

    List<TransactionDTO>  findAllTransactionsByUserId(Long userId);

    TransactionDTO create(TransactionDTO transaction);

    TransactionSumDTO summarizeTransactions(Long userId);

    List<ReportLineDTO> generateWeeklyTransactionReportByUserId(Long userId);

    TransactionDTO findRandomTransaction(String transactionId);

}
