package com.demo.controller;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class ExpenseCommand {

    @NotNull
    private BigDecimal amount;

    private BigDecimal taxAmount;

    private String currencyCode;

    private BigDecimal domesticCurrencyAmount;

    @Length(max = 300)
    private String reason;

    @NotNull
    private String date;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getDomesticCurrencyAmount() {
        return domesticCurrencyAmount;
    }

    public void setDomesticCurrencyAmount(BigDecimal domesticCurrencyAmount) {
        this.domesticCurrencyAmount = domesticCurrencyAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
}
