package com.demo.services.impl;

import com.demo.domain.Money;
import com.demo.services.api.TaxManager;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaxManagerImplTest {
    @Autowired
    private TaxManager taxManager;

    @Test
    public void calculateTaxAmount() throws Exception {
        final Money grossAmount = new Money(new BigDecimal("114.58"), "GBP");
        final Money taxAmount = taxManager.calculateTaxAmount(grossAmount);
        Assert.assertNotNull(taxAmount);
        Assert.assertTrue(taxAmount.getAmount().compareTo(new BigDecimal("19.10")) == 0);
    }

}