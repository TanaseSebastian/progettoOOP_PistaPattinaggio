package org.example.progettooop_pistapattinaggio.service;

import org.example.progettooop_pistapattinaggio.model.*;
import org.example.progettooop_pistapattinaggio.util.CashRegister;
import org.example.progettooop_pistapattinaggio.util.Inventory;
import org.example.progettooop_pistapattinaggio.util.LoggerManager;
import org.example.progettooop_pistapattinaggio.util.Repository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class BookingService {

    private static final Logger logger = LoggerManager.getLogger();
    private final Repository<Booking> bookingRepository = new Repository<>();

    public boolean sellTicket(Slot slot, Customer customer, Ticket ticket, int shoeSize, int shoeQuantity, Inventory inventory, String paymentMethod) {
        try {
            // Verifica la disponibilità dello slot
            if (!slot.isAvailable()) {
                logger.warning("Slot non disponibile: " + slot);
                return false;
            }

            // Verifica la disponibilità dei pattini
            if (!inventory.isAvailable(shoeSize, shoeQuantity)) {
                logger.warning("Scarponi non disponibili per la taglia " + shoeSize);
                return false;
            }

            // Vendi i pattini
            if (!inventory.sellShoes(shoeSize, shoeQuantity)) {
                logger.warning("Impossibile vendere i pattini. Taglia " + shoeSize + ", quantità " + shoeQuantity);
                return false;
            }

            // Crea la vendita (Booking)
            Booking sale = new Booking(customer, ticket, shoeSize, shoeQuantity, paymentMethod);

            // Aggiungi la prenotazione al repository
            bookingRepository.add(sale);  // Aggiungi la prenotazione al repository

            // Aggiungi la prenotazione allo slot
            slot.addBooking(sale);

            // Registra il pagamento nel Cash Register
            CashRegister.getInstance().recordPayment(ticket.getPrice(), paymentMethod);

            // Log della vendita
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
}
