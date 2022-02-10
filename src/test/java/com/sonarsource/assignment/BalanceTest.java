package com.sonarsource.assignment;

import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class BalanceTest {

    @Test
    public void testCalculateTotalPrice(){
        StringWriter stringWriter = new StringWriter();
        BalanceCalculator balanceCalculator = new BalanceCalculator(stringWriter);
        balanceCalculator.handleCommand("INCOME 2020/01/01 gift 500");
        balanceCalculator.handleCommand("INCOME 2020/01/01 gift 500");
        balanceCalculator.handleCommand("INCOME 2021/01/01 gift 100");
        balanceCalculator.handleCommand("PRINT DAY 2020/01/01");
        assertEquals(stringWriter.toString(), "1000");

        balanceCalculator.handleCommand("EXPENSE 2020/01/01 gift 5");
        balanceCalculator.handleCommand("EXPENSE 2020/01/01 gift2 5");

        stringWriter.getBuffer().setLength(0);
        balanceCalculator.handleCommand("PRINT DAY 2020/01/01");
        assertEquals(stringWriter.toString(), "990");

        stringWriter.getBuffer().setLength(0);
        balanceCalculator.handleCommand("PRINT MONTH 2021/01");
        assertEquals(stringWriter.toString(), "100");

        stringWriter.getBuffer().setLength(0);
        balanceCalculator.handleCommand("PRINT YEAR 2022");
        assertEquals(stringWriter.toString(), "0");

        stringWriter.getBuffer().setLength(0);
        balanceCalculator.handleCommand("PRINT DAY 2020/01/02");
        assertEquals(stringWriter.toString(), "0");
    }

}
