package com.sonarsource.assignment;

import com.sonarsource.assignment.model.Balance;
import com.sonarsource.assignment.model.Command;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

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
        List<Balance> expenses =  balanceRepository.getAllExpenses(localDate);
        List<Balance> incomes =  balanceRepository.getAllIncomes(localDate);
        stringWriter.append(calculateAmount(incomes, expenses));
    }

    public void handleCommandPrintYear(String date) {
        List<Balance> incomes = findAllIncomes(()-> filterDateSetByYear(date, balanceRepository.keySetIncomes()));
        List<Balance> expenses = findAllExpenses(()-> filterDateSetByYear(date, balanceRepository.keySetExpenses()));
        stringWriter.append(calculateAmount(incomes, expenses));
    }

    public void handleCommandPrintMonth(String date) {
        List<Balance> incomes = findAllIncomes(()-> filterDateSetByMonth(date, balanceRepository.keySetIncomes()));
        List<Balance> expenses = findAllExpenses(()-> filterDateSetByMonth(date, balanceRepository.keySetExpenses()));
        stringWriter.append(calculateAmount(incomes, expenses));
    }

    private List<Balance> findAllExpenses(Supplier<List<LocalDate>> localDateSetFinder) {
        return new ArrayList<>(balanceRepository.getAllExpenses(localDateSetFinder.get()));
    }

    private List<Balance> findAllIncomes(Supplier<List<LocalDate>> localDateSetFinder) {
        return new ArrayList<>(balanceRepository.getAllIncomes(localDateSetFinder.get()));
    }

    private static List<LocalDate> filterDateSetByMonth(String yearMonth, Set<LocalDate> dateSet) {
        List<LocalDate> allMatchingKeys = new ArrayList<>();
        LocalDate localDate = LocalDate.parse(yearMonth+"/01", formatter);//TODO fix date format
        for (LocalDate key : dateSet) {
            if(key.getMonth()==localDate.getMonth() && key.getYear()==localDate.getYear()){
                allMatchingKeys.add(key);
            }
        }
        return allMatchingKeys;
    }

    private static List<LocalDate> filterDateSetByYear(String year, Set<LocalDate> dateSet) {
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

    public void handle(String[] tokens) {
        Command.CommandPrint commandPrint = Command.CommandPrint.valueOf(tokens[1]);
        String date = tokens[2];
        commandPrint.getHandler().accept(this, date);
    }
}
