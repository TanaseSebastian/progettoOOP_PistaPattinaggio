package org.example.progettooop_pistapattinaggio;

import org.example.progettooop_pistapattinaggio.model.*;
import org.example.progettooop_pistapattinaggio.factory.TicketFactory;
import org.example.progettooop_pistapattinaggio.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestDataManager {

    private Inventory inventory;
    private Booking booking;
    private Slot slot;
    private CashRegister cashRegister;
    private Customer customer;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        // Setup test data
        inventory = DataManager.loadInventory();  // Carica l'inventario da file JSON
        customer = new Customer("Mario Rossi", 35, "3331234567", true); // Cliente
        ticket = TicketFactory.createTicket("single30"); // Usa TicketFactory per creare il biglietto
        booking = new Booking(customer, ticket, 36, 1, "Cash"); // Prenotazione
        slot = new Slot(LocalDateTime.now().plusHours(1), 60, 10); // Slot da 60 min con capienza 10
        cashRegister = CashRegister.getInstance();
        cashRegister.resetPayments();  // Reset CashRegister per ogni test
    }

    @Test
    void testSaveAndLoadInventory() {
        // Salva e carica l'inventario
        DataManager.saveInventory(inventory);
        Inventory loadedInventory = DataManager.loadInventory();

        // Verifica che l'inventario caricato sia corretto
        assertNotNull(loadedInventory, "L'inventario dovrebbe essere caricato correttamente.");
        assertEquals(10, loadedInventory.getShoesQuantity(36), "Dovrebbero esserci 10 pattini della taglia 36.");
    }

    @Test
    void testSaveAndLoadBookings() {
        // Salva la prenotazione
        DataManager.saveBookings(List.of(booking));

        // Carica le prenotazioni
        List<Booking> loadedBookings = DataManager.loadBookings();

        // Verifica che le prenotazioni siano caricate correttamente
        assertNotNull(loadedBookings, "Le prenotazioni dovrebbero essere caricate correttamente.");
        assertEquals(1, loadedBookings.size(), "Dovrebbe esserci almeno una prenotazione.");
        assertEquals(customer.getName(), loadedBookings.get(0).getCustomer().getName(), "Il cliente della prenotazione dovrebbe essere 'Mario Rossi'.");
    }

    @Test
    void testSaveAndLoadSlot() {
        // Salva lo Slot
        DataManager.saveSlot(slot);

        // Carica lo Slot
        Slot loadedSlot = DataManager.loadSlot();

        // Verifica che lo Slot sia caricato correttamente
        assertNotNull(loadedSlot, "Lo Slot dovrebbe essere caricato correttamente.");
        assertEquals(slot.getStartTime(), loadedSlot.getStartTime(), "L'orario di inizio dello slot dovrebbe essere uguale.");
        assertEquals(slot.getCapacity(), loadedSlot.getCapacity(), "La capacità dello slot dovrebbe essere uguale.");
    }

    @Test
    void testSaveAndLoadCashRegister() {
        // Aggiungi pagamenti al Cash Register
        cashRegister.recordPayment(20.0, "Cash");
        cashRegister.recordPayment(30.0, "Card");

        // Salva il CashRegister
        DataManager.saveCashRegister(cashRegister);

        // Carica il CashRegister
        CashRegister loadedCashRegister = DataManager.loadCashRegister();

        // Verifica che il CashRegister sia caricato correttamente
        assertNotNull(loadedCashRegister, "Il Cash Register dovrebbe essere caricato correttamente.");
        assertEquals(20.0, loadedCashRegister.getTotalCash(), "L'importo incassato in contante dovrebbe essere 20.0.");
        assertEquals(30.0, loadedCashRegister.getTotalCard(), "L'importo incassato con carta dovrebbe essere 30.0.");
        assertEquals(50.0, loadedCashRegister.getTotalIncome(), "Il totale incassato dovrebbe essere 50.0.");
    }
}
