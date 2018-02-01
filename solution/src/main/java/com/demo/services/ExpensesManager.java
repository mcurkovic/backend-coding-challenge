package com.demo.services;

import com.demo.domain.Expense;
import java.util.List;

public interface ExpensesManager {

    List<Expense> findExpenses();

    void saveExpense(final Expense expense);


}
