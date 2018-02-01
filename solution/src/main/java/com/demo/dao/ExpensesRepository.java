package com.demo.dao;

import com.demo.domain.Expense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepository extends CrudRepository<Expense, Long> {


}
