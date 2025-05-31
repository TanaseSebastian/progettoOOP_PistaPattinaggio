package org.example.progettooop_pistapattinaggio.observer;

import org.example.progettooop_pistapattinaggio.model.Booking;

/**
 * Implementazione dell'interfaccia {@link BookingObserver} che notifica vocalmente
 * la fine di una prenotazione.
 *
 * <p>Alla conclusione di una {@link Booking}, stampa un messaggio a console e
 * tenta di eseguire una notifica vocale tramite comando di sistema (es. {@code say} su macOS/Linux).</p>
 */
public class VoiceNotifier implements BookingObserver {

    /**
     * Metodo chiamato automaticamente quando una prenotazione termina.
     * Genera un messaggio vocale per richiamare l'attenzione dell'utente.
     *
     * @param booking la prenotazione che è appena terminata
     */
    @Override
    public void onBookingEnded(Booking booking) {
        String message = "ATTENZIONE La corsa di " + booking.getCustomer().getName() +
                " è terminata! è pregato di riportare i pattini in cassa! GRAZIE!";
        System.out.println(message);

        // Tentativo di esecuzione di TTS (Text-to-Speech) tramite comando "say"
        try {
            Runtime.getRuntime().exec("say -v Alice \"" + message + "\"");
        } catch (Exception e) {
            System.out.println("Errore nel text-to-speech: " + e.getMessage());
        }
    }
}
