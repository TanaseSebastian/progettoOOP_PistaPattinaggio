package org.example.progettooop_pistapattinaggio.strategy;

import org.example.progettooop_pistapattinaggio.model.Booking;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategia di ordinamento delle prenotazioni in base al tempo rimanente.
 * <p>Le prenotazioni vengono ordinate in ordine crescente di minuti rimanenti,
 * così da evidenziare prima quelle che stanno per terminare.</p>
 */
public class TimeLeftStrategy implements BookingSortStrategy {

    /**
     * Ordina la lista di prenotazioni in base ai minuti rimanenti prima della scadenza,
     * dalla più vicina alla scadenza alla più lontana.
     *
     * @param bookings la lista di prenotazioni da ordinare
     * @return una nuova lista ordinata per tempo rimanente crescente
     */
    @Override
    public List<Booking> sort(List<Booking> bookings) {
        return bookings.stream()
                .sorted(Comparator.comparingLong(Booking::getMinutesRemaining))
                .collect(Collectors.toList());
    }
}
