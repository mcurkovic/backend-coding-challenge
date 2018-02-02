package com.demo.controller.dto;

import com.demo.controller.validators.ExpenseDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class ExpenseDTO {

    @NotNull
    private BigDecimal amount;

    private BigDecimal taxAmount;

    private String currencyCode;

    private BigDecimal domesticCurrencyAmount;

    @Length(max = 300)
    private String reason;

    @NotNull
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
    @ExpenseDate
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
