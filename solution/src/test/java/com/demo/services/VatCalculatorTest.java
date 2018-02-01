package com.demo.services;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class VatCalculatorTest {

    @Test
    public void testCalculateTax() {
        BigDecimal vat = VatCalculator.calculateVAT(new BigDecimal("200.00"), new BigDecimal("20"));
        Assert.assertTrue(vat.equals(new BigDecimal("33.33")));
    }

}