package org.example.progettooop_pistapattinaggio.strategy;

import org.example.progettooop_pistapattinaggio.model.Booking;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PriceDescendingStrategy implements BookingSortStrategy {
    @Override
    public List<Booking> sort(List<Booking> bookings) {
        return bookings.stream()
                .sorted(Comparator.comparingDouble(b -> -b.getTicket().getPrice()))
                .collect(Collectors.toList());
    }
}
