package com.demo.controller.validators;

import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExpenseDateValidator implements ConstraintValidator<ExpenseDate, Date> {

    @Override
    public void initialize(ExpenseDate constraintAnnotation) {

    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value != null) {
            return value.compareTo(new Date()) <= 0;
        }
        return true;
    }
}
