package com.sonarsource.assignment.model;

import com.sonarsource.assignment.BalanceCalculator;

import java.util.function.BiConsumer;

public enum Command {
    EXPENSE(BalanceCalculator::handleExpense), INCOME(BalanceCalculator::handleIncome), PRINT(BalanceCalculator::handlePrint);

    private BiConsumer<BalanceCalculator, String[]> calculator;

    Command(BiConsumer<BalanceCalculator, String[]> calculator) {
        this.calculator = calculator;
    }

    public BiConsumer<BalanceCalculator, String[]> getCalculator() {
        return calculator;
    }
}
