package com.sonarsource.assignment;

import com.sonarsource.assignment.model.Balance;

import java.time.LocalDate;
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

    public List<Balance> getAllExpenses(LocalDate date) {
        return expenses.getOrDefault(date, new ArrayList<>());
    }

    public List<Balance> getAllExpenses(List<LocalDate> dateSet) {
        return dateSet.stream().flatMap(date->expenses.getOrDefault(date, new ArrayList<>()).stream()).collect(Collectors.toList());
    }

    public List<Balance> getAllIncomes(LocalDate date) {
        return incomes.getOrDefault(date, new ArrayList<>());
    }

    public List<Balance> getAllIncomes(List<LocalDate> dateSet) {
        return dateSet.stream().flatMap(date->incomes.getOrDefault(date, new ArrayList<>()).stream()).collect(Collectors.toList());
    }

    public Set<LocalDate> keySetIncomes() {
        return incomes.keySet();
    }

    public Set<LocalDate> keySetExpenses() {
        return expenses.keySet();
    }

}

