package org.example.progettooop_pistapattinaggio.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.example.progettooop_pistapattinaggio.model.*;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@code PrinterManager} gestisce la stampa di scontrini e report giornalieri per la pista di pattinaggio.
 * Supporta la stampa con codice QR e la memorizzazione della stampante predefinita.
 */
public class PrinterManager {

    private static final String ESC = "\u001B";
    private static final String NEW_LINE = "\n";
    private static final String CONFIG_FILE =
            System.getProperty("user.home") + File.separator + ".pista_printer.properties";

    /**
     * Stampa uno scontrino per una prenotazione, includendo cliente, biglietto, pattini e QR code.
     *
     * @param booking prenotazione da stampare
     */
    public static void printTicket(Booking booking) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            Customer customer = booking.getCustomer();
            Ticket ticket = booking.getTicket();
            List<ShoeRental> shoes = booking.getRentedShoes();

            output.write((ESC + "@").getBytes()); // Reset
            output.write((ESC + "!").getBytes()); // Font size
            output.write(new byte[]{0x38});       // Double size
            output.write("PISTA PATTINAGGIO\n".getBytes());
            output.write((ESC + "!").getBytes());
            output.write(new byte[]{0x00});
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

            BufferedImage qr = generateQR(booking.getId());
            byte[] qrBytes = convertImageToESCBitmap(qr);
            output.write(qrBytes);

            output.write((NEW_LINE + "Prenotazione ID:\n" + booking.getId() + NEW_LINE).getBytes());
            output.write("Grazie e buona pattinata!\n\n\n".getBytes());
            output.write((ESC + "d").getBytes());
            output.write(new byte[]{0x04});
            output.write((ESC + "m").getBytes());

            sendToPrinter(output.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stampa un report giornaliero con tutte le prenotazioni effettuate e il totale incassato.
     *
     * @param date     data del report
     * @param bookings lista delle prenotazioni da includere
     */
    public static void printReport(LocalDate date, List<Booking> bookings) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            output.write((ESC + "@").getBytes());
            output.write((ESC + "!").getBytes());
            output.write(new byte[]{0x38});
            output.write(" REPORT GIORNALIERO\n".getBytes());
            output.write((ESC + "!").getBytes());
            output.write(new byte[]{0x00});
            output.write(("Data: " + date.toString() + NEW_LINE).getBytes());
            output.write("--------------------------------\n".getBytes());

            double total = 0.0;
            for (Booking b : bookings) {
                output.write(("- " + b.getCustomer().getName() + " (" + b.getTicket().getName() + ")\n").getBytes());
                total += b.getTicket().getPrice();
            }

            output.write("--------------------------------\n".getBytes());
            output.write(("Totale prenotazioni: " + bookings.size() + NEW_LINE).getBytes());
            output.write(("Incasso: " + String.format("%.2f", total) + " euro\n").getBytes());
            output.write("\nGrazie!\n\n".getBytes());
            output.write((ESC + "d").getBytes());
            output.write(new byte[]{0x04});
            output.write((ESC + "m").getBytes());

            sendToPrinter(output.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Genera un codice QR a partire da un testo (es. ID prenotazione).
     *
     * @param text testo da codificare
     * @return immagine {@link BufferedImage} del QR generato
     * @throws Exception se la generazione fallisce
     */
    private static BufferedImage generateQR(String text) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    /**
     * Converte un'immagine {@code BufferedImage} in una sequenza di byte compatibile con ESC/POS per la stampa.
     *
     * @param image immagine da convertire
     * @return array di byte da inviare alla stampante
     * @throws IOException se la conversione fallisce
     */
    private static byte[] convertImageToESCBitmap(BufferedImage image) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int width = image.getWidth();
        int height = image.getHeight();

        bos.write(new byte[]{0x1D, 0x76, 0x30, 0x00});
        bos.write((byte) (width / 8));
        bos.write(0);
        bos.write((byte) (height & 0xFF));
        bos.write((byte) ((height >> 8) & 0xFF));

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

    /**
     * Invia i dati binari alla stampante selezionata o predefinita.
     * Se non è configurata una stampante predefinita, viene mostrata una finestra di dialogo.
     *
     * @param data array di byte da stampare
     * @throws PrintException se si verifica un errore di stampa
     */
    private static void sendToPrinter(byte[] data) throws PrintException {
        String defaultPrinterName = loadDefaultPrinterName();

        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService selectedService = null;

        if (!defaultPrinterName.isEmpty()) {
            for (PrintService ps : services) {
                if (ps.getName().equalsIgnoreCase(defaultPrinterName)) {
                    selectedService = ps;
                    break;
                }
            }
        }

        if (selectedService == null) {
            List<String> printerNames = Arrays.stream(services)
                    .map(PrintService::getName)
                    .collect(Collectors.toList());

            ChoiceDialog<String> dialog = new ChoiceDialog<>(printerNames.get(0), printerNames);
            dialog.setTitle("Seleziona stampante");
            dialog.setHeaderText("Scegli la stampante da usare");
            dialog.setContentText("Stampanti disponibili:");

            Optional<String> result = dialog.showAndWait();
            if (result.isEmpty()) {
                System.out.println("Stampa annullata");
                return;
            }

            String selectedName = result.get();
            for (PrintService ps : services) {
                if (ps.getName().equalsIgnoreCase(selectedName)) {
                    selectedService = ps;
                    break;
                }
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Imposta stampante predefinita");
            alert.setHeaderText("Vuoi salvare \"" + selectedName + "\" come stampante predefinita?");
            alert.setContentText("Non ti verrà chiesto di nuovo al prossimo avvio.");

            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> confirm = alert.showAndWait();
            if (confirm.isPresent() && confirm.get() == ButtonType.YES) {
                saveDefaultPrinterName(selectedName);
            }
        }

        Doc doc = new SimpleDoc(new ByteArrayInputStream(data), DocFlavor.INPUT_STREAM.AUTOSENSE, null);
        DocPrintJob job = selectedService.createPrintJob();
        job.print(doc, new HashPrintRequestAttributeSet());
    }

    /**
     * Carica il nome della stampante predefinita dal file di configurazione.
     *
     * @return nome della stampante predefinita, oppure stringa vuota se non presente
     */
    private static String loadDefaultPrinterName() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);
            return props.getProperty("defaultPrinter", "").trim();
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Salva il nome della stampante predefinita nel file di configurazione.
     *
     * @param name nome della stampante da salvare
     */
    private static void saveDefaultPrinterName(String name) {
        Properties props = new Properties();
        props.setProperty("defaultPrinter", name);
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            props.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
