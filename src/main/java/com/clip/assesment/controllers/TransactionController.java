package com.clip.assesment.controllers;


import com.clip.assesment.dto.ReportLineDTO;
import com.clip.assesment.dto.TransactionDTO;
import com.clip.assesment.dto.TransactionSumDTO;
import com.clip.assesment.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController extends BaseController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    @Description(value = "Create a new transaction associated to a user")
    public @ResponseBody
    TransactionDTO createYoutubeBrandSafetyConfiguration(@RequestBody final TransactionDTO transactionDTO, @PathVariable Long userId) {
        logger.info("Create a new transaction");
        transactionDTO.setUserId(userId);
        return transactionService.create( transactionDTO );
    }

    @RequestMapping(value= "/{userId}/{transactionId}", method = RequestMethod.GET)
    @Description(value = "Get the transactionId provided and check if it belongs to thee userId provided")
    public @ResponseBody
    TransactionDTO findTransactionByUserId(@PathVariable UUID transactionId, @PathVariable Long userId) {
        logger.info("Find a transaction corresponding to a user");
        return transactionService.findTransactionById(userId, transactionId);
    }

    @RequestMapping(value= "/{userId}/all", method = RequestMethod.GET)
    @Description(value = "Get all the transactions associated to a userId ordered by date")
    public @ResponseBody
    List<TransactionDTO> findAllTransactionByUserId(@PathVariable Long userId) {
        logger.info("Find all transactions corresponding to a user ordered by date");
        return transactionService.findAllTransactionsByUserId(userId);
    }

    @RequestMapping(value= "/{userId}/sum", method = RequestMethod.GET)
    @Description(value = "Summarize all the transactions associated to a userId")
    public @ResponseBody
    TransactionSumDTO sumAllTransactionByUserId(@PathVariable Long userId) {
        logger.info("Summarize all transactions corresponding to a user");
        return transactionService.summarizeTransactions(userId);
    }

    @RequestMapping(value= "/{userId}/weekly", method = RequestMethod.GET)
    @Description(value = "Summarize all the transactions associated to a userId")
    public @ResponseBody
    List<ReportLineDTO> getWeeklyTransactionReportByUserId(@PathVariable Long userId) {
        logger.info("Get a weekly report of all transactions corresponding to a user");
        return transactionService.generateWeeklyTransactionReportByUserId(userId);
    }


}
