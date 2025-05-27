package org.example.progettooop_pistapattinaggio.observer;

import org.example.progettooop_pistapattinaggio.model.Booking;

public interface BookingObserver {
    void onBookingEnded(Booking booking);
}
