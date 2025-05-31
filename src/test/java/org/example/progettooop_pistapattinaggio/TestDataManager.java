package org.example.progettooop_pistapattinaggio;

import org.example.progettooop_pistapattinaggio.factory.TicketFactory;
import org.example.progettooop_pistapattinaggio.model.*;
import org.example.progettooop_pistapattinaggio.util.CashRegister;
import org.example.progettooop_pistapattinaggio.util.DataManager;
import org.example.progettooop_pistapattinaggio.util.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per verificare il corretto salvataggio e caricamento dei dati tramite DataManager.
 */
public class TestDataManager {

    private Inventory inventory;
    private Booking booking;
    private Slot slot;
    private CashRegister cashRegister;
    private Customer customer;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        // Setup iniziale
        inventory = new Inventory();
        inventory.addShoes(36, 10);

        customer = new Customer("Mario Rossi", 35, "3331234567", true);
        ticket = TicketFactory.createTicket("single30");

        slot = new Slot(LocalDateTime.now().plusHours(1), 60, 10);
        booking = new Booking(customer, ticket, List.of(new ShoeRental(36, 1)), "Cash", slot);

        cashRegister = CashRegister.getInstance();
        cashRegister.resetPayments();
    }

    @Test
    void testSaveAndLoadInventory() {
        DataManager.saveInventory(inventory);
        Inventory loadedInventory = DataManager.loadInventory();

        assertNotNull(loadedInventory, "L'inventario dovrebbe essere caricato correttamente.");
        assertEquals(10, loadedInventory.getShoesQuantity(36), "Dovrebbero esserci 10 pattini della taglia 36.");
    }

    @Test
    void testSaveAndLoadBookings() {
        DataManager.saveBookings(List.of(booking));
        List<Booking> loadedBookings = DataManager.loadBookings();

        assertNotNull(loadedBookings, "Le prenotazioni dovrebbero essere caricate correttamente.");
        assertEquals(1, loadedBookings.size(), "Dovrebbe esserci almeno una prenotazione.");
        assertEquals(customer.getName(), loadedBookings.get(0).getCustomer().getName(),
                "Il cliente della prenotazione dovrebbe essere 'Mario Rossi'.");
    }

    @Test
    void testSaveAndLoadSlot() {
        DataManager.saveSlot(slot);
        Slot loadedSlot = DataManager.loadSlot();

        assertNotNull(loadedSlot, "Lo Slot dovrebbe essere caricato correttamente.");
        assertEquals(slot.getStartTime(), loadedSlot.getStartTime(), "L'orario di inizio dovrebbe corrispondere.");
        assertEquals(slot.getCapacity(), loadedSlot.getCapacity(), "La capacità dovrebbe corrispondere.");
    }

    @Test
    void testSaveAndLoadCashRegister() {
        cashRegister.recordPayment(20.0, "Cash");
        cashRegister.recordPayment(30.0, "Card");

        DataManager.saveCashRegister(cashRegister);
        CashRegister loadedCashRegister = DataManager.loadCashRegister();

        assertNotNull(loadedCashRegister, "Il Cash Register dovrebbe essere caricato correttamente.");
        assertEquals(20.0, loadedCashRegister.getTotalCash(), "Totale contante errato.");
        assertEquals(30.0, loadedCashRegister.getTotalCard(), "Totale carta errato.");
        assertEquals(50.0, loadedCashRegister.getTotalIncome(), "Totale incassato errato.");
    }
}
