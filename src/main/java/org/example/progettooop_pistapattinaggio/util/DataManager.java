package org.example.progettooop_pistapattinaggio.util;

import org.example.progettooop_pistapattinaggio.model.Booking;
import org.example.progettooop_pistapattinaggio.model.Slot;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.security.MessageDigest;
import java.util.*;

/**
 * Persistenza con serializzazione + controlli:
 * - scrittura atomica (NIO, file temporaneo + move)
 * - file .tag di integrità: HMAC-SHA256 se APP_HMAC_KEY, altrimenti SHA-256
 * - filtro di deserializzazione JEP-290 (allow-list + limiti)
 * - metodi tipizzati che verificano il tipo reale degli oggetti
 */
public class DataManager {

    private static Path tagPath(Path dataFile) {
        return dataFile.resolveSibling(dataFile.getFileName() + ".tag");
    }

    private static void setOwnerRW(Path p) {
        try {
            Set<PosixFilePermission> perms = EnumSet.of(
                    PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE
            );
            Files.setPosixFilePermissions(p, perms);
        } catch (UnsupportedOperationException | IOException ignored) {
        }
    }

    private static byte[] computeTag(byte[] payload) {
        String key = System.getenv("APP_HMAC_KEY");
        try {
            if (key != null && !key.isBlank()) {
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                return mac.doFinal(payload);
            } else {
                return MessageDigest.getInstance("SHA-256").digest(payload);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Errore calcolo tag", e);
        }
    }

    private static void writeAtomic(Path dataFile, byte[] payload) throws IOException {
        Path parent = dataFile.getParent();
        if (parent != null) Files.createDirectories(parent);

        Path tmp = dataFile.resolveSibling(dataFile.getFileName() + ".tmp");
        Files.write(tmp, payload, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        setOwnerRW(tmp);

        try {
            Files.move(tmp, dataFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException ex) {
            Files.move(tmp, dataFile, StandardCopyOption.REPLACE_EXISTING);
        }
        setOwnerRW(dataFile);

        byte[] tag = computeTag(payload);
        Path tagFile = tagPath(dataFile);
        Files.write(tagFile, tag, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        setOwnerRW(tagFile);
    }

    /** Ritorna bytes se tag ok, null se mancano file/tag, eccezione se tag non corrisponde. */
    private static byte[] readVerified(Path dataFile) throws IOException {
        Path tagFile = tagPath(dataFile);
        if (!Files.exists(dataFile) || !Files.exists(tagFile)) return null;

        byte[] payload = Files.readAllBytes(dataFile);
        byte[] tag = Files.readAllBytes(tagFile);
        byte[] calc = computeTag(payload);

        if (!MessageDigest.isEqual(tag, calc)) {
            throw new SecurityException("Tag non valido per " + dataFile.getFileName());
        }
        return payload;
    }

    //Serializzazione difensiva
    private static ObjectInputFilter filter() {
        String pattern = String.join("",
                "maxdepth=50;maxrefs=100000;maxbytes=10485760;",
                "java.base/*;java.util.*;java.time.*;",
                "org.example.progettooop_pistapattinaggio.model.*;",
                "org.example.progettooop_pistapattinaggio.util.*;",
                "!*"
        );
        return ObjectInputFilter.Config.createFilter(pattern);
    }

    private static byte[] toBytes(Object o) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(o);
            oos.flush();
            return bos.toByteArray();
        }
    }

    private static Object fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            ois.setObjectInputFilter(filter());
            return ois.readObject();
        }
    }

    //API generiche (firme invariate)
    public static void save(Object object, String filename) {
        Path dataFile = Paths.get(filename);
        try {
            writeAtomic(dataFile, toBytes(object));
        } catch (IOException e) {
            System.out.println("Errore salvataggio: " + e.getMessage());
        }
    }

    public static Object load(String filename) {
        Path dataFile = Paths.get(filename);
        if (!Files.exists(dataFile)) {
            System.out.println("File assente: " + filename);
            return null;
        }
        try {
            byte[] payload = readVerified(dataFile);
            if (payload == null) return null; // file o tag mancanti
            return fromBytes(payload);
        } catch (SecurityException se) {
            System.out.println("Integrità fallita: " + se.getMessage());
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Errore caricamento: " + e.getMessage());
            return null;
        }
    }

    //Helper tipizzati

    private static <T> T loadExact(String filename, Class<T> expected) {
        Object o = load(filename);
        if (o == null) return null;
        if (o.getClass() != expected) {
            System.out.println("Tipo inatteso in " + filename + ": " + o.getClass().getName());
            return null;
        }
        return expected.cast(o);
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> loadListExact(String filename, Class<T> elementType) {
        Object o = load(filename);
        if (!(o instanceof List<?> raw)) return null;
        for (Object it : raw) {
            if (it == null || it.getClass() != elementType) {
                System.out.println("Lista non valida in " + filename);
                return null;
            }
        }
        return (List<T>) raw;
    }

    //API tipizzate

    public static void saveInventory(Inventory inventory) {
        save(inventory, "inventory.ser");
    }

    public static Inventory loadInventory() {
        Inventory inv = loadExact("inventory.ser", Inventory.class);
        return (inv != null) ? inv : new Inventory();
    }

    public static void saveBookings(List<Booking> bookings) {
        for (Object b : bookings) {
            if (b == null || b.getClass() != Booking.class) {
                throw new IllegalArgumentException("La lista contiene elementi non Booking");
            }
        }
        save(bookings, "bookings.ser");
    }

    public static List<Booking> loadBookings() {
        List<Booking> list = loadListExact("bookings.ser", Booking.class);
        if (list == null) {
            list = new ArrayList<>();
            saveBookings(list);
            System.out.println("Creato bookings.ser vuoto.");
        }
        return list;
    }

    public static void saveSlot(Slot slot) {
        save(slot, "slot.ser");
    }

    public static Slot loadSlot() {
        return loadExact("slot.ser", Slot.class);
    }

    public static void saveCashRegister(CashRegister cashRegister) {
        save(cashRegister, "cashRegister.ser");
    }

    public static CashRegister loadCashRegister() {
        return loadExact("cashRegister.ser", CashRegister.class);
    }
}