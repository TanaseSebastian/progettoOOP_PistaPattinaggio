package org.example.progettooop_pistapattinaggio.model;

public interface Ticket {
    String getName();             // Nome leggibile
    double getPrice();            // Prezzo
    int getDurationMinutes();     // Durata in minuti (es. 30, 60)
    int getMaxPeople();           // Persone coperte
}