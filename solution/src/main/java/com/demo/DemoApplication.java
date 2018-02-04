package com.demo;

import com.demo.services.external.ExchangeRatesConverterFactory;
import com.demo.services.external.FixerExchangeRatesService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import retrofit2.Retrofit;

@SpringBootApplication
@EnableCaching
public class DemoApplication {

    @Value("${fixer.url}")
    private String fixerUrl;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/app/**")
                        .allowedOrigins("http://localhost:8080")
                        .allowedMethods("PUT", "POST", "GET", "DELETE")
                        .maxAge(3600);
            }
        };
    }

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient client = new OkHttpClient();
         return client;
    }

    @Bean
    public Retrofit retrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .addConverterFactory(new ExchangeRatesConverterFactory())
                .baseUrl(fixerUrl)
                .client(client)
                .build();
    }

    @Bean
    public FixerExchangeRatesService fixerExchangeRatesService(Retrofit retrofit) {
        return retrofit.create(FixerExchangeRatesService.class);
    }
}
