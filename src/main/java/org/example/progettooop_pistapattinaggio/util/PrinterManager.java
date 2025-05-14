package org.example.progettooop_pistapattinaggio.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.util.List;
import org.example.progettooop_pistapattinaggio.model.*;

public class PrinterManager {

    private static final String ESC = "\u001B";
    private static final String NEW_LINE = "\n";

    public static void printTicket(Booking booking) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            Customer customer = booking.getCustomer();
            Ticket ticket = booking.getTicket();
            List<ShoeRental> shoes = booking.getRentedShoes();

            output.write((ESC + "@").getBytes());
            output.write((ESC + "!").getBytes());
            output.write(new byte[]{0x38});
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

    private static BufferedImage generateQR(String text) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

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

    private static void sendToPrinter(byte[] data) throws PrintException {
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
    }
}