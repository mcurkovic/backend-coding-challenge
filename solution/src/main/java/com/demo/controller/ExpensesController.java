package com.demo.controller;

import com.demo.domain.ExchangeRates;
import com.demo.domain.Expense;
import com.demo.domain.Money;
import com.demo.services.ExchangeRatesManager;
import com.demo.services.ExpensesManager;
import com.demo.services.VatCalculator;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class ExpensesController {

    @Value("${defaultCurrencyCode}")
    private String defaultCurrencyCode;

    @Value("${taxRatePercent}")
    private String taxRatePercent;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");

    @Autowired
    private ExpensesManager expensesManager;

    @Autowired
    private ExchangeRatesManager exchangeRatesManager;

    @RequestMapping(value = "/expenses", method = RequestMethod.POST)
    public void saveExpense(@Valid @RequestBody ExpenseCommand command) {
        final Expense expense = new Expense();

        Date expenseDate = null;
        try {
            expenseDate = prepareExpenseDate(command);
        } catch (ParseException e) {
            throw new IllegalStateException("Error parsing expense date", e);
        }

        final ExchangeRates rates = exchangeRatesManager.findExchangeRates(expenseDate);
        final BigDecimal rate = prepareExchangeRate(rates, command.getCurrencyCode());
        final BigDecimal domesticAmount = rate.multiply(command.getAmount());

        expense.setDomesticAmount(new Money(domesticAmount, defaultCurrencyCode));
        expense.setAmount(new Money(command.getAmount(), command.getCurrencyCode()));
        expense.setTaxAmount(
                new Money(VatCalculator.calculateVAT(command.getAmount(), new BigDecimal(taxRatePercent)),
                        defaultCurrencyCode));
        expense.setExpenseDate(expenseDate);
        expense.setReason(command.getReason());
        expense.setTaxRate(new BigDecimal(taxRatePercent));
        expense.setExchangeRate(rate);
        expense.setExchangeRateDate(expenseDate);
        expense.setUserId(Long.valueOf(1)); //TODO define user abstraction
        expensesManager.saveExpense(expense);


    }

    private BigDecimal prepareExchangeRate(final ExchangeRates rates, final String currencyCode) {
        BigDecimal rate = new BigDecimal("1.00");
        if (defaultCurrencyCode.equals(currencyCode)) {
            rate = rates.getRates().get(currencyCode);
        }
        return rate;
    }

    private Date prepareExpenseDate(@Valid @RequestBody ExpenseCommand command) throws ParseException {
        return simpleDateFormat.parse(command.getDate());
    }

    @RequestMapping(value = "/expenses", method = RequestMethod.GET)
    public List<Expense> findExpenses() {
        return expensesManager.findExpenses();
    }

    @RequestMapping(value = "/rates", method = RequestMethod.GET)
    public ExchangeRates findExchangeRates(Date date) {
        return exchangeRatesManager.findExchangeRates(date);
    }
}
