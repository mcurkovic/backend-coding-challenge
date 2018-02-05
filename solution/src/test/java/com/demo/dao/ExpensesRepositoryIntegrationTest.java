package com.demo.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.demo.domain.Expense;
import com.demo.domain.Money;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ExpensesRepositoryIntegrationTest {

    @Autowired
    private ExpensesRepository expensesRepository;


    @Test
    public void testSaveFindExpenses() {

        final Expense expense = new Expense();
        expense.setAmount(new Money(new BigDecimal(100), "798"));
        expense.setDomesticAmount(new Money(new BigDecimal(100), "798"));
        expense.setExchangeRate(new BigDecimal(1.45667));
        expense.setExpenseDate(new Date());
        expense.setReason("testing");
        expense.setTaxAmount(new Money(new BigDecimal(1), "798"));
        expense.setTaxRate(new BigDecimal(20));
        expense.setUserId(Long.valueOf(1));

        expensesRepository.save(expense);
        final Iterable<Expense> expenses = expensesRepository.findAll();

        assertNotNull(expenses);
        assertTrue(!CollectionUtils.isEmpty(Arrays.asList(expenses)));
    }
}
