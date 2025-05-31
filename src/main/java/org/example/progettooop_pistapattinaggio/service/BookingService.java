package org.example.progettooop_pistapattinaggio.service;

import org.example.progettooop_pistapattinaggio.model.*;
import org.example.progettooop_pistapattinaggio.observer.BookingObserver;
import org.example.progettooop_pistapattinaggio.util.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Servizio che gestisce le prenotazioni, le vendite dei biglietti,
 * l'inventario degli scarponi e la notifica degli osservatori al termine delle prenotazioni.
 */
public class BookingService {

    private static final Logger logger = LoggerManager.getLogger();
    private final List<BookingObserver> observers = new ArrayList<>();
    private final Repository<Booking> bookingRepository = new Repository<>();
    private final PistaDiPattinaggio pista;

    /**
     * Costruttore che inizializza il servizio e carica le prenotazioni da file.
     */
    public BookingService(PistaDiPattinaggio pista, boolean loadFromFile) {
        this.pista = pista;
        if (loadFromFile) {
            List<Booking> loadedBookings = DataManager.loadBookings();
            bookingRepository.setAll(loadedBookings != null ? loadedBookings : List.of());
        }
    }

    /**
     * Aggiunge un osservatore da notificare quando una prenotazione termina.
     * @param observer l'osservatore da aggiungere
     */
    public void addObserver(BookingObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifica tutti gli osservatori che una prenotazione è terminata.
     * @param booking la prenotazione conclusa
     */
    private void notifyBookingEnded(Booking booking) {
        for (BookingObserver observer : observers) {
            observer.onBookingEnded(booking);
        }
    }

    /**
     * Aggiorna lo stato delle prenotazioni e notifica se necessario.
     *
     * @param notify se {@code true}, notifica le prenotazioni concluse
     */
    public void checkAllBookingStatuses(boolean notify) {
        for (Booking booking : getAllBookings()) {
            BookingStatus previous = booking.getStatus();
            booking.checkStatus();
            if (previous == BookingStatus.IN_CORSO && booking.getStatus() == BookingStatus.CONCLUSA) {
                if (notify) {
                    notifyBookingEnded(booking);
                }
            }
        }
    }

    /**
     * Tenta di vendere un biglietto per una prenotazione, gestendo l'inventario e registrando il pagamento.
     *
     * @param booking   la prenotazione da completare
     * @param inventory l'inventario da cui sottrarre scarponi eventualmente noleggiati
     * @return true se la vendita è andata a buon fine, false altrimenti
     */
    public boolean sellTicket(Booking booking, Inventory inventory) {
        try {
            Slot slot = booking.getSlot();
            PistaDiPattinaggio pista = booking.getPista();

            if (!pista.getSlotsDisponibili().contains(slot)) {
                logger.warning("Slot non valido per la pista selezionata.");
                return false;
            }

            if (!slot.isAvailable()) {
                logger.warning("Slot non disponibile: " + slot);
                return false;
            }

            for (ShoeRental rental : booking.getRentedShoes()) {
                if (!inventory.isAvailable(rental.getSize(), rental.getQuantity())) {
                    logger.warning("Scarponi non disponibili per taglia " + rental.getSize());
                    return false;
                }
            }

            for (ShoeRental rental : booking.getRentedShoes()) {
                if (!inventory.sellShoes(rental.getSize(), rental.getQuantity())) {
                    logger.warning("Errore nella vendita: taglia " + rental.getSize());
                    return false;
                }
            }

            bookingRepository.add(booking);
            slot.addBooking(booking);
            CashRegister.getInstance().recordPayment(booking.getTicket().getPrice(), booking.getPaymentMethod());
            logger.info("Vendita completata: " + booking);
            return true;

        } catch (Exception e) {
            logger.severe("Errore durante la vendita: " + e.getMessage());
            return false;
        }
    }

    /**
     * Trova il prossimo slot disponibile su una pista specifica, che abbia almeno una certa capienza libera.
     *
     * @param pista       la pista da analizzare
     * @param minCapacity capacità minima richiesta
     * @return uno slot disponibile, se esiste
     */
    public Optional<Slot> findNextAvailableSlot(PistaDiPattinaggio pista, int minCapacity) {
        return pista.getSlotsDisponibili().stream()
                .filter(Slot::isAvailable)
                .filter(slot -> (slot.getCapacity() - slot.getBookings().stream()
                        .mapToInt(b -> b.getTicket().getMaxPeople()).sum()) >= minCapacity)
                .findFirst();
    }

    /**
     * Restituisce gli slot disponibili per una pista specifica.
     *
     * @param pista la pista selezionata
     * @return lista degli slot disponibili
     */
    public List<Slot> getSlotDisponibili(PistaDiPattinaggio pista) {
        return pista.getSlotsDisponibili().stream()
                .filter(Slot::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * Restituisce tutte le prenotazioni salvate.
     *
     * @return lista delle prenotazioni
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.getAll();
    }

    /**
     * Aggiunge una nuova prenotazione all'elenco.
     *
     * @param booking la prenotazione da aggiungere
     */
    public void addBooking(Booking booking) {
        bookingRepository.add(booking);
    }
}
