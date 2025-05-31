package org.example.progettooop_pistapattinaggio.observer;

import org.example.progettooop_pistapattinaggio.model.Booking;

/**
 * Interfaccia per l'implementazione del pattern Observer applicato alle prenotazioni.
 * <p>I listener che implementano questa interfaccia possono essere notificati quando una prenotazione termina.</p>
 */
public interface BookingObserver {

    /**
     * Metodo chiamato quando una prenotazione è terminata.
     *
     * @param booking la prenotazione che è appena stata conclusa
     */
    void onBookingEnded(Booking booking);
}
