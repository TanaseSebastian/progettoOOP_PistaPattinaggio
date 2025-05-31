package org.example.progettooop_pistapattinaggio;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import org.example.progettooop_pistapattinaggio.factory.PistaBuilder;
import org.example.progettooop_pistapattinaggio.factory.TicketFactory;
import org.example.progettooop_pistapattinaggio.model.*;
import org.example.progettooop_pistapattinaggio.observer.VoiceNotifier;
import org.example.progettooop_pistapattinaggio.service.BookingService;
import org.example.progettooop_pistapattinaggio.strategy.BookingSortStrategy;
import org.example.progettooop_pistapattinaggio.strategy.PriceDescendingStrategy;
import org.example.progettooop_pistapattinaggio.util.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller principale della dashboard dell'applicazione di gestione pista pattinaggio.
 * Gestisce l'interfaccia utente, le prenotazioni, l'inventario e la logica di stampa.
 */
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
    @FXML private ComboBox<Slot> slotComboBox;


    @FXML private Label totalSales;
    @FXML private TableColumn<Booking, String> shoeInfoColumn;
    private BookingService bookingService;
    private Inventory inventory = DataManager.loadInventory();
    private BookingSortStrategy currentStrategy = new PriceDescendingStrategy(); // oppure new TimeLeftStrategy();
    @FXML private ListView<String> shoeRentalListView;
    private final List<ShoeRental> tempShoeRentals = new ArrayList<>();

    @FXML private TableColumn<Booking, String> bookingTimeColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    @FXML private TableColumn<Booking, String> timeLeftColumn;

    @FXML private ComboBox<String> pistaComboBox;

    private PistaMultipla pistaMultipla;
    private boolean firstLoad = true;

    @FXML
    private void handleAddShoeRental() {
        try {
            int size = Integer.parseInt(bookingShoeSizeField.getText());
            int qty = Integer.parseInt(bookingQuantityField.getText());
            if (!inventory.isAvailable(size, qty)) {
                throw new IllegalArgumentException("Pattini non disponibili per la taglia " + size);
            }

            tempShoeRentals.add(new ShoeRental(size, qty));
            shoeRentalListView.getItems().add(qty + "x" + size);
            bookingShoeSizeField.clear();
            bookingQuantityField.clear();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText("Inserisci solo numeri validi.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void initialize() {
        // Creazione piste base tramite builder
        PistaDiPattinaggio pistaA = new PistaBuilder()
                .setNome("Pista A")
                .setIndirizzo("Via Roma 1")
                .setTipo("indoor")
                .setCapienzaMassima(300)
                .setNote("Pista principale")
                .configuraSlotGiornalieri(10, 20, 60, 100)
                .build();

        PistaDiPattinaggio pistaB = new PistaBuilder()
                .setNome("Pista B")
                .setIndirizzo("Via Roma 1b")
                .setTipo("indoor")
                .setCapienzaMassima(250)
                .setNote("Seconda pista coperta con accesso disabili")
                .configuraSlotGiornalieri(10, 20, 60, 100)
                .build();

        // Costruzione di pista multipla
        pistaMultipla = new PistaMultipla("Centro Pattinaggio", "Via Centrale");
        pistaMultipla.aggiungiPista(pistaA);
        pistaMultipla.aggiungiPista(pistaB);

        // Inizializza il servizio di prenotazione
        bookingService = new BookingService(pistaMultipla, true);
        bookingService.addObserver(new VoiceNotifier());
        // Inizializza le colonne della tabella per visualizzare l'inventario
        shoeSizeColumn.setCellValueFactory(cellData -> cellData.getValue().getShoeSizeProperty().asObject());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityProperty().asObject());
        shoeInfoColumn.setCellValueFactory(cellData -> {
            List<ShoeRental> shoes = cellData.getValue().getRentedShoes();
            String descrizione = shoes.stream()
                    .map(s -> s.getQuantity() + "x" + s.getSize())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("-");
            return new javafx.beans.property.SimpleStringProperty(descrizione);
        });



        // Popola la combo delle piste
        pistaComboBox.getItems().addAll(
                pistaMultipla.getPiste().stream()
                        .map(PistaDiPattinaggio::getNome)
                        .toList()
        );

        // Quando selezioni una pista, carica gli slot di quella pista
        pistaComboBox.setOnAction(event -> {
            String nome = pistaComboBox.getValue();
            if (nome != null) {
                PistaDiPattinaggio selezionata = pistaMultipla.getPistaByNome(nome);
                if (selezionata != null) {
                    List<Slot> disponibili = bookingService.getSlotDisponibili(selezionata);
                    slotComboBox.getItems().setAll(disponibili);
                }
            }
        });

        //popola gli slot
        slotComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Slot slot, boolean empty) {
                super.updateItem(slot, empty);
                setText(empty || slot == null ? null : slot.toString());
            }
        });
        slotComboBox.setButtonCell(slotComboBox.getCellFactory().call(null));

        // Popola la tabella con i dati dell'inventario
        inventoryTable.getItems().setAll(inventory.getInventoryList());

        // Inizializza le colonne della tabella per visualizzare le prenotazioni
        customerNameColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCustomer().getName())
        );

        ticketTypeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTicket().getName())
        );

        // ComboBox Ticket
        ticketTypeCombo.getItems().addAll(TicketFactory.getAllLabels());
        paymentMethodCombo.getItems().addAll("Contanti", "Carta");
        // Popola la tabella con le prenotazioni esistenti
        handleViewBookings();
        updateTotalSales(); // ← aggiorna il totale all'avvio



        bookingTimeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBookingTime().toString())
        );

        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().name())
        );

        timeLeftColumn.setCellValueFactory(cellData -> {
            Booking b = cellData.getValue();
            b.checkStatus(); // aggiorna status se è scaduto
            return new SimpleStringProperty(b.getMinutesRemaining() + " min");
        });

        Timeline refreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(30), e -> handleViewBookings())
        );
        refreshTimeline.setCycleCount(Animation.INDEFINITE);
        refreshTimeline.play();
    }

    private void updateTotalSales() {
        double total = bookingService.getAllBookings().stream()
                .mapToDouble(b -> b.getTicket().getPrice())
                .sum();
        totalSales.setText(String.format("Totale Vendite: € %.2f", total));
    }

    @FXML
    private void handleNewBooking() {
        // Logica per aggiungere una nuova prenotazione
        System.out.println("Aggiungi una nuova prenotazione");
    }

    /**
     * Gestisce l'inserimento di una nuova prenotazione, validando i dati e aggiornando la UI.
     * Mostra un alert e stampa uno scontrino in caso di successo.
     */
    @FXML
    private void handleCreateBooking() {
        try {
            String customerName = customerNameField.getText().trim();
            String ticketLabel = ticketTypeCombo.getValue(); // Etichetta visibile
            String ticketType = TicketFactory.getTypeFromLabel(ticketLabel); // Codice interno
            String paymentMethod = paymentMethodCombo.getValue();
            if (tempShoeRentals.isEmpty()) {
                throw new IllegalArgumentException("Aggiungi almeno una taglia di pattini.");
            }
            if (ticketLabel == null || ticketLabel.isEmpty()) throw new IllegalArgumentException("Seleziona un tipo di biglietto.");
            if (paymentMethod == null || paymentMethod.isEmpty()) throw new IllegalArgumentException("Seleziona un metodo di pagamento.");

            for (ShoeRental sr : tempShoeRentals) {
                if (!inventory.isAvailable(sr.getSize(), sr.getQuantity())) {
                    throw new IllegalArgumentException("Pattini non disponibili per la taglia " + sr.getSize());
                }
            }
            List<ShoeRental> shoes = new ArrayList<>(tempShoeRentals);
            tempShoeRentals.clear();
            shoeRentalListView.getItems().clear();
            if (customerName.isEmpty()) throw new IllegalArgumentException("Il nome cliente è obbligatorio.");

            Customer customer = new Customer(customerName);
            Ticket ticket = TicketFactory.createTicket(ticketType);

            Slot slot = slotComboBox.getValue();
            if (slot == null) throw new IllegalArgumentException("Seleziona uno slot orario.");
            String nomeSelezionato = pistaComboBox.getValue();
            PistaDiPattinaggio pistaSelezionata = pistaMultipla.getPistaByNome(nomeSelezionato);
            Booking booking = new Booking(customer, ticket, shoes, paymentMethod, slot, pistaSelezionata);
            boolean success = bookingService.sellTicket(booking, inventory);
            if (!success) throw new RuntimeException("Errore durante la prenotazione");

            for (ShoeRental sr : shoes) {
                inventory.sellShoes(sr.getSize(), sr.getQuantity());
            }

            DataManager.saveBookings(bookingService.getAllBookings());
            DataManager.saveInventory(inventory);

            bookingsTable.getItems().setAll(bookingService.getAllBookings());
            inventoryTable.getItems().setAll(inventory.getInventoryList());

            customerNameField.clear();
            bookingShoeSizeField.clear();
            bookingQuantityField.clear();

            customerNameField.clear();
            bookingShoeSizeField.clear();
            bookingQuantityField.clear();

            ticketTypeCombo.setValue(null);
            paymentMethodCombo.setValue(null);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Prenotazione Aggiunta");
            String taglie = shoes.stream()
                    .map(sr -> sr.getQuantity() + "x" + sr.getSize())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("-");
            alert.setContentText("✅ Prenotazione per " + customerName + " creata con successo!\nTaglie: " + taglie);
            alert.showAndWait();
            PrinterManager.printTicket(booking);
            updateTotalSales();

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

    /**
     * Aggiorna la tabella delle prenotazioni applicando la strategia di ordinamento selezionata.
     * Se è il primo caricamento, evita le notifiche vocali.
     */
    @FXML
    private void handleViewBookings() {
        if (firstLoad) {
            // Al primo caricamento non notificare
            bookingService.checkAllBookingStatuses(false); // ← passiamo false
            firstLoad = false;
        } else {
            bookingService.checkAllBookingStatuses(true); // ← notifiche attive
        }

        List<Booking> bookings = bookingService.getAllBookings();
        List<Booking> sorted = currentStrategy.sort(bookings);
        bookingsTable.getItems().setAll(sorted);
        DataManager.saveBookings(sorted);
    }

    /**
     * Stampa un report riepilogativo delle prenotazioni della giornata.
     */
    @FXML
    private void handleGenerateReport() {
        List<Booking> allBookings = bookingService.getAllBookings();

        LocalDate today = LocalDate.now();

        List<Booking> todaysBookings = allBookings.stream()
                .filter(b -> b.getBookingTime().toLocalDate().equals(today))
                .toList();

        double total = todaysBookings.stream()
                .mapToDouble(b -> b.getTicket().getPrice())
                .sum();

        String clienti = todaysBookings.stream()
                .map(b -> "- " + b.getCustomer().getName() + " (" + b.getTicket().getName() + ")")
                .reduce("", (a, b) -> a + b + "\n");

        Alert report = new Alert(Alert.AlertType.INFORMATION);
        report.setTitle("Report Giornaliero");
        report.setHeaderText("📅 " + today);
        report.setContentText("Prenotazioni totali: " + todaysBookings.size() +
                "\nIncasso del giorno: € " + String.format("%.2f", total) +
                "\n\nClienti:\n" + clienti);
        report.setResizable(true);
        report.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        report.showAndWait();

        PrinterManager.printReport(today, todaysBookings);
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
