package com.demo.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "EXPENSE_DATE")
    private Date expenseDate;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "AMOUNT")),
            @AttributeOverride(name = "currency", column = @Column(name = "AMOUNT_CURRENCY_CODE"))
    })

    private Money amount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "DOMESTIC_AMOUNT")),
            @AttributeOverride(name = "currency", column = @Column(name = "DOMESTIC_AMOUNT_CURRENCY_CODE"))
    })
    private Money domesticAmount;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "TAX_AMOUNT")),
            @AttributeOverride(name = "currency", column = @Column(name = "TAX_AMOUNT_CURRENCY_CODE"))
    })
    private Money taxAmount;
    @Column(name = "TAX_RATE")
    private BigDecimal taxRate;
    @Column(name = "EXCHANGE_RATE")
    private BigDecimal exchangeRate;
    @Column(name = "EXCHANGE_RATE_DATE")
    private Date exchangeRateDate;
    @Column(name = "REASON", length = 300)
    private String reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

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

    public Money getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Money taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Date getExchangeRateDate() {
        return exchangeRateDate;
    }

    public void setExchangeRateDate(Date exchangeRateDate) {
        this.exchangeRateDate = exchangeRateDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
