package com.demo.services.impl;

import com.demo.dao.ExpensesRepository;
import com.demo.domain.Expense;
import com.demo.services.api.ExchangeRatesManager;
import com.demo.services.api.ExpensesManager;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ExpensesManagerImpl implements ExpensesManager {

    @Autowired
    private final ExpensesRepository expensesRepository;

    @Autowired
    private final ExchangeRatesManager exchangeRatesManager;

    @Autowired
    public ExpensesManagerImpl(final ExpensesRepository expensesRepository,
            final ExchangeRatesManager exchangeRatesManager)
    {
        this.expensesRepository = expensesRepository;
        this.exchangeRatesManager = exchangeRatesManager;
    }

    @Override
    public Iterable<Expense> findExpenses() {
        return expensesRepository.findAll();
    }

    @Transactional
    @Override
    public Expense saveExpense(final Expense expense) {
        return expensesRepository.save(expense);
    }


}
