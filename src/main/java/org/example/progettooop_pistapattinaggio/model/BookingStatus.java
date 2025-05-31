package org.example.progettooop_pistapattinaggio.model;

/**
 * Enum {@code BookingStatus} rappresenta lo stato di una prenotazione
 * presso la pista di pattinaggio.
 *
 * <p>Gli stati possibili sono:</p>
 * <ul>
 *     <li>{@link #IN_CORSO} - La prenotazione è attualmente attiva.</li>
 *     <li>{@link #CONCLUSA} - La prenotazione è terminata.</li>
 * </ul>
 */
public enum BookingStatus {
    /**
     * Stato che indica una prenotazione attiva e in corso.
     */
    IN_CORSO,

    /**
     * Stato che indica che la prenotazione è stata completata o è scaduta.
     */
    CONCLUSA
}
