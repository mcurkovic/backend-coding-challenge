package com.demo.services.impl;

import com.demo.domain.ConversionResult;
import com.demo.domain.ExchangeRates;
import com.demo.domain.Money;
import com.demo.services.api.ConversionManager;
import com.demo.services.api.ExchangeRatesManager;
import com.demo.services.api.ServiceException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConversionManagerImpl implements ConversionManager {

    private final String defaultCurrencyCode;

    private final ExchangeRatesManager exchangeRatesManager;

    @Autowired
    public ConversionManagerImpl(final @Value("${defaultCurrencyCode}") String defaultCurrencyCode,
            final ExchangeRatesManager exchangeRatesManager) {
        this.defaultCurrencyCode = defaultCurrencyCode;
        this.exchangeRatesManager = exchangeRatesManager;
    }

    @Override
    public ConversionResult convertToDomesticAmount(final Money amount, final Date exchangeRateDate) {
        final ConversionResult conversionResult = new ConversionResult();
        conversionResult.setAmount(amount);

        if (defaultCurrencyCode.equals(amount.getCurrency())) {
            conversionResult.setDomesticAmount(amount);
            return conversionResult;
        }

        final BigDecimal rate = fetchExchangeRate(exchangeRateDate, amount.getCurrency());
        final BigDecimal calculatedAmount = rate.multiply(amount.getAmount()).setScale(2, RoundingMode.HALF_UP);
        conversionResult.setDomesticAmount(new Money(calculatedAmount, defaultCurrencyCode));
        conversionResult.setRate(rate);

        return conversionResult;
    }

    private BigDecimal fetchExchangeRate(final Date date, final String currencyCode) {
        if (defaultCurrencyCode.equals(currencyCode)) {
            throw new IllegalArgumentException("Currency code equals domestic currency code");
        }
        final ExchangeRates rates = exchangeRatesManager.findExchangeRates(date);
        if (rates.getRates() == null || rates.getRates().containsKey(currencyCode) == false) {
            throw new ServiceException("Exchange rate not available");
        }
        final BigDecimal rate = rates.getRates().get(currencyCode);
        return rate;
    }
}
