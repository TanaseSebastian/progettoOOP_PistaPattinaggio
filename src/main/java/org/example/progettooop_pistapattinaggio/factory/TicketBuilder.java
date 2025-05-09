package org.example.progettooop_pistapattinaggio.factory;

import org.example.progettooop_pistapattinaggio.model.CustomTicket;
import org.example.progettooop_pistapattinaggio.model.Ticket;

public class TicketBuilder {
    private String name;
    private double price;
    private int validMinutes;
    private int maxPeople;

    public TicketBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TicketBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    public TicketBuilder setValidMinutes(int minutes) {
        this.validMinutes = minutes;
        return this;
    }

    public TicketBuilder setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
        return this;
    }

    public Ticket build() {
        return new CustomTicket(name, price, validMinutes, maxPeople);
    }
}
