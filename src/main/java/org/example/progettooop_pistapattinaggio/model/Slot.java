package org.example.progettooop_pistapattinaggio.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Slot implements Serializable {
    private final LocalDateTime startTime;
    private final int durationMinutes;
    private final int capacity; // max persone
    private final List<Booking> bookings;

    public Slot(LocalDateTime startTime, int durationMinutes, int capacity) {
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.capacity = capacity;
        this.bookings = new ArrayList<>();
    }

    // Controlla se lo slot è disponibile
    public boolean isAvailable() {
        if (capacity == 0) {
            return false; // Se la capacità è 0, lo slot è sempre pieno
        }
        int totalPeople = bookings.stream()
                .mapToInt(b -> b.getTicket().getMaxPeople())
                .sum();
        return totalPeople < capacity;
    }

    // Aggiungi la prenotazione allo slot
    public void addBooking(Booking booking) {
        if (!isAvailable()) {
            throw new IllegalStateException("Slot pieno, impossibile aggiungere prenotazione.");
        }
        bookings.add(booking);
    }

    // Restituisci tutte le prenotazioni associate allo slot
    public List<Booking> getBookings() {
        return bookings;
    }

    // Restituisci l'orario di inizio dello slot
    public LocalDateTime getStartTime() {
        return startTime;
    }

    // Restituisci la durata in minuti dello slot
    public int getDurationMinutes() {
        return durationMinutes;
    }

    // Restituisci la capacità massima dello slot
    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return startTime + " (" + durationMinutes + " min, max " + capacity + " persone)";
    }
}
