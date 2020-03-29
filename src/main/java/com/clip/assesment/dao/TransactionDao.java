package com.clip.assesment.dao;

import com.clip.assesment.model.Transaction;
import com.clip.assesment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransactionDao extends JpaRepository<Transaction, UUID> {

    List<Transaction> findAllByUserOrderByDate(User user);

}
