package com.demo.services.api;

import com.demo.domain.Expense;
import java.util.List;

public interface ExpensesManager {

    List<Expense> findExpenses();

    Expense saveExpense(final Expense expense);

}

