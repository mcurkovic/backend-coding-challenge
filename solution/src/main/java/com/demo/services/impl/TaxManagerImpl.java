package com.demo.services.impl;

import com.demo.domain.Money;
import com.demo.services.api.TaxManager;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TaxManagerImpl implements TaxManager {

    private final String defaultCurrencyCode;
    private final String taxRatePercent;

    public TaxManagerImpl(@Value("${defaultCurrencyCode}") String defaultCurrencyCode,
            @Value("${taxRatePercent}") String taxRatePercent)
    {
        this.defaultCurrencyCode = defaultCurrencyCode;
        this.taxRatePercent = taxRatePercent;
    }

    @Override
    public Money calculateTaxAmount(final Money grossAmount) {
        return new Money(calculateVAT(grossAmount.getAmount(), new BigDecimal(taxRatePercent)),
                defaultCurrencyCode);
    }

    private BigDecimal calculateVAT(final BigDecimal grossAmount, BigDecimal taxRate) {
        final BigDecimal tax = taxRate.divide(new BigDecimal("100.00")).add(new BigDecimal("1.00"));
        final BigDecimal netAmount = grossAmount.divide(tax, 2, BigDecimal.ROUND_HALF_UP);
        final BigDecimal vatAmount = grossAmount.subtract(netAmount);
        return vatAmount;
    }
}
