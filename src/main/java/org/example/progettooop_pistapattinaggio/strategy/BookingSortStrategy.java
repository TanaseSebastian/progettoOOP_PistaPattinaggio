package org.example.progettooop_pistapattinaggio.strategy;

import org.example.progettooop_pistapattinaggio.model.Booking;
import java.util.List;

/**
 * Interfaccia per la definizione di strategie di ordinamento delle prenotazioni.
 * <p>Utilizzata per implementare il pattern Strategy, consente di applicare diverse
 * logiche di ordinamento su una lista di oggetti {@link Booking}.</p>
 */
public interface BookingSortStrategy {

    /**
     * Ordina una lista di prenotazioni secondo una strategia specifica.
     *
     * @param bookings la lista di prenotazioni da ordinare
     * @return una nuova lista ordinata di prenotazioni
     */
    List<Booking> sort(List<Booking> bookings);
}
