package org.example.progettooop_pistapattinaggio.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta uno slot temporale disponibile per l'accesso alla pista di pattinaggio.
 * Ogni slot ha un orario di inizio, una durata, una capacità massima e una lista di prenotazioni associate.
 */
public class Slot implements Serializable {

    private final LocalDateTime startTime;
    private final int durationMinutes;
    private final int capacity;
    private final List<Booking> bookings;

    /**
     * Costruisce un nuovo {@code Slot} con i parametri specificati.
     *
     * @param startTime        orario di inizio dello slot
     * @param durationMinutes  durata dello slot in minuti
     * @param capacity         capacità massima di persone ammesse nello slot
     */
    public Slot(LocalDateTime startTime, int durationMinutes, int capacity) {
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.capacity = capacity;
        this.bookings = new ArrayList<>();
    }

    /**
     * Verifica se lo slot è disponibile, ovvero se non ha ancora raggiunto la capacità massima.
     *
     * @return {@code true} se c'è ancora disponibilità, altrimenti {@code false}
     */
    public boolean isAvailable() {
        if (capacity == 0) {
            return false;
        }
        int totalPeople = bookings.stream()
                .mapToInt(b -> b.getTicket().getMaxPeople())
                .sum();
        return totalPeople < capacity;
    }

    /**
     * Aggiunge una prenotazione allo slot, se c'è disponibilità.
     *
     * @param booking la prenotazione da aggiungere
     * @throws IllegalStateException se lo slot è già pieno
     */
    public void addBooking(Booking booking) {
        if (!isAvailable()) {
            throw new IllegalStateException("Slot pieno, impossibile aggiungere prenotazione.");
        }
        bookings.add(booking);
    }

    /**
     * Restituisce la lista delle prenotazioni associate a questo slot.
     *
     * @return lista delle prenotazioni
     */
    public List<Booking> getBookings() {
        return bookings;
    }

    /**
     * Restituisce l'orario di inizio dello slot.
     *
     * @return orario di inizio
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Restituisce la durata dello slot in minuti.
     *
     * @return durata in minuti
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }

    /**
     * Restituisce la capacità massima di persone ammesse nello slot.
     *
     * @return capacità massima
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Restituisce una rappresentazione testuale dello slot, utile per interfacce e log.
     *
     * @return stringa descrittiva dello slot
     */
    @Override
    public String toString() {
        return startTime + " (" + durationMinutes + " min, max " + capacity + " persone)";
    }
}
