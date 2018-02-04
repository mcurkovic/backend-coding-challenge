package com.demo.services.api;

import com.demo.domain.Expense;
import com.demo.domain.Money;
import java.math.BigDecimal;
import java.util.List;

public interface ExpensesManager {

    List<Expense> findExpenses();

    void saveExpense(final Expense expense);

}

