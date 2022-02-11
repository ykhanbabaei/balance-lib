package com.sonarsource.assignment.model;

import com.sonarsource.assignment.CommandHandler;
import com.sonarsource.assignment.PrintCommandHandler;

import java.util.function.BiConsumer;

public enum Command {
    EXPENSE(CommandHandler::handleExpense), INCOME(CommandHandler::handleIncome), PRINT(CommandHandler::handlePrint);

    private final BiConsumer<CommandHandler, String[]> handler;

    Command(BiConsumer<CommandHandler, String[]> handler) {
        this.handler = handler;
    }

    public BiConsumer<CommandHandler, String[]> getHandler() {
        return handler;
    }

    public enum CommandPrint{
        DAY(PrintCommandHandler::handleCommandPrintDay), YEAR(PrintCommandHandler::handleCommandPrintYear),
        MONTH(PrintCommandHandler::handleCommandPrintMonth);

        private final BiConsumer<PrintCommandHandler, String> handler;

        CommandPrint(BiConsumer<PrintCommandHandler, String> handler) {
            this.handler = handler;
        }

        public BiConsumer<PrintCommandHandler, String> getHandler() {
            return handler;
        }
    }
}
