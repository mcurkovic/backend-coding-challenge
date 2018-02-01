package com.demo.services;

import com.demo.domain.ExchangeRates;
import java.util.Date;

public interface ExchangeRatesManager {

    ExchangeRates findExchangeRates(Date date);
}
