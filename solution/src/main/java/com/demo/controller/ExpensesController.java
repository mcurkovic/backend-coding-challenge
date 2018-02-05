package com.demo.controller;

import com.demo.controller.dto.CalculatorDTO;
import com.demo.controller.dto.ExpenseDTO;
import com.demo.domain.ConversionResult;
import com.demo.domain.Expense;
import com.demo.domain.Money;
import com.demo.services.api.ConversionManager;
import com.demo.services.api.ExpensesManager;
import com.demo.services.api.TaxManager;
import javax.validation.Valid;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class ExpensesController {

    private final ExpensesManager expensesManager;

    private final TaxManager taxManager;

    private final ConversionManager conversionManager;

    //orika mapper used for mapping DTO to Entity and vice versa.
    //For mappers injection see @com.demo.utils.SpringConfigurableMapper
    private final MapperFacade mapperFacade;

    public ExpensesController(ExpensesManager expensesManager, TaxManager taxManager,
            ConversionManager conversionManager, MapperFacade mapperFacade) {
        this.expensesManager = expensesManager;
        this.taxManager = taxManager;
        this.conversionManager = conversionManager;
        this.mapperFacade = mapperFacade;
    }

    @RequestMapping(value = "/expenses", method = RequestMethod.POST)
    public ExpenseDTO saveExpense(@Valid @RequestBody ExpenseDTO command) {
        final Expense expense = mapperFacade.map(command, Expense.class);
        final Expense savedExpense = expensesManager.saveExpense(expense);
        final ExpenseDTO populatedDTO = mapperFacade.map(savedExpense, ExpenseDTO.class);
        return command;
    }


    @RequestMapping(value = "/expenses", method = RequestMethod.GET)
    public Iterable<Expense> findExpenses() {
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
