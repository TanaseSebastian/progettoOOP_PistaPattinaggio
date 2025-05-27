package org.example.progettooop_pistapattinaggio.service;

import org.example.progettooop_pistapattinaggio.model.*;
import org.example.progettooop_pistapattinaggio.observer.BookingObserver;
import org.example.progettooop_pistapattinaggio.util.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class BookingService {

    private static final Logger logger = LoggerManager.getLogger();
    private final List<BookingObserver> observers = new java.util.ArrayList<>();
    private final Repository<Booking> bookingRepository = new Repository<>();

    public BookingService() {
        List<Booking> loadedBookings = DataManager.loadBookings();
        bookingRepository.setAll(loadedBookings != null ? loadedBookings : List.of());
    }

    public void addObserver(BookingObserver observer) {
        observers.add(observer);
    }
    private void notifyBookingEnded(Booking booking) {
        for (BookingObserver observer : observers) {
            observer.onBookingEnded(booking);
        }
    }
    public void checkAllBookingStatuses() {
        for (Booking booking : getAllBookings()) {
            BookingStatus previous = booking.getStatus();
            booking.checkStatus(); // aggiorna stato
            if (previous == BookingStatus.IN_CORSO && booking.getStatus() == BookingStatus.CONCLUSA) {
                notifyBookingEnded(booking);
            }
        }
    }

    public boolean sellTicket(Slot slot, Customer customer, Ticket ticket, List<ShoeRental> rentedShoes, Inventory inventory, String paymentMethod){
        try {
            // Verifica la disponibilità dello slot
            if (!slot.isAvailable()) {
                logger.warning("Slot non disponibile: " + slot);
                return false;
            }
            // Verifica disponibilità per ogni taglia
            for (ShoeRental rental : rentedShoes) {
                if (!inventory.isAvailable(rental.getSize(), rental.getQuantity())) {
                    logger.warning("Scarponi non disponibili per taglia " + rental.getSize());
                    return false;
                }
            }
            // Vendi i pattini
            for (ShoeRental rental : rentedShoes) {
                if (!inventory.sellShoes(rental.getSize(), rental.getQuantity())) {
                    logger.warning("Errore nella vendita: taglia " + rental.getSize());
                    return false;
                }
            }
            // Crea la vendita
            Booking sale = new Booking(customer, ticket, rentedShoes, paymentMethod);
            // Salva la prenotazione
            bookingRepository.add(sale);
            slot.addBooking(sale);
            // Registra pagamento
            CashRegister.getInstance().recordPayment(ticket.getPrice(), paymentMethod);
            logger.info("Vendita completata: " + sale);
            return true;

        } catch (Exception e) {
            logger.severe("Errore durante la vendita: " + e.getMessage());
            return false;
        }
    }


    public Optional<Slot> findNextAvailableSlot(Day day, int minCapacity) {
        for (Slot slot : day) {
            int postiLiberi = slot.getCapacity() -
                    slot.getBookings().stream().mapToInt(b -> b.getTicket().getMaxPeople()).sum();

            if (postiLiberi >= minCapacity) {
                return Optional.of(slot);
            }
        }
        return Optional.empty();
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.getAll();
    }

    public void addBooking(Booking booking) {
        bookingRepository.add(booking);
    }

}
