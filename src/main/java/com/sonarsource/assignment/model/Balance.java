package com.sonarsource.assignment.model;

import java.time.LocalDate;

public class Balance {
    private final String description;
    private final Integer amount;
    private final LocalDate localDate;

    public Balance(String description, Integer amount, LocalDate localDate) {
        this.description = description;
        this.amount = amount;
        this.localDate = localDate;
    }

    public String getDescription() {
        return description;
    }

    public Integer getAmount() {
        return amount;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

}
