package com.demo.controller;

import com.demo.controller.dto.CalculatorDTO;
import com.demo.controller.dto.ExpenseDTO;
import com.demo.domain.ConversionResult;
import com.demo.domain.Expense;
import com.demo.domain.Money;
import com.demo.services.api.ConversionManager;
import com.demo.services.api.ExpensesManager;
import com.demo.services.api.TaxManager;
import java.util.List;
import javax.validation.Valid;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
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
    //For mappers injection see @com.demo.SpringConfigurableMapper
    private final MapperFacade mapperFacade;

    @Autowired
    public ExpensesController(ExpensesManager expensesManager, TaxManager taxManager,
            ConversionManager conversionManager, MapperFacade mapperFacade) {
        this.expensesManager = expensesManager;
        this.taxManager = taxManager;
        this.conversionManager = conversionManager;
        this.mapperFacade = mapperFacade;
    }

    @RequestMapping(value = "/expenses", method = RequestMethod.POST)
    public ExpenseDTO saveExpense(@Valid @RequestBody ExpenseDTO command) {
        //map DTO object to Entity
        final Expense expense = mapperFacade.map(command, Expense.class);
        //save Entity object
        final Expense savedExpense = expensesManager.saveExpense(expense);

        //map entity back to DTO
        final ExpenseDTO populatedDTO = mapperFacade.map(savedExpense, ExpenseDTO.class);

        //return DTO
        return populatedDTO;
    }


    @RequestMapping(value = "/expenses", method = RequestMethod.GET)
    public List<ExpenseDTO> findExpenses() {
        final List<ExpenseDTO> expenseDTOS = mapperFacade.mapAsList(expensesManager.findExpenses(), ExpenseDTO.class);
        return expenseDTOS;
    }

    @RequestMapping(value = "/calculator", method = RequestMethod.POST)
    public Money calculate(@Valid @RequestBody CalculatorDTO command) {
        final Money amount = new Money(command.getAmount(), command.getCurrencyCode());
        final ConversionResult conversionResult = conversionManager
                .convertToDomesticAmount(amount, command.getDate());
        return taxManager.calculateTaxAmount(conversionResult.getDomesticAmount());
    }


}
