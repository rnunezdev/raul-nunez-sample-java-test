package com.clip.assesment.controllers;


import com.clip.assesment.dto.TransactionDTO;
import com.clip.assesment.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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


}
