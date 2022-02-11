package com.sonarsource.assignment;

import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class BalanceTest {

    @Test
    public void testBalance(){
        StringWriter stringWriter = new StringWriter();
        CommandHandler commandHandler = new CommandHandler(stringWriter);
        commandHandler.handleCommand("INCOME 2020/01/01 gift 500");
        commandHandler.handleCommand("INCOME 2020/01/01 gift 500");
        commandHandler.handleCommand("INCOME 2021/01/01 gift 100");
        commandHandler.handleCommand("PRINT DAY 2020/01/01");
        assertEquals(stringWriter.toString(), "1000");

        commandHandler.handleCommand("EXPENSE 2020/01/01 gift 5");
        commandHandler.handleCommand("EXPENSE 2020/01/01 gift2 5");

        stringWriter.getBuffer().setLength(0);
        commandHandler.handleCommand("PRINT DAY 2020/01/01");
        assertEquals(stringWriter.toString(), "990");

        stringWriter.getBuffer().setLength(0);
        commandHandler.handleCommand("PRINT MONTH 2021/01");
        assertEquals(stringWriter.toString(), "100");

        stringWriter.getBuffer().setLength(0);
        commandHandler.handleCommand("PRINT YEAR 2022");
        assertEquals(stringWriter.toString(), "0");

        stringWriter.getBuffer().setLength(0);
        commandHandler.handleCommand("PRINT DAY 2020/01/02");
        assertEquals(stringWriter.toString(), "0");
    }

}
