package com.sonarsource.assignment;

import com.sonarsource.assignment.model.Balance;
import com.sonarsource.assignment.model.Command;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CommandHandler {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/d");
    private final BalanceRepository balanceRepository;
    private final PrintCommandHandler printCommandHandler;

    public CommandHandler(StringWriter stringWriter) {
        balanceRepository = new BalanceRepository();
        printCommandHandler = new PrintCommandHandler(stringWriter, balanceRepository);
    }

    public void handleCommand(String line){
        String[] tokens = line.split(" ");
        Command command = Command.valueOf(tokens[0]);
        command.getHandler().accept(this, tokens);
    }

    public void handlePrint(String[] tokens) {
        printCommandHandler.handle(tokens);
    }

    public void handleIncome(String[] tokens) {
        balanceRepository.addIncome(createBalanceFromTokens(tokens));
    }

    public void handleExpense(String[] tokens) {
        balanceRepository.addExpense(createBalanceFromTokens(tokens));
    }

    private Balance createBalanceFromTokens(String[] tokens){
        Integer amount = Integer.parseInt(tokens[3]);
        String description = tokens[2];
        String date = tokens[1];
        LocalDate localDate = LocalDate.parse(date, formatter);
        return new Balance(description, amount, localDate);
    }

}
