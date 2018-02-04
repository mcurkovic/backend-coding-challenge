package com.demo.services.external;

import com.demo.domain.ExchangeRates;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Helper class used by @{@link ExchangeRatesConverterFactory}
 */
public class FixerExchangeRateParser {

    public static ExchangeRates readJson(String json, ObjectMapper objectMapper) throws IOException, ParseException {
        final JsonNode jsonNode = objectMapper.readTree(json);
        final ExchangeRates exchangeRate = new ExchangeRates();
        exchangeRate.setBase(jsonNode.get("base").textValue());

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        exchangeRate.setDate(sdf.parse(jsonNode.get("date").textValue()));

        final Iterator<Entry<String, JsonNode>> jsonRates = jsonNode.get("rates").fields();
        final Map<String, BigDecimal> rates = new HashMap<>();
        while (jsonRates.hasNext()) {
            final Entry<String, JsonNode> next = jsonRates.next();
            rates.put(next.getKey(), new BigDecimal(next.getValue().toString()));
        }

        exchangeRate.setRates(rates);
        return exchangeRate;
    }
}
