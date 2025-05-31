package org.example.progettooop_pistapattinaggio.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Rappresenta una prenotazione effettuata da un cliente per accedere a una pista di pattinaggio.
 * Contiene tutte le informazioni necessarie come cliente, biglietto, noleggi, metodo di pagamento, orario, stato, slot e pista associata.
 */
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final Customer customer;
    private final Ticket ticket;
    private final List<ShoeRental> rentedShoes;
    private final String paymentMethod;
    private final LocalDateTime bookingTime;
    private BookingStatus status;
    private final Slot slot;
    private final PistaDiPattinaggio pista;

    /**
     * Costruttore per creare una nuova prenotazione.
     *
     * @param customer       il cliente che effettua la prenotazione
     * @param ticket         il biglietto scelto
     * @param rentedShoes    la lista delle scarpe noleggiate
     * @param paymentMethod  il metodo di pagamento utilizzato
     * @param slot           lo slot temporale prenotato
     * @param pista          la pista di pattinaggio scelta
     */
    public Booking(Customer customer, Ticket ticket, List<ShoeRental> rentedShoes, String paymentMethod, Slot slot, PistaDiPattinaggio pista) {
        this.id = UUID.randomUUID().toString();
        this.customer = customer;
        this.ticket = ticket;
        this.rentedShoes = rentedShoes;
        this.paymentMethod = paymentMethod;
        this.bookingTime = LocalDateTime.now();
        this.status = BookingStatus.IN_CORSO;
        this.slot = slot;
        this.pista = pista;
    }

    // Getter

    /**
     * Restituisce l'ID univoco della prenotazione.
     *
     * @return ID della prenotazione
     */
    public String getId() {
        return id;
    }

    /**
     * Restituisce il cliente associato alla prenotazione.
     *
     * @return cliente
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Restituisce il biglietto acquistato per la prenotazione.
     *
     * @return ticket
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     * Restituisce la lista di scarpe noleggiate.
     *
     * @return lista di noleggi
     */
    public List<ShoeRental> getRentedShoes() {
        return rentedShoes;
    }

    /**
     * Restituisce il metodo di pagamento utilizzato.
     *
     * @return metodo di pagamento
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Restituisce la data e l'orario in cui è stata effettuata la prenotazione.
     *
     * @return orario prenotazione
     */
    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    /**
     * Restituisce lo stato corrente della prenotazione.
     *
     * @return stato della prenotazione
     */
    public BookingStatus getStatus() {
        return status;
    }

    /**
     * Imposta un nuovo stato per la prenotazione.
     *
     * @param status nuovo stato
     */
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    /**
     * Restituisce lo slot temporale prenotato.
     *
     * @return slot
     */
    public Slot getSlot() {
        return slot;
    }

    /**
     * Restituisce la pista di pattinaggio associata alla prenotazione.
     *
     * @return pista di pattinaggio
     */
    public PistaDiPattinaggio getPista() {
        return pista;
    }

    // Timer

    /**
     * Calcola i minuti rimanenti prima della scadenza del biglietto.
     *
     * @return minuti rimanenti
     */
    public long getMinutesRemaining() {
        long elapsed = Duration.between(bookingTime, LocalDateTime.now()).toMinutes();
        return Math.max(ticket.getDurationMinutes() - elapsed, 0);
    }

    /**
     * Verifica se la prenotazione è scaduta.
     *
     * @return {@code true} se scaduta, altrimenti {@code false}
     */
    public boolean isExpired() {
        return getMinutesRemaining() == 0;
    }

    /**
     * Controlla lo stato della prenotazione e lo aggiorna a {@code CONCLUSA} se è scaduta.
     */
    public void checkStatus() {
        if (status == BookingStatus.IN_CORSO && isExpired()) {
            this.status = BookingStatus.CONCLUSA;
        }
    }

    /**
     * Restituisce una rappresentazione testuale della prenotazione.
     *
     * @return stringa rappresentativa
     */
    @Override
    public String toString() {
        return "[" + id + "] " + customer.getName() + " - " + ticket.getName() + " (" + status + ")";
    }
}
