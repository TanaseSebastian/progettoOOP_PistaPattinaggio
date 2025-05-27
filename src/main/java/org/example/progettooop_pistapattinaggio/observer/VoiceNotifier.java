package org.example.progettooop_pistapattinaggio.observer;

import org.example.progettooop_pistapattinaggio.model.Booking;

public class VoiceNotifier implements BookingObserver {
    @Override
    public void onBookingEnded(Booking booking) {
        String message = "ATTENZIONE La corsa di " + booking.getCustomer().getName() + " è terminata! è pregato di riportare i pattini in cassa! GRAZIE!";
        System.out.println(message);

        // Per TTS reale, esempio con say (su Mac/Linux con `say` installato):
        try {
            Runtime.getRuntime().exec("say -v Alice \"" + message + "\"");
        } catch (Exception e) {
            System.err.println("Errore nel text-to-speech: " + e.getMessage());
        }
    }
}
