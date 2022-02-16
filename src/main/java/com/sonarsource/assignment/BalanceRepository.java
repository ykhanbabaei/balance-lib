package com.sonarsource.assignment;

import com.sonarsource.assignment.model.Balance;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

public class BalanceRepository {

    private final Map<LocalDate, List<Balance>> incomes = new HashMap<>();
    private final Map<LocalDate, List<Balance>> expenses = new HashMap<>();

    public void addIncome(Balance balance) {
        List<Balance> list = incomes.getOrDefault(balance.getLocalDate(), new ArrayList<>());
        list.add(balance);
        incomes.put(balance.getLocalDate(), list);
    }

    public void addExpense(Balance balance) {
        List<Balance> list = expenses.getOrDefault(balance.getLocalDate(), new ArrayList<>());
        list.add(balance);
        expenses.put(balance.getLocalDate(), list);
    }

    public List<Balance> findAllExpenses(LocalDate date) {
        return expenses.getOrDefault(date, new ArrayList<>());
    }

    public List<Balance> findAllIncomes(LocalDate date) {
        return incomes.getOrDefault(date, new ArrayList<>());
    }

    public List<Balance> findAllIncomesByMonth(int year, Month month) {
        return findAllIncomesByMonth(year, month, incomes.entrySet());
    }

    private List<Balance> findAllIncomesByMonth(int year, Month month, Set<Map.Entry<LocalDate, List<Balance>>> entries) {
        return entries.stream().filter(entry->entry.getKey().getMonth()==month && entry.getKey().getYear()==year).flatMap(entry->entry.getValue().stream()).collect(Collectors.toList());
    }

    public List<Balance> findAllExpensesByMonth(int year, Month month) {
        return findAllIncomesByMonth(year, month, expenses.entrySet());
    }

    private List<Balance> filterDateSetByYear(int year, Set<Map.Entry<LocalDate, List<Balance>>> entries) {
        return entries.stream().filter(entry->entry.getKey().getYear() == year).flatMap(entry->entry.getValue().stream()).collect(Collectors.toList());
    }

    public List<Balance> findAllIncomesByYear(int year) {
        return filterDateSetByYear(year, incomes.entrySet());
    }

    public List<Balance> findAllExpensesByYear(int year) {
        return filterDateSetByYear(year, expenses.entrySet());
    }

}

