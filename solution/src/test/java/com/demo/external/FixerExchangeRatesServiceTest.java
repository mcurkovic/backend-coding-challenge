package com.demo.external;

import static org.junit.Assert.assertNotNull;

import com.demo.DemoApplication;
import com.demo.domain.ExchangeRates;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Call;
import retrofit2.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = DemoApplication.class)
public class FixerExchangeRatesServiceTest {

    @Autowired
    private FixerExchangeRatesService fixerExchangeRatesService;

    @Test
    public void testFindLatestExchangeRates() {
        Call<ExchangeRates> call = fixerExchangeRatesService.findExchangeRates("latest", "GBP");
        try {
            Response<ExchangeRates> response = call.execute();

            if (response.isSuccessful()) {
                ExchangeRates rates = response.body();
                assertNotNull(rates);
            } else {
                assertNotNull(response.body());
            }
        } catch (IOException e) {
            throw new IllegalStateException("fixer service error:", e);
        }
    }



}