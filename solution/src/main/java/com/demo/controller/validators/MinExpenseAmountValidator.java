package com.demo.controller.validators;

import java.math.BigDecimal;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinExpenseAmountValidator implements ConstraintValidator<MinExpenseAmount, BigDecimal> {

    private final BigDecimal minimalAmount = new BigDecimal("0.01");

    @Override
    public void initialize(MinExpenseAmount constraintAnnotation) {

    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value != null) {
            return value.compareTo(minimalAmount) >= 0;
        }
        return true;
    }
}
