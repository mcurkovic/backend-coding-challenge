package com.demo.services.api;

import com.demo.domain.ConversionResult;
import com.demo.domain.ExchangeRates;
import com.demo.domain.Money;
import java.util.Date;

public interface ExchangeRatesManager {

    ExchangeRates findExchangeRates(Date date);

    //ConversionResult convertToDomesticAmount(Money amount, Date exchangeRateDate);
}

