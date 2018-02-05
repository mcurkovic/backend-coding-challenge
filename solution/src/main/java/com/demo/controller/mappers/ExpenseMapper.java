package com.demo.controller.mappers;

import com.demo.controller.dto.ExpenseDTO;
import com.demo.domain.ConversionResult;
import com.demo.domain.Expense;
import com.demo.domain.Money;
import com.demo.services.api.ConversionManager;
import com.demo.services.api.ExchangeRatesManager;
import com.demo.services.api.ExpensesManager;
import com.demo.services.api.TaxManager;
import java.math.BigDecimal;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper extends CustomMapper<ExpenseDTO, Expense> {
    private final BigDecimal taxRatePercent;

    private final ExpensesManager expensesManager;

    private final ExchangeRatesManager exchangeRatesManager;

    private final TaxManager taxManager;

    private final ConversionManager conversionManager;


    @Autowired
    public ExpenseMapper(@Value("${taxRatePercent}") String taxRatePercent, ExpensesManager expensesManager,
            ExchangeRatesManager exchangeRatesManager, TaxManager taxManager,
            ConversionManager conversionManager)
    {
        try {
            final BigDecimal taxRate = new BigDecimal(taxRatePercent);
            this.taxRatePercent = taxRate;
        } catch (Exception e) {
            throw new IllegalArgumentException("taxRatePercent misconfigured. Use format 00.00 e.g. 20.00");
        }

        this.expensesManager = expensesManager;
        this.exchangeRatesManager = exchangeRatesManager;
        this.taxManager = taxManager;
        this.conversionManager = conversionManager;
    }

    @Override
    public void mapAtoB(ExpenseDTO expenseDTO, Expense expense, MappingContext context) {
        final Money amount = new Money(expenseDTO.getAmount(), expenseDTO.getCurrencyCode());
        final ConversionResult conversionResult = conversionManager.convertToDomesticAmount(amount, expenseDTO.getDate());
        final Money taxAmount = taxManager.calculateTaxAmount(conversionResult.getDomesticAmount());
        expense.setDomesticAmount(conversionResult.getDomesticAmount());
        expense.setAmount(amount);
        expense.setTaxAmount(taxAmount);
        expense.setExpenseDate(expenseDTO.getDate());
        expense.setReason(expenseDTO.getReason());
        expense.setTaxRate(taxRatePercent);
        expense.setExchangeRate(conversionResult.getRate());
        expense.setExchangeRateDate(expenseDTO.getDate());
        expense.setUserId(Long.valueOf(1));
        expense.setId(expenseDTO.getId());
    }

    @Override
    public void mapBtoA(Expense expense, ExpenseDTO expenseDTO, MappingContext context) {
        expenseDTO.setId(expense.getId());
        expenseDTO.setCurrencyCode(expense.getAmount().getCurrency());
        expenseDTO.setDate(expense.getExpenseDate());
        expenseDTO.setDisplayDate(expenseDTO.getDate());
        expenseDTO.setReason(expense.getReason());
        expenseDTO.setAmount(expense.getAmount().getAmount());
        expenseDTO.setTaxAmount(expense.getTaxAmount().getAmount());
        expenseDTO.setDomesticCurrencyAmount(expense.getDomesticAmount().getAmount());
       }
}
