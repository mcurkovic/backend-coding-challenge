package com.demo;

import com.demo.domain.ExchangeRates;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public class TestUtils {

    public static ExchangeRates prepareMockExchangeRates() {
        final ExchangeRates mockExhangeRates = new ExchangeRates();
        mockExhangeRates.setBase("GBP");
        mockExhangeRates.setDate(new Date());
        HashMap<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("1.1373"));
        mockExhangeRates.setRates(rates);
        return mockExhangeRates;
    }

}
