package org.example.progettooop_pistapattinaggio.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final Customer customer;
    private final Ticket ticket;
    private final List<ShoeRental> rentedShoes;
    private final String paymentMethod;
    private final LocalDateTime bookingTime;
    private BookingStatus status;

    public Booking(Customer customer, Ticket ticket, List<ShoeRental> rentedShoes, String paymentMethod) {
        this.id = UUID.randomUUID().toString();
        this.customer = customer;
        this.ticket = ticket;
        this.rentedShoes = rentedShoes;
        this.paymentMethod = paymentMethod;
        this.bookingTime = LocalDateTime.now();
        this.status = BookingStatus.IN_CORSO;
    }

    // Getter
    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public List<ShoeRental> getRentedShoes() {
        return rentedShoes;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    // Timer
    public long getMinutesRemaining() {
        long elapsed = Duration.between(bookingTime, LocalDateTime.now()).toMinutes();
        return Math.max(ticket.getDurationMinutes() - elapsed, 0);
    }

    public boolean isExpired() {
        return getMinutesRemaining() == 0;
    }

    public void checkStatus() {
        if (status == BookingStatus.IN_CORSO && isExpired()) {
            this.status = BookingStatus.CONCLUSA;
        }
    }

    @Override
    public String toString() {
        return "[" + id + "] " + customer.getName() + " - " + ticket.getName() + " (" + status + ")";
    }
}
