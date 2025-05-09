package org.example.progettooop_pistapattinaggio;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.example.progettooop_pistapattinaggio.factory.TicketFactory;
import org.example.progettooop_pistapattinaggio.model.*;
import org.example.progettooop_pistapattinaggio.service.BookingService;
import org.example.progettooop_pistapattinaggio.util.*;

import java.util.List;

public class DashboardController {

    // Tabella per l'inventario
    @FXML private TableView<InventoryItem> inventoryTable;
    @FXML private TableColumn<InventoryItem, Integer> shoeSizeColumn;
    @FXML private TableColumn<InventoryItem, Integer> quantityColumn;
    @FXML private TextField newShoeSizeField;
    @FXML private TextField newQuantityField;


    // Tabella per le prenotazioni
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> customerNameColumn;
    @FXML private TableColumn<Booking, String> ticketTypeColumn;
    @FXML private TextField customerNameField;
    @FXML private ComboBox<String> ticketTypeCombo;
    @FXML private TextField bookingShoeSizeField;
    @FXML private TextField bookingQuantityField;
    @FXML private ComboBox<String> paymentMethodCombo;


    @FXML private Label totalSales;
    @FXML private TableColumn<Booking, Integer> bookingShoeSizeColumn;
    @FXML private TableColumn<Booking, Integer> bookingQuantityColumn;
    private BookingService bookingService = new BookingService();
    private Inventory inventory = DataManager.loadInventory();  // Carica l'inventario da file JSON

    @FXML
    private void initialize() {
        // Inizializza le colonne della tabella per visualizzare l'inventario
        shoeSizeColumn.setCellValueFactory(cellData -> cellData.getValue().getShoeSizeProperty().asObject());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityProperty().asObject());
        bookingShoeSizeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getShoeSize()).asObject());

        bookingQuantityColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getShoeQuantity()).asObject());

        // Popola la tabella con i dati dell'inventario
        inventoryTable.getItems().setAll(inventory.getInventoryList());

        // Inizializza le colonne della tabella per visualizzare le prenotazioni
        customerNameColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCustomer().getName())
        );

        ticketTypeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTicket().getName())
        );


        ticketTypeCombo.getItems().addAll("single30", "single60", "couple30", "couple60", "family", "pass");
        paymentMethodCombo.getItems().addAll("Contanti", "Carta");
        // Popola la tabella con le prenotazioni esistenti
        handleViewBookings();
    }

    @FXML
    private void handleNewBooking() {
        // Logica per aggiungere una nuova prenotazione
        System.out.println("Aggiungi una nuova prenotazione");
    }

    @FXML
    private void handleCreateBooking() {
        try {
            String customerName = customerNameField.getText().trim();
            String ticketType = ticketTypeCombo.getValue();
            String paymentMethod = paymentMethodCombo.getValue();
            int shoeSize = Integer.parseInt(bookingShoeSizeField.getText());
            int quantity = Integer.parseInt(bookingQuantityField.getText());

            if (customerName.isEmpty()) throw new IllegalArgumentException("Il nome cliente è obbligatorio.");
            if (!inventory.isAvailable(shoeSize, quantity)) {
                throw new IllegalArgumentException("Pattini non disponibili per la taglia richiesta.");
            }

            Customer customer = new Customer(customerName);
            Ticket ticket = TicketFactory.createTicket(ticketType);
            Booking booking = new Booking(customer, ticket, shoeSize, quantity, paymentMethod);

            // Aggiorna dati
            bookingService.addBooking(booking);
            inventory.sellShoes(shoeSize, quantity);

            DataManager.saveBookings(bookingService.getAllBookings());
            DataManager.saveInventory(inventory);

            bookingsTable.getItems().setAll(bookingService.getAllBookings());
            inventoryTable.getItems().setAll(inventory.getInventoryList());

            // Pulisce i campi
            customerNameField.clear();
            bookingShoeSizeField.clear();
            bookingQuantityField.clear();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Prenotazione Aggiunta");
            alert.setContentText("✅ Prenotazione per " + customerName + " creata con successo!");
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Prenotazione non valida");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    private void handleAddInventoryItem() {
        try {
            int size = Integer.parseInt(newShoeSizeField.getText());
            int quantity = Integer.parseInt(newQuantityField.getText());

            InventoryItem newItem = new InventoryItem(size, quantity);
            inventory.addItem(newItem);
            DataManager.saveInventory(inventory);

            inventoryTable.getItems().setAll(inventory.getInventoryList());

            newShoeSizeField.clear();
            newQuantityField.clear();

            System.out.println("✅ Nuovo pattino inserito: taglia " + size + ", quantità " + quantity);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Inserimento non valido");
            alert.setContentText("Inserisci solo numeri interi per taglia e quantità.");
            alert.showAndWait();
        }
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
