package org.example.progettooop_pistapattinaggio.model;

import java.io.Serializable;

/**
 * Rappresenta un noleggio di pattini, con una determinata taglia e quantità.
 * <p>Questa classe è utilizzata per tracciare i pattini noleggiati da un cliente durante una prenotazione.</p>
 */
public class ShoeRental implements Serializable {

    private final int size;
    private final int quantity;

    /**
     * Costruisce un oggetto {@code ShoeRental} con taglia e quantità specificate.
     *
     * @param size     la taglia dei pattini noleggiati
     * @param quantity il numero di paia noleggiati
     */
    public ShoeRental(int size, int quantity) {
        this.size = size;
        this.quantity = quantity;
    }

    /**
     * Restituisce la taglia dei pattini noleggiati.
     *
     * @return taglia dei pattini
     */
    public int getSize() {
        return size;
    }

    /**
     * Restituisce la quantità di pattini noleggiati per la taglia specificata.
     *
     * @return quantità noleggiata
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Restituisce una rappresentazione testuale del noleggio, utile per interfacce utente e riepiloghi.
     *
     * @return stringa formattata del tipo "3 x taglia 42"
     */
    @Override
    public String toString() {
        return quantity + " x taglia " + size;
    }
}
