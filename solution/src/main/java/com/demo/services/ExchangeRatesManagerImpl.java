package com.demo.services;

import com.demo.domain.ExchangeRates;
import com.demo.external.FixerExchangeRatesService;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

@Service
public class ExchangeRatesManagerImpl implements ExchangeRatesManager {

    @Value("${defaultCurrencyCode}")
    private String defaultCurrencyCode;

    @Autowired
    private FixerExchangeRatesService fixerExchangeRatesService;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //cache result - see keyGenerator class for cache key
    @Cacheable(value = "rates", keyGenerator = "exchangeRatesKeyGenerator")
    @Override
    public ExchangeRates findExchangeRates(Date date) {
        final String exchangeRateDate = simpleDateFormat.format(date);

        //validate default currency code
        final String currencyCode = Currency.getInstance(defaultCurrencyCode).getCurrencyCode();

        final Call<ExchangeRates> call = fixerExchangeRatesService.findExchangeRates(exchangeRateDate, currencyCode);

        try {
            final Response<ExchangeRates> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                return new ExchangeRates();
            }
        } catch (IOException e) {
            throw new IllegalStateException("fixer service error:", e);
        }

    }
}
