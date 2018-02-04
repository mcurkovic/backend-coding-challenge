package com.demo.services;

import com.demo.domain.ConversionResult;
import com.demo.domain.ExchangeRates;
import com.demo.domain.Money;
import com.demo.external.FixerExchangeRatesService;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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

    @Override
    public ConversionResult calculateDomesticAmount(final Money amount, Date exhangeRateDate) {
        final ConversionResult conversionResult = new ConversionResult();
        conversionResult.setAmount(amount);
        if (defaultCurrencyCode.equals(amount.getCurrency())){
            conversionResult.setDomesticAmount(amount);
            return conversionResult;
        }
        final BigDecimal rate = fetchExchangeRate(exhangeRateDate, amount.getCurrency());
        final BigDecimal calculatedAmount = rate.multiply(amount.getAmount(), new MathContext(2, RoundingMode.HALF_UP));
        conversionResult.setDomesticAmount(new Money(calculatedAmount, defaultCurrencyCode));
        conversionResult.setRate(rate);
        return conversionResult;
    }

    private BigDecimal fetchExchangeRate(final Date date, final String currencyCode) {
        final ExchangeRates rates = findExchangeRates(date);
        BigDecimal rate = new BigDecimal("1.00");
        if (!defaultCurrencyCode.equals(currencyCode)) {
            rate = rates.getRates().get(currencyCode);
        }
        return rate;
    }

}

