package org.example.progettooop_pistapattinaggio;

import org.example.progettooop_pistapattinaggio.model.*;
import org.example.progettooop_pistapattinaggio.service.BookingService;
import org.example.progettooop_pistapattinaggio.factory.TicketFactory;
import org.example.progettooop_pistapattinaggio.util.Inventory;
import org.example.progettooop_pistapattinaggio.util.CashRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {

    private BookingService bookingService;
    private Slot slot;
    private Customer customer;
    private Ticket ticket;
    private Inventory inventory;
    private CashRegister cashRegister;

    @BeforeEach
    void setUp() {
        // Setup test data
        bookingService = new BookingService();
        slot = new Slot(LocalDateTime.now(), 60, 10); // Slot da 60 min con capienza 10
        customer = new Customer("Mario Rossi", 35, "3331234567", true); // Cliente
        ticket = TicketFactory.createTicket("single30"); // Usa TicketFactory per creare il biglietto
        inventory = new Inventory();
        inventory.addShoes(36, 10);  // Aggiungi 10 pattini della taglia 36
        cashRegister = CashRegister.getInstance(); // Usa il Singleton del Cash Register
        cashRegister.resetPayments();
    }

    @Test
    void testSellTicketSuccess() {
        // Vendi il biglietto e registra la vendita
        boolean result = bookingService.sellTicket(slot, customer, ticket, 36, 1, inventory, "Cash");

        // Verifica che la vendita sia riuscita
        assertTrue(result, "La vendita dovrebbe riuscire.");
        assertEquals(1, slot.getBookings().size(), "Lo slot dovrebbe avere una prenotazione.");
        assertEquals(9, inventory.getShoesQuantity(36), "L'inventario dei pattini taglia 36 dovrebbe essere aggiornato.");
    }

    @Test
    void testSellTicketFailureDueToNoShoes() {
        // Proviamo a vendere più scarponi di quelli disponibili
        inventory.addShoes(32, 1);
        boolean result = bookingService.sellTicket(slot, customer, ticket, 32, inventory.getShoesQuantity(36)+1, inventory, "Card");
        // Verifica che la vendita fallisca per mancanza di pattini
        assertFalse(result, "La vendita non dovrebbe riuscire, pattini insufficienti.");
    }

    @Test
    void testSellTicketFailureDueToSlotFull() {
        // Impostiamo uno slot con zero posti disponibili
        Slot fullSlot = new Slot(LocalDateTime.now(), 60, 0);
        boolean result = bookingService.sellTicket(fullSlot, customer, ticket, 36, 1, inventory, "Cash");

        // Verifica che la vendita fallisca per slot pieno
        assertFalse(result, "La vendita non dovrebbe riuscire, slot pieno.");
    }

    @Test
    void testCashRegisterRecordPayment() {
        // Vendi un biglietto e registra l'incasso
        boolean result = bookingService.sellTicket(slot, customer, ticket, 36, 1, inventory, "Cash");

        // Verifica che il pagamento in contante sia stato registrato nel Cash Register
        assertTrue(result, "La vendita dovrebbe riuscire.");
        assertEquals(1, cashRegister.getPaymentMethods().get("Cash"), "Il pagamento in contante dovrebbe essere registrato una volta.");
        assertEquals(ticket.getPrice(), cashRegister.getTotalCash(), "L'importo incassato dovrebbe essere uguale al prezzo del biglietto.");

        // Vendi un altro biglietto con pagamento "Card"
        bookingService.sellTicket(slot, customer, ticket, 36, 1, inventory, "Card");

        // Verifica che il pagamento con carta sia stato registrato nel Cash Register
        assertEquals(1, cashRegister.getPaymentMethods().get("Card"), "Il pagamento con carta dovrebbe essere registrato una volta.");
        assertEquals(ticket.getPrice(), cashRegister.getTotalCard(), "L'importo incassato con carta dovrebbe essere uguale al prezzo del biglietto.");
    }



    @Test
    void testGetAllBookings() {
        // Vendi un biglietto
        bookingService.sellTicket(slot, customer, ticket, 36, 1, inventory, "Card");

        // Recuperiamo tutte le prenotazioni
        List<Booking> bookings = bookingService.getAllBookings();

        // Verifica che ci sia almeno una prenotazione
        assertEquals(1, bookings.size(), "Dovrebbe esserci almeno una prenotazione.");
        assertEquals(customer.getName(), bookings.get(0).getCustomer().getName(), "Il cliente della prenotazione dovrebbe essere 'Mario Rossi'.");
    }
}
