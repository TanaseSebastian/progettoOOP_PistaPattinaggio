package org.example.progettooop_pistapattinaggio.strategy;

import org.example.progettooop_pistapattinaggio.model.Booking;
import java.util.List;

public interface BookingSortStrategy {
    List<Booking> sort(List<Booking> bookings);
}