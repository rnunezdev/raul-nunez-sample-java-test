package com.clip.assesment.dto;

public class TransactionSumDTO {

    private Long userId;

    private Double sum;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }
}
