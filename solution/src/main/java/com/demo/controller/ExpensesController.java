package com.demo.controller;

import com.demo.controller.commands.CalculatorCommand;
import com.demo.controller.commands.ExpenseCommand;
import com.demo.domain.ExchangeRates;
import com.demo.domain.Expense;
import com.demo.domain.Money;
import com.demo.services.ExchangeRatesManager;
import com.demo.services.ExpensesManager;
import com.demo.services.VatCalculator;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
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
        //prepare input values
        final Date expenseDate = parseDate(command.getDate());
        final String currencyCode = prepareCurrencyCode(command.getCurrencyCode());
        final BigDecimal rate = fetchExchangeRate(expenseDate, currencyCode);
        final BigDecimal domesticAmount = calculateDomesticAmount(command.getAmount(), rate);
        final Money taxAmount = calculateTaxAmount(domesticAmount);

        Assert.isTrue(taxAmount.getAmount().compareTo(command.getTaxAmount()) == 0, "Tax amount not valid!");

        final Expense expense = new Expense();
        expense.setDomesticAmount(new Money(domesticAmount, defaultCurrencyCode));
        expense.setAmount(new Money(command.getAmount(), currencyCode));
        expense.setTaxAmount(taxAmount);
        expense.setExpenseDate(expenseDate);
        expense.setReason(command.getReason());

        Assert.hasText(taxRatePercent, "taxRatePercent not defined, check application.properties taxRatePercent entry");
        expense.setTaxRate(new BigDecimal(taxRatePercent));

        expense.setExchangeRate(rate);
        expense.setExchangeRateDate(expenseDate);
        expense.setUserId(Long.valueOf(1)); //TODO define user abstraction
        expensesManager.saveExpense(expense);
    }

    private BigDecimal calculateDomesticAmount(final BigDecimal amount, BigDecimal rate) {
        return rate.multiply(amount, new MathContext(2, RoundingMode.HALF_UP));
    }

    private Money calculateTaxAmount(final BigDecimal grossAmount) {
        return new Money(VatCalculator.calculateVAT(grossAmount, new BigDecimal(taxRatePercent)),
                defaultCurrencyCode);
    }

    private String prepareCurrencyCode(final String currencyCode) {
        if (StringUtils.hasText(currencyCode)) {
            return currencyCode.toUpperCase();
        }
        return defaultCurrencyCode;
    }

    private Date parseDate(final String date) {
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new IllegalStateException("Error parsing expense date", e);
        }

    }

    private BigDecimal fetchExchangeRate(final Date date, final String currencyCode) {
        final ExchangeRates rates = exchangeRatesManager.findExchangeRates(date);
        BigDecimal rate = new BigDecimal("1.00");
        if (!defaultCurrencyCode.equals(currencyCode)) {
            rate = rates.getRates().get(currencyCode);
        }
        return rate;
    }

    @RequestMapping(value = "/expenses", method = RequestMethod.GET)
    public List<Expense> findExpenses() {
        return expensesManager.findExpenses();
    }

    @RequestMapping(value = "/calculator", method = RequestMethod.POST)
    public Money calculate(@Valid @RequestBody CalculatorCommand command) {
        final Date expenseDate = parseDate(command.getDate());
        final String currencyCode = prepareCurrencyCode(command.getCurrencyCode());
        final BigDecimal rate = fetchExchangeRate(expenseDate, currencyCode);
        final BigDecimal domesticAmount = calculateDomesticAmount(command.getAmount(), rate);
        final Money taxAmount = calculateTaxAmount(domesticAmount);
        return taxAmount;
    }


}
