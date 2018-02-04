package com.demo.controller;

import com.demo.controller.dto.CalculatorDTO;
import com.demo.controller.dto.ExpenseDTO;
import com.demo.domain.ConversionResult;
import com.demo.domain.Expense;
import com.demo.domain.Money;
import com.demo.services.api.ConversionManager;
import com.demo.services.api.ExchangeRatesManager;
import com.demo.services.api.ExpensesManager;
import com.demo.services.api.TaxManager;
import java.math.BigDecimal;
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
    //see application.properties taxRatePercent
    private final BigDecimal taxRatePercent;

    private final ExpensesManager expensesManager;

    private final ExchangeRatesManager exchangeRatesManager;

    private final TaxManager taxManager;

    private final ConversionManager conversionManager;

    @Autowired
    public ExpensesController(@Value("${taxRatePercent}") String taxRatePercent, ExpensesManager expensesManager,
            ExchangeRatesManager exchangeRatesManager, TaxManager taxManager, ConversionManager conversionManager) {

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

    @RequestMapping(value = "/expenses", method = RequestMethod.POST)
    public ExpenseDTO saveExpense(@Valid @RequestBody ExpenseDTO command) {

        //calculate domestic amount and tax
        final Money amount = new Money(command.getAmount(), command.getCurrencyCode());
        final ConversionResult conversionResult = conversionManager.convertToDomesticAmount(amount, command.getDate());
        final Money taxAmount = taxManager.calculateTaxAmount(conversionResult.getDomesticAmount());
        final Expense expense = convertToExpense(command, amount, conversionResult, taxAmount);
        final Expense savedExpense = expensesManager.saveExpense(expense);

        //return populated command
        return command;
    }

    private Expense convertToExpense(ExpenseDTO command, Money amount,
            ConversionResult conversionResult, Money taxAmount)
    {
        //map dto to entity - usually use library such as Orika Mapper, for demo in controller.
        final Expense expense = new Expense();
        expense.setDomesticAmount(conversionResult.getDomesticAmount());
        expense.setAmount(amount);
        expense.setTaxAmount(taxAmount);
        expense.setExpenseDate(command.getDate());
        expense.setReason(command.getReason());
        expense.setTaxRate(taxRatePercent);
        expense.setExchangeRate(conversionResult.getRate());
        expense.setExchangeRateDate(command.getDate());
        expense.setUserId(Long.valueOf(1));
        return expense;
    }


    @RequestMapping(value = "/expenses", method = RequestMethod.GET)
    public List<Expense> findExpenses() {
        return expensesManager.findExpenses();
    }

    @RequestMapping(value = "/calculator", method = RequestMethod.POST)
    public Money calculate(@Valid @RequestBody CalculatorDTO command) {
        final Money amount = new Money(command.getAmount(), command.getCurrencyCode());
        final ConversionResult conversionResult = conversionManager
                .convertToDomesticAmount(amount, command.getDate());
        return taxManager.calculateTaxAmount(conversionResult.getDomesticAmount());
    }


}
