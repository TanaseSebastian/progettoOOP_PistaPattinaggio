package org.example.progettooop_pistapattinaggio.strategy;

import org.example.progettooop_pistapattinaggio.model.Booking;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TimeLeftStrategy implements BookingSortStrategy {
    @Override
    public List<Booking> sort(List<Booking> bookings) {
        return bookings.stream()
                .sorted(Comparator.comparingLong(Booking::getMinutesRemaining))
                .collect(Collectors.toList());
    }
}
