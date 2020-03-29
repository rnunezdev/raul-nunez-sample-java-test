package com.clip.assesment.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransactionSumDTO {

    private Long userId;

    private BigDecimal sum = new BigDecimal("0");


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum.setScale(2, RoundingMode.HALF_EVEN);
    }
}
