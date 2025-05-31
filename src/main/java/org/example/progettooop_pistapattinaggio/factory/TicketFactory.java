package org.example.progettooop_pistapattinaggio.factory;

import org.example.progettooop_pistapattinaggio.model.CustomTicket;
import org.example.progettooop_pistapattinaggio.model.Ticket;

import java.util.*;

/**
 * Factory per la creazione di oggetti {@link Ticket} predefiniti
 * in base a un identificatore testuale.
 */
public class TicketFactory {

    // Mappa che collega i tipi di ticket ai nomi leggibili da mostrare all'utente
    private static final Map<String, String> ticketLabelMap = Map.of(
            "test30s", "test Singolo 60 secondi",
            "single30", "Singolo 30 minuti",
            "single60", "Singolo 60 minuti",
            "couple30", "Coppia 30 minuti",
            "couple60", "Coppia 60 minuti",
            "family",   "Famiglia 60 minuti",
            "pass",     "Abbonamento Mensile"
    );

    /**
     * Crea un'istanza di {@link Ticket} basata sul tipo specificato.
     *
     * @param type Tipo del biglietto (es. "single30", "family", ecc.)
     * @return Istanza di {@link Ticket} corrispondente al tipo
     * @throws IllegalArgumentException se il tipo non è valido
     */
    public static Ticket createTicket(String type) {
        return switch (type.toLowerCase()) {
            case "test30s" -> new CustomTicket("Test 60 secondi", 5.0, 1, 1);
            case "single30" -> new CustomTicket("Singolo 30 minuti", 5.0, 30, 1);
            case "single60" -> new CustomTicket("Singolo 60 minuti", 8.0, 60, 1);
            case "couple30" -> new CustomTicket("Coppia 30 minuti", 9.0, 30, 2);
            case "couple60" -> new CustomTicket("Coppia 60 minuti", 14.0, 60, 2);
            case "family"   -> new CustomTicket("Famiglia 60 minuti", 25.0, 60, 4);
            case "pass"     -> new CustomTicket("Abbonamento Mensile", 60.0, 43200, 1); // 30 giorni
            default -> throw new IllegalArgumentException("Tipo biglietto non valido: " + type);
        };
    }

    /**
     * Restituisce la mappa dei tipi di biglietto e le loro etichette leggibili.
     *
     * @return Mappa tipo → etichetta
     */
    public static Map<String, String> getTicketLabelMap() {
        return ticketLabelMap;
    }

    /**
     * Restituisce la lista di tutte le etichette dei biglietti disponibili.
     *
     * @return Lista di etichette leggibili
     */
    public static List<String> getAllLabels() {
        return new ArrayList<>(ticketLabelMap.values());
    }

    /**
     * Restituisce il tipo (key) corrispondente a un'etichetta leggibile.
     *
     * @param label Etichetta leggibile (es. "Coppia 60 minuti")
     * @return Chiave del tipo di biglietto corrispondente
     * @throws IllegalArgumentException se l'etichetta non è riconosciuta
     */
    public static String getTypeFromLabel(String label) {
        return ticketLabelMap.entrySet().stream()
                .filter(e -> e.getValue().equals(label))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Etichetta biglietto non riconosciuta: " + label));
    }
}
