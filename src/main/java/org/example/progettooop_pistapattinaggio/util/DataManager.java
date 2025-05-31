package org.example.progettooop_pistapattinaggio.util;

import org.example.progettooop_pistapattinaggio.model.Booking;
import org.example.progettooop_pistapattinaggio.model.Slot;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code DataManager} gestisce il salvataggio e il caricamento
 * persistente dei dati dell'applicazione, come prenotazioni, slot, inventario e cassa.
 * <p>Utilizza la serializzazione Java per scrivere e leggere oggetti su file .ser.</p>
 */
public class DataManager {

    /**
     * Salva un oggetto generico in un file tramite serializzazione.
     *
     * @param object   oggetto da salvare
     * @param filename nome del file di destinazione
     */
    public static void save(Object object, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(object);
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio dei dati: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carica un oggetto serializzato da file.
     *
     * @param filename nome del file da cui caricare
     * @return oggetto caricato, oppure {@code null} se il file non esiste o si verifica un errore
     */
    public static Object load(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("📁 Il file " + filename + " non esiste, verrà creato alla prima scrittura.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Errore durante il caricamento dei dati: " + e.getMessage());
            return null;
        }
    }

    /**
     * Salva l'oggetto {@link Inventory} su file.
     *
     * @param inventory inventario da salvare
     */
    public static void saveInventory(Inventory inventory) {
        save(inventory, "inventory.ser");
    }

    /**
     * Carica l'oggetto {@link Inventory} da file. Se il file non esiste o è corrotto,
     * restituisce un nuovo oggetto {@code Inventory} vuoto.
     *
     * @return oggetto {@code Inventory} caricato o nuovo
     */
    public static Inventory loadInventory() {
        Inventory inventory = (Inventory) load("inventory.ser");
        return (inventory != null) ? inventory : new Inventory();
    }

    /**
     * Salva la lista di prenotazioni su file.
     *
     * @param bookings lista di {@link Booking}
     */
    public static void saveBookings(List<Booking> bookings) {
        save(bookings, "bookings.ser");
    }

    /**
     * Carica la lista di prenotazioni da file.
     * Se il file è assente o corrotto, crea un nuovo file vuoto.
     *
     * @return lista di {@link Booking}
     */
    public static List<Booking> loadBookings() {
        List<Booking> bookings = (List<Booking>) load("bookings.ser");
        if (bookings == null) {
            saveBookings(new ArrayList<>());
            System.out.println("bookings.ser creato con lista vuota.");
            return new ArrayList<>();
        }
        return bookings;
    }

    /**
     * Salva un singolo {@link Slot} su file.
     *
     * @param slot slot da salvare
     */
    public static void saveSlot(Slot slot) {
        save(slot, "slot.ser");
    }

    /**
     * Carica un oggetto {@link Slot} da file.
     *
     * @return oggetto {@code Slot} caricato oppure {@code null} se non esiste
     */
    public static Slot loadSlot() {
        return (Slot) load("slot.ser");
    }

    /**
     * Salva il registro cassa su file.
     *
     * @param cashRegister oggetto {@link CashRegister} da salvare
     */
    public static void saveCashRegister(CashRegister cashRegister) {
        save(cashRegister, "cashRegister.ser");
    }

    /**
     * Carica il registro cassa da file.
     *
     * @return oggetto {@link CashRegister} caricato oppure {@code null} se non esiste
     */
    public static CashRegister loadCashRegister() {
        return (CashRegister) load("cashRegister.ser");
    }
}
