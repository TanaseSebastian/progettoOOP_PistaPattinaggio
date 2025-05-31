package org.example.progettooop_pistapattinaggio.model;

/**
 * Interfaccia che rappresenta un biglietto per l'accesso alla pista di pattinaggio.
 * Un {@code Ticket} definisce il nome, il prezzo, la durata e il numero massimo di persone coperte dal biglietto.
 */
public interface Ticket {

    /**
     * Restituisce il nome descrittivo del biglietto (es. "Biglietto Standard", "Promo Famiglia").
     *
     * @return nome del biglietto
     */
    String getName();

    /**
     * Restituisce il prezzo del biglietto.
     *
     * @return prezzo in euro
     */
    double getPrice();

    /**
     * Restituisce la durata del biglietto in minuti.
     * <p>Ad esempio: 30 minuti, 60 minuti, ecc.</p>
     *
     * @return durata in minuti
     */
    int getDurationMinutes();

    /**
     * Restituisce il numero massimo di persone che possono usufruire del biglietto.
     * <p>Utile per biglietti multipli (es. gruppi o famiglie).</p>
     *
     * @return numero massimo di persone
     */
    int getMaxPeople();
}
