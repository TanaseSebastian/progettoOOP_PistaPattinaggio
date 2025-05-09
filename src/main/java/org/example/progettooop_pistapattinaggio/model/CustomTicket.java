package org.example.progettooop_pistapattinaggio.model;

import java.io.Serializable;

public class CustomTicket implements Ticket, Serializable {
    private final String name;
    private final double price;
    private final int validMinutes;
    private final int maxPeople;

    public CustomTicket(String name, double price, int validMinutes, int maxPeople) {
        this.name = name;
        this.price = price;
        this.validMinutes = validMinutes;
        this.maxPeople = maxPeople;
    }

    @Override
    public String getName() { return name; }

    @Override
    public double getPrice() { return price; }

    @Override
    public int getValidMinutes() { return validMinutes; }

    @Override
    public int getMaxPeople() { return maxPeople; }
}
