package org.example.progettooop_pistapattinaggio.model;

public interface Ticket {
    String getName();
    double getPrice();
    int getValidMinutes(); // durata
    int getMaxPeople();    // numero persone coperte
}