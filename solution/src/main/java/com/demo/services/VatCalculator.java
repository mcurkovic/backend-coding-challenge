package com.demo.services;

import java.math.BigDecimal;

public class VatCalculator {

    public static BigDecimal calculateVAT(final BigDecimal grossAmount, BigDecimal taxRate) {
        final BigDecimal tax = taxRate.divide(new BigDecimal("100.00")).add(new BigDecimal("1.00"));
        final BigDecimal netAmount = grossAmount.divide(tax, 2, BigDecimal.ROUND_HALF_EVEN);
        final BigDecimal vatAmount = grossAmount.subtract(netAmount);
        return vatAmount;
    }
}
