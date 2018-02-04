package com.demo.services.impl;

import static org.mockito.BDDMockito.given;

import com.demo.TestUtils;
import com.demo.domain.ConversionResult;
import com.demo.domain.Money;
import com.demo.services.api.ConversionManager;
import com.demo.services.api.ExchangeRatesManager;
import com.demo.services.api.ServiceException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConversionManagerTest {


    public static final String OK_DATE = "2018-01-01";
    public static final String NOT_OK_DATE = "1900-01-01";
    @MockBean
    private ExchangeRatesManager exchangeRatesManager;

    @Autowired
    private ConversionManager conversionManager;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Before
    public void setUp() throws ParseException, IOException {

        final String okDate = OK_DATE;
        final String notOkDate = NOT_OK_DATE;
        given(this.exchangeRatesManager.findExchangeRates(Matchers.eq(sdf.parse(okDate))))
                .willReturn(TestUtils.prepareMockExchangeRates());
        given(this.exchangeRatesManager.findExchangeRates(Matchers.eq(sdf.parse(notOkDate)))).willThrow(
                ServiceException.class);
    }

    @Test
    public void testOkConversion() throws Exception {
        final ConversionResult conversionResult = conversionManager
                .convertToDomesticAmount(new Money(new BigDecimal("100.66"), "EUR"), sdf.parse(OK_DATE));
        Assert.assertNotNull(conversionResult);
        Assert.assertNotNull(conversionResult.getAmount());
        Assert.assertNotNull(conversionResult.getRate());
        Assert.assertNotNull(conversionResult.getDomesticAmount());

        Assert.assertTrue(conversionResult.getDomesticAmount().getAmount().compareTo(new BigDecimal("114.48")) == 0);
    }

    @Test(expected = ServiceException.class)
    public void testFailConversion() throws Exception {
        final ConversionResult conversionResult = conversionManager
                .convertToDomesticAmount(new Money(new BigDecimal("100.00"), "EUR"), sdf.parse(NOT_OK_DATE));

    }


}