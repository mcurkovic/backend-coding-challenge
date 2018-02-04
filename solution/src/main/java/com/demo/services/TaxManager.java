package com.demo.services;

import com.demo.domain.Money;

public interface TaxManager {

    Money calculateTaxAmount(final Money grossAmount);
}

