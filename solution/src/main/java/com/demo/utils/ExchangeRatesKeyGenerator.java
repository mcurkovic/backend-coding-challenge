package com.demo.utils;

import java.lang.reflect.Method;
import java.util.Date;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;


@Component
public class ExchangeRatesKeyGenerator implements KeyGenerator {

    private FastDateFormat sdf = FastDateFormat.getInstance("ddMMyyyy");

    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params[0] instanceof Date) {
            Date param = (Date) params[0];
            return sdf.format(param);
        }
        throw new IllegalStateException("First param must be of type Date");
    }
}
