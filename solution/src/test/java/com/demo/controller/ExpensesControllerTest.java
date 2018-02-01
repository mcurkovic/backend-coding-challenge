package com.demo.controller;


import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.controller.commands.CalculatorCommand;
import com.demo.controller.commands.ExpenseCommand;
import com.demo.domain.ExchangeRates;
import com.demo.services.ExchangeRatesManager;
import com.demo.services.ExpensesManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@RunWith(SpringRunner.class)
@WebMvcTest(ExpensesController.class)
public class ExpensesControllerTest {

    public static final String BASIC_AUTH_VALUE = "Basic dXNlcjpwYXNzd29yZA==";
    public static final String BASIC_AUTH_KEY = "Authorization";
    public static final String URI_EXPENSES = "/app/expenses";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ExchangeRatesManager exchangeRatesManager;

    @MockBean
    private ExpensesManager expensesManager;

    @Before
    public void setUp() {
        final ExchangeRates mockExhangeRates = prepareMockExchangeRates();
        given(this.exchangeRatesManager.findExchangeRates(any(Date.class))).willReturn(mockExhangeRates);
    }

    @Test
    public void testSaveOkExpense() throws Exception {
        final ExpenseCommand command = new ExpenseCommand();
        command.setAmount(new BigDecimal("100.00"));
        command.setDate("11/01/2016");
        command.setReason("test");
        command.setTaxAmount(new BigDecimal("16.67"));
        postObject(command, status().isOk());
    }


    @Test
    //test invalid command
    public void testSaveInvalidExpense() throws Exception {

        final ExpenseCommand command = new ExpenseCommand();
        command.setDate("11/01/2016");
        command.setReason("test");
        postObject(command, status().is4xxClientError());
    }

    @Test
    public void testFindExpenses() throws Exception {
        this.mvc.perform(
                get(URI_EXPENSES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(BASIC_AUTH_KEY, BASIC_AUTH_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void testCalculator() throws Exception {
        final CalculatorCommand command = new CalculatorCommand();
        command.setAmount(new BigDecimal("100"));
        command.setCurrencyCode("EUR");
        command.setDate("12/01/2018");

        this.mvc.perform(
                post("/app/calculator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(BASIC_AUTH_KEY, BASIC_AUTH_VALUE)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    private void postObject(ExpenseCommand command, ResultMatcher status) throws Exception {

        this.mvc.perform(
                post(URI_EXPENSES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_PLAIN)
                        .header(BASIC_AUTH_KEY, BASIC_AUTH_VALUE)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status);
    }

    private ExchangeRates prepareMockExchangeRates() {
        final ExchangeRates mockExhangeRates = new ExchangeRates();
        mockExhangeRates.setBase("GBP");
        mockExhangeRates.setDate(new Date());
        HashMap<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("1.1373"));
        mockExhangeRates.setRates(rates);
        return mockExhangeRates;
    }


}