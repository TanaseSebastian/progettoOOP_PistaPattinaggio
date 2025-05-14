package org.example.progettooop_pistapattinaggio.util;

import org.example.progettooop_pistapattinaggio.model.Booking;
import org.example.progettooop_pistapattinaggio.model.Slot;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    // Salva un oggetto in un file
    public static void save(Object object, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(object);
            //System.out.println("Dati salvati correttamente nel file: " + filename);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dei dati: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Carica un oggetto da un file
    public static Object load(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("📁 Il file " + filename + " non esiste, verrà creato alla prima scrittura.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento dei dati: " + e.getMessage());
            return null;
        }
    }

    // Salva l'inventario
    public static void saveInventory(Inventory inventory) {
        save(inventory, "inventory.ser");
    }

    // Carica l'inventario
    public static Inventory loadInventory() {
        Inventory inventory = (Inventory) load("inventory.ser");
        return (inventory != null) ? inventory : new Inventory();  // ← restituisce oggetto vuoto se file assente o corrotto
    }

    // Salva le prenotazioni
    public static void saveBookings(List<Booking> bookings) {
        save(bookings, "bookings.ser");
    }

    // Carica le prenotazioni
    public static List<Booking> loadBookings() {
        List<Booking> bookings = (List<Booking>) load("bookings.ser");
        if (bookings == null) {
            saveBookings(new ArrayList<>());  // Crea file vuoto se assente
            System.out.println("bookings.ser creato con lista vuota.");
            return new ArrayList<>();
        }
        return bookings;
    }

    // Salva lo Slot
    public static void saveSlot(Slot slot) {
        save(slot, "slot.ser");
    }

    // Carica uno Slot
    public static Slot loadSlot() {
        return (Slot) load("slot.ser");
    }

    // Salva il Cash Register
    public static void saveCashRegister(CashRegister cashRegister) {
        save(cashRegister, "cashRegister.ser");
    }

    // Carica il Cash Register
    public static CashRegister loadCashRegister() {
        return (CashRegister) load("cashRegister.ser");
    }
}
