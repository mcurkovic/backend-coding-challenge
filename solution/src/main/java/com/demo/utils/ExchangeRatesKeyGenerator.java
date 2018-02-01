package com.demo.utils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;


@Component
public class ExchangeRatesKeyGenerator implements KeyGenerator {

    private SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params[0] instanceof Date) {
            Date param = (Date) params[0];
            return sdf.format(param);
        }
        throw new IllegalStateException("First param must be of type Date");
    }
}
