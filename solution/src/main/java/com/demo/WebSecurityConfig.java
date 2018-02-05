package com.demo;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Web security configuration. Enabled CORS and basic auth. Disabled CSRF filter
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //enable CORS and disable CSRF for REST api
        http.cors().and().csrf().disable();

        //configure basic authentication explicitly for /app/** requests. all other disabled
        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/app/**")
                .authenticated().anyRequest().not().permitAll();



    }
}
