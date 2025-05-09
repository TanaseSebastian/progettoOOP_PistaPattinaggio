package org.example.progettooop_pistapattinaggio;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.example.progettooop_pistapattinaggio.model.*;
import org.example.progettooop_pistapattinaggio.service.BookingService;
import org.example.progettooop_pistapattinaggio.util.*;

import java.util.List;

public class DashboardController {

    // Tabella per l'inventario
    @FXML private TableView<InventoryItem> inventoryTable;
    @FXML private TableColumn<InventoryItem, Integer> shoeSizeColumn;
    @FXML private TableColumn<InventoryItem, Integer> quantityColumn;

    // Tabella per le prenotazioni
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> customerNameColumn;
    @FXML private TableColumn<Booking, String> ticketTypeColumn;

    @FXML private Text totalSales;

    private BookingService bookingService = new BookingService();
    private Inventory inventory = DataManager.loadInventory();  // Carica l'inventario da file JSON

    @FXML
    private void initialize() {
        // Inizializza le colonne della tabella per visualizzare l'inventario
        shoeSizeColumn.setCellValueFactory(cellData -> cellData.getValue().getShoeSizeProperty().asObject());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityProperty().asObject());

        // Popola la tabella con i dati dell'inventario
        inventoryTable.getItems().setAll(inventory.getInventoryList());

        // Inizializza le colonne della tabella per visualizzare le prenotazioni
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomer().getName());
        ticketTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getTicket().getName());

        // Popola la tabella con le prenotazioni esistenti
        handleViewBookings();
    }

    @FXML
    private void handleNewBooking() {
        // Logica per aggiungere una nuova prenotazione
        System.out.println("Aggiungi una nuova prenotazione");
    }

    @FXML
    private void handleViewBookings() {
        // Popolare la tabella con le prenotazioni esistenti
        List<Booking> bookings = bookingService.getAllBookings();
        bookingsTable.getItems().setAll(bookings);
    }

    @FXML
    private void handleGenerateReport() {
        // Logica per generare il report delle vendite
        double totalSalesAmount = 0;  // Calcola il totale delle vendite
        totalSales.setText("€ " + totalSalesAmount);
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
