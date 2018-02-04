package com.demo.services.external;

import com.demo.domain.ExchangeRates;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FixerExchangeRatesService {

    @GET("{date}")
    Call<ExchangeRates> findExchangeRates(@Path("date") String date, @Query("base") String currencyCode);
}


