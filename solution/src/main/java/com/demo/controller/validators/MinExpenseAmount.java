package com.demo.controller.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinExpenseAmountValidator.class)
public @interface MinExpenseAmount {

    //set the key of message. Validation message should be localized. See src/main/ValidationMessages.properties
    String message() default "{invalid.expense.amount.minValue}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
