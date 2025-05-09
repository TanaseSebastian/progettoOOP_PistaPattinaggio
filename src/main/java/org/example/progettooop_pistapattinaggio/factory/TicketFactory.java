package org.example.progettooop_pistapattinaggio.factory;

import org.example.progettooop_pistapattinaggio.model.CustomTicket;
import org.example.progettooop_pistapattinaggio.model.Ticket;

public class TicketFactory {

    public static Ticket createTicket(String type) {
        return switch (type.toLowerCase()) {
            case "single30" -> new CustomTicket("Singolo 30 minuti", 5.0, 30, 1);
            case "single60" -> new CustomTicket("Singolo 60 minuti", 8.0, 60, 1);
            case "couple30" -> new CustomTicket("Coppia 30 minuti", 9.0, 30, 2);
            case "couple60" -> new CustomTicket("Coppia 60 minuti", 14.0, 60, 2);
            case "family" -> new CustomTicket("Famiglia 60 minuti", 25.0, 60, 4);
            case "pass" -> new CustomTicket("Abbonamento Mensile", 60.0, 43200, 1);
            default -> throw new IllegalArgumentException("Tipo biglietto non valido: " + type);
        };
    }
}
