package org.example.progettooop_pistapattinaggio;
import javax.print.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.ByteArrayInputStream;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.progettooop_pistapattinaggio.factory.TicketFactory;
import org.example.progettooop_pistapattinaggio.model.*;
import org.example.progettooop_pistapattinaggio.service.BookingService;
import org.example.progettooop_pistapattinaggio.util.*;

import java.time.LocalDate;
import java.util.ArrayList;
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
    @FXML private TableColumn<Booking, String> shoeInfoColumn;
    private BookingService bookingService = new BookingService();
    private Inventory inventory = DataManager.loadInventory();  // Carica l'inventario da file JSON
    @FXML private ListView<String> shoeRentalListView;
    private final List<ShoeRental> tempShoeRentals = new ArrayList<>();

    @FXML private TableColumn<Booking, String> bookingTimeColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    @FXML private TableColumn<Booking, String> timeLeftColumn;

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

            Booking booking = new Booking(customer, ticket, shoes, paymentMethod);
            bookingService.addBooking(booking);
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
            printTicketWithQR(booking);
            updateTotalSales();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Prenotazione non valida");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void printTicketWithQR(Booking booking) {
        try {
            String ESC = "\u001B";
            String NEW_LINE = "\n";

            Customer customer = booking.getCustomer();
            Ticket ticket = booking.getTicket();
            List<ShoeRental> shoes = booking.getRentedShoes();

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            // LOGO ASCII (puoi sostituirlo con grafico se vuoi)
            output.write((ESC + "@").getBytes()); // Init
            output.write((ESC + "!").getBytes()); // Style
            output.write("\u001B!\u0038".getBytes()); // Bold + double
            output.write("PISTA PATTINAGGIO\n".getBytes());
            output.write("\u001B!\u0000".getBytes()); // Normal
            output.write("--------------------------------\n".getBytes());

            output.write(("Cliente: " + customer.getName() + NEW_LINE).getBytes());
            output.write(("Biglietto: " + ticket.getName() + NEW_LINE).getBytes());
            output.write(("Prezzo: " + String.format("%.2f", ticket.getPrice()) + " euro" + NEW_LINE).getBytes());
            output.write(("Metodo: " + booking.getPaymentMethod() + NEW_LINE).getBytes());

            output.write("Taglie pattini:\n".getBytes());
            for (ShoeRental sr : shoes) {
                output.write((" - " + sr.getQuantity() + "x" + sr.getSize() + NEW_LINE).getBytes());
            }
            output.write("--------------------------------\n".getBytes());

            // GENERA QR CON ID
            BufferedImage qr = generateQR(booking.getId());
            byte[] qrBytes = convertImageToESCBitmap(qr);
            output.write(qrBytes);

            output.write((NEW_LINE + "Prenotazione ID:\n" + booking.getId() + NEW_LINE).getBytes());
            output.write(NEW_LINE.getBytes());
            output.write("Grazie e buona pattinata!\n\n\n".getBytes());
            output.write((ESC + "d" + "\u0004").getBytes()); // Feed
            output.write((ESC + "m").getBytes()); // Cut

            // INVIO STAMPA
            byte[] data = output.toByteArray();
            Doc doc = new SimpleDoc(new ByteArrayInputStream(data), DocFlavor.INPUT_STREAM.AUTOSENSE, null);

            PrintService escposPrinter = null;
            for (PrintService ps : PrintServiceLookup.lookupPrintServices(null, null)) {
                if (ps.getName().toLowerCase().contains("yichip")) {
                    escposPrinter = ps;
                    break;
                }
            }

            if (escposPrinter == null) {
                System.err.println("Stampante ESC/POS non trovata");
                return;
            }

            DocPrintJob job = escposPrinter.createPrintJob();
            job.print(doc, new HashPrintRequestAttributeSet());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedImage generateQR(String text) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    private byte[] convertImageToESCBitmap(BufferedImage image) throws IOException {
        // Convert image to monochrome ESC/POS format
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int width = image.getWidth();
        int height = image.getHeight();

        bos.write(new byte[]{0x1D, 0x76, 0x30, 0x00}); // Raster bit image command
        bos.write((byte) (width / 8)); // width in bytes (low byte)
        bos.write(0); // width high byte
        bos.write((byte) (height & 0xFF)); // height low byte
        bos.write((byte) ((height >> 8) & 0xFF)); // height high byte

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x += 8) {
                int byteVal = 0;
                for (int b = 0; b < 8; b++) {
                    int px = x + b;
                    if (px < width) {
                        int color = image.getRGB(px, y);
                        int r = (color >> 16) & 0xFF;
                        int g = (color >> 8) & 0xFF;
                        int bl = color & 0xFF;
                        int avg = (r + g + bl) / 3;
                        if (avg < 128) {
                            byteVal |= (1 << (7 - b));
                        }
                    }
                }
                bos.write(byteVal);
            }
        }

        return bos.toByteArray();
    }

    private void printReportToESC_POS(LocalDate date, List<Booking> bookings) {
        try {
            String ESC = "\u001B";
            String NEW_LINE = "\n";
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            output.write((ESC + "@").getBytes()); // Init

            output.write(ESC.getBytes());
            output.write("!".getBytes());
            output.write(new byte[]{0x38}); // Bold + Double height/width

            output.write(" REPORT GIORNALIERO\n".getBytes());

            output.write(ESC.getBytes());
            output.write("!".getBytes());
            output.write(new byte[]{0x00}); // Reset style

            output.write(("Data: " + date.toString() + NEW_LINE).getBytes());
            output.write("--------------------------------\n".getBytes());

            double total = 0.0;
            for (Booking b : bookings) {
                String line = "- " + b.getCustomer().getName() + " (" + b.getTicket().getName() + ")\n";
                output.write(line.getBytes());
                total += b.getTicket().getPrice();
            }

            output.write("--------------------------------\n".getBytes());
            output.write(("Totale prenotazioni: " + bookings.size() + NEW_LINE).getBytes());
            output.write(("Incasso: " + String.format("%.2f", total) + " euro\n").getBytes());
            output.write("\nGrazie!\n\n".getBytes());

            output.write(ESC.getBytes());
            output.write("d".getBytes());
            output.write(new byte[]{0x04}); // Feed 4 lines

            output.write(ESC.getBytes());
            output.write("m".getBytes()); // Cut

            byte[] data = output.toByteArray();
            Doc doc = new SimpleDoc(new ByteArrayInputStream(data), DocFlavor.INPUT_STREAM.AUTOSENSE, null);

            PrintService escposPrinter = null;
            for (PrintService ps : PrintServiceLookup.lookupPrintServices(null, null)) {
                if (ps.getName().toLowerCase().contains("yichip")) {
                    escposPrinter = ps;
                    break;
                }
            }

            if (escposPrinter == null) {
                System.err.println("Stampante ESC/POS non trovata");
                return;
            }

            DocPrintJob job = escposPrinter.createPrintJob();
            job.print(doc, new HashPrintRequestAttributeSet());

        } catch (Exception e) {
            e.printStackTrace();
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
        List<Booking> bookings = bookingService.getAllBookings();
        bookings.forEach(Booking::checkStatus); // aggiorna stato se scaduto
        bookingsTable.getItems().setAll(bookings);
        DataManager.saveBookings(bookings); // salva aggiornamenti
    }

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

        printReportToESC_POS(today, todaysBookings);
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
