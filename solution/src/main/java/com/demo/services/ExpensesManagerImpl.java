package com.demo.services;

import com.demo.dao.ExpensesRepository;
import com.demo.domain.ExchangeRates;
import com.demo.domain.Expense;
import com.demo.domain.Money;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ExpensesManagerImpl implements ExpensesManager {

    @Value("${defaultCurrencyCode}")
    private String defaultCurrencyCode;

    @Autowired
    private ExpensesRepository expensesRepository;

    @Autowired
    private ExchangeRatesManager exchangeRatesManager;

    @Override
    public List<Expense> findExpenses() {
        final Iterable<Expense> all = expensesRepository.findAll();
        final List<Expense> expenses = new ArrayList<>();
        all.forEach(expenses::add);
        return expenses;
    }

    @Override
    public void saveExpense(final Expense expense) {
        expensesRepository.save(expense);

    }
}
