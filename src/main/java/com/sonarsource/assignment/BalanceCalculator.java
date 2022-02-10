package com.sonarsource.assignment;

import com.sonarsource.assignment.model.Balance;
import com.sonarsource.assignment.model.Command;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BalanceCalculator {

    private static final String  COMMAND_PRINT_DAY ="DAY";
    private static final String  COMMAND_PRINT_MONTH ="MONTH";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/d");

    private final StringWriter stringWriter;

    private final BalanceRepository balanceRepository;

    public BalanceCalculator(StringWriter stringWriter) {
        balanceRepository = new BalanceRepository();
        this.stringWriter = stringWriter;
    }

    public void handleCommand(String line){
        String[] tokens = line.split(" ");
        Command command = Command.valueOf(tokens[0]);
        command.getCalculator().accept(this, tokens);
    }

    public void handlePrint(String[] tokens) {
        String dayMontYear = tokens[1];
        String date = tokens[2];
        if(COMMAND_PRINT_DAY.equals(dayMontYear)){
            LocalDate localDate = LocalDate.parse(date, formatter);
            List<Balance> expenses =  balanceRepository.getAllExpenses(localDate);
            List<Balance> incomes =  balanceRepository.getAllIncomes(localDate);
            stringWriter.append(calculateAmount(incomes, expenses));
        }
        else if(COMMAND_PRINT_MONTH.equals(dayMontYear)){
            List<Balance> incomes = getAllMatchedIncomeLocalDates(()->findAllKeysForMonth(date, balanceRepository.keySetIncomes()));
            List<Balance> expenses = getAllMatchedExpenseLocalDates(()->findAllKeysForMonth(date, balanceRepository.keySetExpense()));
            stringWriter.append(calculateAmount(incomes, expenses));
        }
        else{
            List<Balance> incomes = getAllMatchedIncomeLocalDates(()->findAllKeysForYear(date, balanceRepository.keySetIncomes()));
            List<Balance> expenses = getAllMatchedExpenseLocalDates(()->findAllKeysForYear(date, balanceRepository.keySetExpense()));
            stringWriter.append(calculateAmount(incomes, expenses));
        }
    }

    private List<Balance> getAllMatchedExpenseLocalDates(Supplier<List<LocalDate>> supplier) {
        List<LocalDate> allMatchedExpenseLocalDates = supplier.get();
        return allMatchedExpenseLocalDates.stream().flatMap(localDate -> balanceRepository.getAllExpenses(localDate).stream()).collect(Collectors.toList());
    }

    private List<Balance> getAllMatchedIncomeLocalDates(Supplier<List<LocalDate>> supplier) {
        List<LocalDate> allMatchedIncomeLocalDates = supplier.get();
        return allMatchedIncomeLocalDates.stream().flatMap(localDate -> balanceRepository.getAllIncomes(localDate).stream()).collect(Collectors.toList());
    }

    private List<LocalDate> findAllKeysForMonth(String yearMonth, Set<LocalDate> keys) {
        List<LocalDate> allMatchingKeys = new ArrayList<>();
        LocalDate localDate = LocalDate.parse(yearMonth+"/01", formatter);//TODO fix date format
        for (LocalDate key : keys) {
            if(key.getMonth()==localDate.getMonth() && key.getYear()==localDate.getYear()){
                allMatchingKeys.add(key);
            }
        }
        return allMatchingKeys;
    }

    private List<LocalDate> findAllKeysForYear(String year, Set<LocalDate> dateSet) {
        List<LocalDate> allMatchingDate = new ArrayList<>();
        int yearInt = Integer.parseInt(year);
        for (LocalDate key : dateSet) {
            if(key.getYear()==yearInt){
                allMatchingDate.add(key);
            }
        }
        return allMatchingDate;
    }

    private String calculateAmount(List<Balance> incomes, List<Balance> expenses) {
        int totalExpenses = expenses.stream().map(Balance::getAmount).reduce(0, (result, amount)->result += amount);
        int totalIncomes = incomes.stream().map(Balance::getAmount).reduce(0, (result, amount)->result += amount);
        return Integer.toString(totalIncomes - totalExpenses);
    }

    public void handleIncome(String[] tokens) {
        balanceRepository.addIncome(createBalanceFRomTokens(tokens));
    }

    public void handleExpense(String[] tokens) {
        balanceRepository.addExpense(createBalanceFRomTokens(tokens));
    }

    private Balance createBalanceFRomTokens(String[] tokens){
        Integer amount = Integer.parseInt(tokens[3]);
        String description = tokens[2];
        String date = tokens[1];
        LocalDate localDate = LocalDate.parse(date, formatter);
        return new Balance(description, amount, localDate);
    }

}
