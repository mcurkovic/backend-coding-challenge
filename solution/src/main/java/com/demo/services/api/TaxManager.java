package com.demo.services.api;

import com.demo.domain.Money;

public interface TaxManager {

    Money calculateTaxAmount(final Money grossAmount);
}

