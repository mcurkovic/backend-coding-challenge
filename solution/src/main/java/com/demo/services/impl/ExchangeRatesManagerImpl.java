package com.demo.services.impl;

import com.demo.domain.ExchangeRates;
import com.demo.services.api.ExchangeRatesManager;
import com.demo.services.api.ServiceException;
import com.demo.services.external.FixerExchangeRatesService;
import java.io.IOException;
import java.util.Currency;
import java.util.Date;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import retrofit2.Call;
import retrofit2.Response;

@Service
public class ExchangeRatesManagerImpl implements ExchangeRatesManager {

    private final String defaultCurrencyCode;

    private final FastDateFormat dateFormatter;

    private final FixerExchangeRatesService fixerExchangeRatesService;

    @Autowired
    public ExchangeRatesManagerImpl(
            final @Value("${defaultCurrencyCode}") String defaultCurrencyCode,
            final @Value("${fixer.dateFormat}") String fixerDateFormat,
            final FixerExchangeRatesService fixerExchangeRatesService) {
        Assert.hasText(defaultCurrencyCode, "defaultCurrencyCode must be defined. Check application.properties config");
        Assert.hasText(fixerDateFormat, "fixer.dateFormat must be defined. Check application.properties config");

        //validate configured default currency code
        try {
            Assert.notNull(Currency.getInstance(defaultCurrencyCode).getCurrencyCode(), "Invalid currency code");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Check application.properties defaultCurrencyCode. Unrecognized currency code=" + defaultCurrencyCode);
        }

        this.defaultCurrencyCode = defaultCurrencyCode;
        this.fixerExchangeRatesService = fixerExchangeRatesService;
        this.dateFormatter = FastDateFormat.getInstance(fixerDateFormat);
    }

    //cache result - see keyGenerator class for cache key/ for demo purpososes used spring boot generic caching
    @Cacheable(value = "rates", keyGenerator = "exchangeRatesKeyGenerator")
    @Override
    public ExchangeRates findExchangeRates(final Date date) {
        Assert.notNull(date, "Date param must not be null!");
        final String exchangeRateDate = dateFormatter.format(date);

        //validate default currency code
        final Call<ExchangeRates> call = fixerExchangeRatesService.findExchangeRates(exchangeRateDate, defaultCurrencyCode);

        final String errMsg = "Exchange rate not available.";
        try {
            final Response<ExchangeRates> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            throw new ServiceException(errMsg);
        } catch (IOException e) {
            throw new ServiceException(errMsg);
        }
    }
}

