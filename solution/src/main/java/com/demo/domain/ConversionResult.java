package com.demo.domain;

import java.math.BigDecimal;

public class ConversionResult {
    private Money amount;
    private Money domesticAmount;
    private BigDecimal rate;

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public Money getDomesticAmount() {
        return domesticAmount;
    }

    public void setDomesticAmount(Money domesticAmount) {
        this.domesticAmount = domesticAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
