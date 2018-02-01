package com.demo.controller.commands;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class CalculatorCommand {
    @NotNull
    private BigDecimal amount;

    @NotNull
    private String currencyCode;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
