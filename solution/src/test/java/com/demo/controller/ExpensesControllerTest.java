package com.demo.controller;


import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.TestUtils;
import com.demo.controller.dto.CalculatorDTO;
import com.demo.controller.dto.ExpenseDTO;
import com.demo.domain.ConversionResult;
import com.demo.domain.Expense;
import com.demo.domain.Money;
import com.demo.services.api.ConversionManager;
import com.demo.services.api.ExchangeRatesManager;
import com.demo.services.api.ExpensesManager;
import com.demo.services.api.TaxManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(ExpensesController.class)
public class ExpensesControllerTest {

    private static final String BASIC_AUTH_VALUE = "Basic dXNlcjpwYXNzd29yZA==";
    private static final String BASIC_AUTH_KEY = "Authorization";
    private static final String URI_EXPENSES = "/app/expenses";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ExchangeRatesManager exchangeRatesManager;

    @MockBean
    private ConversionManager conversionManager;

    @MockBean
    private ExpensesManager expensesManager;

    @MockBean
    private TaxManager taxManager;

    @Before
    public void setUp() {

        given(this.exchangeRatesManager.findExchangeRates(any(Date.class)))
                .willReturn(TestUtils.prepareMockExchangeRates());
        given(this.taxManager.calculateTaxAmount(any(Money.class)))
                .willReturn(new Money(new BigDecimal("100"), "GBP"));
        final Expense mockeExpense = prepareMockExpense();
        given(this.expensesManager.findExpenses()).willReturn(Arrays.asList(mockeExpense));
        given(this.conversionManager.convertToDomesticAmount(any(Money.class), any(Date.class)))
                .willReturn(new ConversionResult());
    }

    private Expense prepareMockExpense() {
        Expense mockeExpense = new Expense();
        mockeExpense.setId(Long.valueOf(1));
        mockeExpense.setUserId(Long.valueOf(1));
        mockeExpense.setAmount(new Money(new BigDecimal("1.00"), ",GBP"));
        mockeExpense.setExchangeRateDate(new Date());
        mockeExpense.setExchangeRate(new BigDecimal("1.3245"));
        mockeExpense.setTaxAmount(new Money(new BigDecimal("1.00"), ",GBP"));
        mockeExpense.setTaxRate(new BigDecimal("20.00"));
        mockeExpense.setReason("test reason");
        mockeExpense.setExpenseDate(new Date());
        mockeExpense.setDomesticAmount(new Money(new BigDecimal("1.00"), ",GBP"));
        return mockeExpense;
    }

    @Test
    public void testSaveOkExpense() throws Exception {
        final ExpenseDTO command = new ExpenseDTO();
        command.setAmount(new BigDecimal("100.00"));
        command.setCurrencyCode("GBP");
        command.setDate(new Date());
        command.setReason("test");
        command.setTaxAmount(new BigDecimal("16.67"));
        postObject(command, status().isOk());
    }


    @Test
    //test invalid command
    public void testSaveInvalidExpense() throws Exception {

        final ExpenseDTO command = new ExpenseDTO();
        command.setDate(new Date());
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].taxAmount", Matchers.is(1.00)));
    }

    @Test
    public void testCalculator() throws Exception {
        final CalculatorDTO command = new CalculatorDTO();
        command.setAmount(new BigDecimal("100"));
        command.setCurrencyCode("EUR");
        command.setDate(new Date());

        this.mvc.perform(
                post("/app/calculator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(BASIC_AUTH_KEY, BASIC_AUTH_VALUE)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", Matchers.is(100)))
                .andExpect(jsonPath("$.currency", Matchers.is("GBP")));
    }

    private void postObject(ExpenseDTO command, ResultMatcher status) throws Exception {

        this.mvc.perform(
                post(URI_EXPENSES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(BASIC_AUTH_KEY, BASIC_AUTH_VALUE)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status);
    }
}