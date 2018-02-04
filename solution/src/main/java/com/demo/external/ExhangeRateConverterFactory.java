package com.demo.external;


import com.demo.domain.ExchangeRates;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ExhangeRateConverterFactory extends Converter.Factory {


    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return JsonConverter.INSTANCE;
    }

    final static class JsonConverter implements Converter<ResponseBody, ExchangeRates> {

        static final JsonConverter INSTANCE = new JsonConverter();

        @Override
        public ExchangeRates convert(ResponseBody responseBody) throws IOException {

            try {
                return JsonHelper.readJson(responseBody.string(), objectMapper);

            } catch (Exception e) {
                throw new IOException("Failed to parse JSON", e);
            }
        }
    }
}