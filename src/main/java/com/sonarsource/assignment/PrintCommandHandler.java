package com.sonarsource.assignment;

import com.sonarsource.assignment.model.Balance;
import com.sonarsource.assignment.model.Command;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrintCommandHandler {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/d");
    private final StringWriter stringWriter;
    private final BalanceRepository balanceRepository;

    public PrintCommandHandler(StringWriter stringWriter, BalanceRepository balanceRepository) {
        this.stringWriter = stringWriter;
        this.balanceRepository = balanceRepository;
    }

    public void handleCommandPrintDay(String date) {
        LocalDate localDate = LocalDate.parse(date, formatter);
        List<Balance> expenses =  balanceRepository.findAllExpenses(localDate);
        List<Balance> incomes =  balanceRepository.findAllIncomes(localDate);
        stringWriter.append(calculateAmount(incomes, expenses));
    }

    public void handleCommandPrintYear(String date) {
        int year = Integer.parseInt(date);
        List<Balance> incomes = balanceRepository.findAllIncomesByYear(year);
        List<Balance> expenses = balanceRepository.findAllExpensesByYear(year);
        stringWriter.append(calculateAmount(incomes, expenses));
    }

    public void handleCommandPrintMonth(String date) {
        LocalDate localDate = LocalDate.parse(date + "/01", formatter);
        List<Balance> incomes = balanceRepository.findAllIncomesByMonth(localDate.getYear(), localDate.getMonth());
        List<Balance> expenses = balanceRepository.findAllExpensesByMonth(localDate.getYear(), localDate.getMonth());
        stringWriter.append(calculateAmount(incomes, expenses));
    }

    private String calculateAmount(List<Balance> incomes, List<Balance> expenses) {
        int totalExpenses = expenses.stream().map(Balance::getAmount).reduce(0, (result, amount)->result += amount);
        int totalIncomes = incomes.stream().map(Balance::getAmount).reduce(0, (result, amount)->result += amount);
        return Integer.toString(totalIncomes - totalExpenses);
    }

    public void handle(String[] tokens) {
        Command.CommandPrint commandPrint = Command.CommandPrint.valueOf(tokens[1]);
        String date = tokens[2];
        commandPrint.getHandler().accept(this, date);
    }
}
