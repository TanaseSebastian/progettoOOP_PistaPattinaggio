package org.example.progettooop_pistapattinaggio.strategy;

import org.example.progettooop_pistapattinaggio.model.Booking;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategia di ordinamento delle prenotazioni in base al prezzo del biglietto in ordine decrescente.
 * <p>Implementa l'interfaccia {@link BookingSortStrategy} utilizzando lo stream API di Java per
 * ordinare le prenotazioni dal biglietto più costoso al meno costoso.</p>
 */
public class PriceDescendingStrategy implements BookingSortStrategy {

    /**
     * Ordina la lista di prenotazioni in base al prezzo del biglietto, dal più alto al più basso.
     *
     * @param bookings la lista di prenotazioni da ordinare
     * @return una nuova lista ordinata per prezzo decrescente
     */
    @Override
    public List<Booking> sort(List<Booking> bookings) {
        return bookings.stream()
                .sorted(Comparator.comparingDouble(b -> -b.getTicket().getPrice()))
                .collect(Collectors.toList());
    }
}
