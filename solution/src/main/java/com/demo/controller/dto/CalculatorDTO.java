package com.demo.controller.dto;

import com.demo.controller.validators.ExpenseDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;

public class CalculatorDTO {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String currencyCode;

    @NotNull
    @ExpenseDate
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
    private Date date;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
