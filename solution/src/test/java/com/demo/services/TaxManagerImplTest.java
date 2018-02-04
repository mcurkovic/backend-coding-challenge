package com.demo.services;

import static org.junit.Assert.*;

import com.demo.domain.Money;
import java.math.BigDecimal;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TaxManagerImplTest {
    @Autowired
    private TaxManager taxManager;

    @Test
    public void calculateTaxAmount() throws Exception {
        final Money amount = new Money(new BigDecimal("100.00"), "GBP");

    }

}