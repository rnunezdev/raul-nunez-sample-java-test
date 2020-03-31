package com.clip.assesment.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class TransactionDTO {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private UUID transactionId;

    private Long userId;

    private String description;

    private BigDecimal amount;

    private String date;


    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Date getDateConverted(String timeZone) throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return dateFormat.parse(this.date);
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(Date date, String timeZone) {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        this.date = dateFormat.format(date);
    }
}
