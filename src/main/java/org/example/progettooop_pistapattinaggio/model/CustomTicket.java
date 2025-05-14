package org.example.progettooop_pistapattinaggio.model;

import java.io.Serializable;

public class CustomTicket implements Ticket, Serializable {
    private final String name;
    private final double price;
    private final int durationMinutes;
    private final int maxPeople;

    public CustomTicket(String name, double price, int durationMinutes, int maxPeople) {
        this.name = name;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.maxPeople = maxPeople;
    }

    @Override
    public String getName() { return name; }

    @Override
    public double getPrice() { return price; }

    @Override
    public int getDurationMinutes() { return durationMinutes; }

    @Override
    public int getMaxPeople() { return maxPeople; }
}
