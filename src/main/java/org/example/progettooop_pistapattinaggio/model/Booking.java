package org.example.progettooop_pistapattinaggio.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;  // Versione serializzazione

    private final Customer customer;
    private final Ticket ticket;
    private final int shoeSize;  // Numero scarpa
    private final int shoeQuantity;  // Quantità pattini
    private final String paymentMethod;  // Metodo di pagamento
    private final LocalDateTime bookingTime;

    // Costruttore
    public Booking(Customer customer, Ticket ticket, int shoeSize, int shoeQuantity, String paymentMethod) {
        this.customer = customer;
        this.ticket = ticket;
        this.shoeSize = shoeSize;
        this.shoeQuantity = shoeQuantity;
        this.paymentMethod = paymentMethod;
        this.bookingTime = LocalDateTime.now();
    }

    // Getters
    public Customer getCustomer() {
        return customer;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public int getShoeSize() {
        return shoeSize;
    }

    public int getShoeQuantity() {
        return shoeQuantity;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    // Metodo per la stampa
    @Override
    public String toString() {
        return customer.getName() + " ha acquistato un " + ticket.getName() +
                " alle " + bookingTime + " [" + shoeQuantity + " pattini taglia " + shoeSize + "] - Metodo di pagamento: " + paymentMethod;
    }
}
