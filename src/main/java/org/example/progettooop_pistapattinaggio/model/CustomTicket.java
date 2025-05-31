package org.example.progettooop_pistapattinaggio.model;

import java.io.Serializable;

/**
 * Implementazione personalizzata dell'interfaccia {@link Ticket}, rappresenta un biglietto
 * configurabile per l'accesso alla pista di pattinaggio.
 * <p>Ogni biglietto include nome, prezzo, durata in minuti e numero massimo di persone.</p>
 */
public class CustomTicket implements Ticket, Serializable {

    private final String name;
    private final double price;
    private final int durationMinutes;
    private final int maxPeople;

    /**
     * Costruisce un nuovo {@code CustomTicket} con i parametri specificati.
     *
     * @param name             nome del biglietto
     * @param price            prezzo del biglietto
     * @param durationMinutes  durata in minuti dell'accesso alla pista
     * @param maxPeople        numero massimo di persone consentite con questo biglietto
     */
    public CustomTicket(String name, double price, int durationMinutes, int maxPeople) {
        this.name = name;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.maxPeople = maxPeople;
    }

    /**
     * Restituisce il nome del biglietto.
     *
     * @return nome del biglietto
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Restituisce il prezzo del biglietto.
     *
     * @return prezzo del biglietto
     */
    @Override
    public double getPrice() {
        return price;
    }

    /**
     * Restituisce la durata in minuti del biglietto.
     *
     * @return durata in minuti
     */
    @Override
    public int getDurationMinutes() {
        return durationMinutes;
    }

    /**
     * Restituisce il numero massimo di persone consentite con questo biglietto.
     *
     * @return numero massimo di persone
     */
    @Override
    public int getMaxPeople() {
        return maxPeople;
    }
}
