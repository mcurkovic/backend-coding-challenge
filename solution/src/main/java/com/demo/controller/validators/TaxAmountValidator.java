package com.demo.controller.validators;

import com.demo.controller.dto.ExpenseDTO;
import com.demo.domain.ConversionResult;
import com.demo.domain.Money;
import com.demo.services.ExchangeRatesManager;
import com.demo.services.TaxManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class TaxAmountValidator implements ConstraintValidator<TaxAmount, ExpenseDTO> {

    private final TaxManager taxManager;

    private final ExchangeRatesManager exchangeRatesManager;

    @Autowired
    public TaxAmountValidator(TaxManager taxManager, ExchangeRatesManager exchangeRatesManager) {
        this.taxManager = taxManager;
        this.exchangeRatesManager = exchangeRatesManager;
    }

    @Override
    public void initialize(TaxAmount constraintAnnotation) {

    }

    @Override
    public boolean isValid(ExpenseDTO value, ConstraintValidatorContext context) {
        final Money amount = new Money(value.getAmount(), value.getCurrencyCode());
        final ConversionResult conversionResult = exchangeRatesManager.calculateDomesticAmount(amount, value.getDate());
        final Money calculateTaxAmount = taxManager.calculateTaxAmount(conversionResult.getDomesticAmount());
        return value.getTaxAmount().compareTo(calculateTaxAmount.getAmount()) == 0;
    }
}
