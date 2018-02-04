package com.demo.services.impl;

import com.demo.domain.ExchangeRates;
import com.demo.services.external.FixerExchangeRatesService;
import com.demo.services.api.ServiceException;
import com.demo.services.api.ExchangeRatesManager;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Date;
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

    private final DateTimeFormatter dateFormatter;

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
            Currency.getInstance(defaultCurrencyCode).getCurrencyCode();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Check application.properties defaultCurrencyCode. Unrecognized currency code=" + defaultCurrencyCode);
        }

        this.defaultCurrencyCode = defaultCurrencyCode;
        this.fixerExchangeRatesService = fixerExchangeRatesService;
        this.dateFormatter = DateTimeFormatter.ofPattern(fixerDateFormat);
    }

    //cache result - see keyGenerator class for cache key/ for demo purpososes used spring boot generic caching
    @Cacheable(value = "rates", keyGenerator = "exchangeRatesKeyGenerator")
    @Override
    public ExchangeRates findExchangeRates(final Date date) {
        Assert.notNull(date, "Date param must not be null!");

        final LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        final String exchangeRateDate = dateFormatter.format(localDateTime);

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

    /*@Override
    public ConversionResult convertToDomesticAmount(final Money amount, Date exhangeRateDate) {
        final ConversionResult conversionResult = new ConversionResult();
        conversionResult.setAmount(amount);

        if (defaultCurrencyCode.equals(amount.getCurrency())) {
            conversionResult.setDomesticAmount(amount);
            return conversionResult;
        }

        final BigDecimal rate = fetchExchangeRate(exhangeRateDate, amount.getCurrency());
        final BigDecimal calculatedAmount = rate.multiply(amount.getAmount(), new MathContext(2, RoundingMode.HALF_UP));
        conversionResult.setDomesticAmount(new Money(calculatedAmount, defaultCurrencyCode));
        conversionResult.setRate(rate);

        return conversionResult;
    }
*/
    /*private BigDecimal fetchExchangeRate(final Date date, final String currencyCode) {
        if (defaultCurrencyCode.equals(currencyCode)) {
            throw new IllegalArgumentException("Currency code equals domestic currency code");
        }
        final ExchangeRates rates = findExchangeRates(date);
        if (rates.getRates() == null || rates.getRates().containsKey(currencyCode) == false){
            throw new ServiceException("Exchange rate not available");
        }
        final BigDecimal rate = rates.getRates().get(currencyCode);
        return rate;
    }*/

}

