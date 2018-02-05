package com.demo.services.external;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.demo.domain.ExchangeRates;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.ParseException;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

public class FixerExchangeRateParserTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testReadJson() throws IOException, ParseException {
        final String json = "{\n"
                + "  \"base\": \"GBP\",\n"
                + "  \"date\": \"2018-01-26\",\n"
                + "  \"rates\": {\n"
                + "    \"AUD\": 1.761,\n"
                + "    \"BGN\": 2.2394,\n"
                + "    \"BRL\": 4.4826,\n"
                + "    \"CAD\": 1.7547,\n"
                + "    \"CHF\": 1.3319,\n"
                + "    \"CNY\": 9.0105,\n"
                + "    \"CZK\": 29.034,\n"
                + "    \"DKK\": 8.5221,\n"
                + "    \"EUR\": 1.145,\n"
                + "    \"HKD\": 11.132,\n"
                + "    \"HRK\": 8.4949,\n"
                + "    \"HUF\": 354.73,\n"
                + "    \"IDR\": 18960,\n"
                + "    \"ILS\": 4.8262,\n"
                + "    \"INR\": 90.578,\n"
                + "    \"JPY\": 155.66,\n"
                + "    \"KRW\": 1515.7,\n"
                + "    \"MXN\": 26.412,\n"
                + "    \"MYR\": 5.5166,\n"
                + "    \"NOK\": 10.953,\n"
                + "    \"NZD\": 1.94,\n"
                + "    \"PHP\": 72.699,\n"
                + "    \"PLN\": 4.7429,\n"
                + "    \"RON\": 5.343,\n"
                + "    \"RUB\": 79.607,\n"
                + "    \"SEK\": 11.222,\n"
                + "    \"SGD\": 1.8617,\n"
                + "    \"THB\": 44.698,\n"
                + "    \"TRY\": 5.3398,\n"
                + "    \"USD\": 1.4239,\n"
                + "    \"ZAR\": 16.933\n"
                + "  }\n"
                + "}";

        final ExchangeRates exchangeRate = FixerExchangeRateParser.readJson(json, objectMapper);
        assertNotNull(exchangeRate);
        assertTrue(!CollectionUtils.isEmpty(exchangeRate.getRates()));
        assertTrue(exchangeRate.getRates().get("SEK") != null);
    }

}
