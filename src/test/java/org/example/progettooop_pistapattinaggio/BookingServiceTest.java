package org.example.progettooop_pistapattinaggio;

import org.example.progettooop_pistapattinaggio.factory.PistaBuilder;
import org.example.progettooop_pistapattinaggio.factory.TicketFactory;
import org.example.progettooop_pistapattinaggio.model.*;
import org.example.progettooop_pistapattinaggio.service.BookingService;
import org.example.progettooop_pistapattinaggio.util.CashRegister;
import org.example.progettooop_pistapattinaggio.util.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe BookingService.
 */
class BookingServiceTest {

    private BookingService bookingService;
    private PistaDiPattinaggio pista;
    private Slot slot;
    private Customer customer;
    private Ticket ticket;
    private Inventory inventory;
    private CashRegister cashRegister;

    /**
     * Imposta l’ambiente di test creando una pista con slot, cliente,
     * biglietto, inventario e registratore di cassa inizializzati.
     */
    @BeforeEach
    void setUp() {
        pista = new PistaBuilder()
                .setNome("Pista Test")
                .setIndirizzo("Via dei Test")
                .setTipo("indoor")
                .setSuperficieMq(500)
                .setCoperta(true)
                .setNote("Pista di test")
                .configuraSlotGiornalieri(18, 22, 60, 50)
                .build();

        bookingService = new BookingService(pista, false);
        slot = pista.getSlotsDisponibili().get(0);
        customer = new Customer("Mario Rossi", 35, "3331234567", true);
        ticket = TicketFactory.createTicket("single30");

        inventory = new Inventory();
        inventory.addShoes(36, 10);

        cashRegister = CashRegister.getInstance();
        cashRegister.resetPayments();
    }

    @Test
    void testSellTicketSuccess() {
        Booking booking = new Booking(customer, ticket, List.of(new ShoeRental(36, 1)), "Cash", slot, pista);
        boolean result = bookingService.sellTicket(booking, inventory);
        assertTrue(result);
        assertEquals(1, slot.getBookings().size());
        assertEquals(9, inventory.getShoesQuantity(36));
    }

    @Test
    void testSellTicketFailureDueToNoShoes() {
        Booking booking = new Booking(customer, ticket, List.of(new ShoeRental(32, 5)), "Card", slot, pista);
        boolean result = bookingService.sellTicket(booking, inventory);
        assertFalse(result);
    }

    @Test
    void testSellTicketFailureDueToSlotFull() {
        Slot fullSlot = new Slot(slot.getStartTime(), slot.getDurationMinutes(), 0);
        Booking booking = new Booking(customer, ticket, List.of(new ShoeRental(36, 1)), "Cash", fullSlot, pista);
        boolean result = bookingService.sellTicket(booking, inventory);
        assertFalse(result);
    }

    @Test
    void testCashRegisterRecordPayment() {
        Booking bookingCash = new Booking(customer, ticket, List.of(new ShoeRental(36, 1)), "Cash", slot, pista);
        boolean result = bookingService.sellTicket(bookingCash, inventory);
        assertTrue(result);
        assertEquals(1, cashRegister.getPaymentMethods().get("Cash"));
        assertEquals(ticket.getPrice(), cashRegister.getTotalCash());

        Booking bookingCard = new Booking(customer, ticket, List.of(new ShoeRental(36, 1)), "Card", slot, pista);
        bookingService.sellTicket(bookingCard, inventory);
        assertEquals(1, cashRegister.getPaymentMethods().get("Card"));
        assertEquals(ticket.getPrice(), cashRegister.getTotalCard());
    }

    @Test
    void testGetAllBookings() {
        PistaDiPattinaggio nuovaPista = new PistaBuilder()
                .setNome("Pista Isolata")
                .setIndirizzo("Via isolata")
                .setTipo("indoor")
                .setSuperficieMq(300)
                .setCoperta(true)
                .setNote("Test isolato")
                .configuraSlotGiornalieri(18, 22, 60, 50)
                .build();

        BookingService bookingServiceIsolato = new BookingService(nuovaPista, false);
        Inventory inventarioIsolato = new Inventory();
        inventarioIsolato.addShoes(36, 10);
        Slot slotIsolato = nuovaPista.getSlotsDisponibili().get(0);

        Ticket nuovoTicket = TicketFactory.createTicket("single30");
        Customer customerIsolato = new Customer("Luca Verdi", 30, "3339876543", true);
        Booking booking = new Booking(customerIsolato, nuovoTicket, List.of(new ShoeRental(36, 1)), "Card", slotIsolato, nuovaPista);
        bookingServiceIsolato.sellTicket(booking, inventarioIsolato);

        List<Booking> bookings = bookingServiceIsolato.getAllBookings();
        assertEquals(1, bookings.size());
        assertEquals("Luca Verdi", bookings.get(0).getCustomer().getName());
    }
}
