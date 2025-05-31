package org.example.progettooop_pistapattinaggio.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code Inventory} gestisce l'inventario dei pattini a noleggio per la pista di pattinaggio.
 * <p>Permette di aggiungere, vendere e controllare la disponibilità dei pattini,
 * nonché visualizzare l'inventario sotto forma di lista per l'interfaccia grafica.</p>
 */
public class Inventory implements Serializable {

    private final Map<Integer, Integer> shoeSizes; // <taglia, quantità disponibile>

    /**
     * Crea un nuovo inventario vuoto.
     */
    public Inventory() {
        shoeSizes = new HashMap<>();
    }

    /**
     * Aggiunge una quantità specifica di pattini di una determinata taglia all'inventario.
     *
     * @param size     taglia del pattino
     * @param quantity quantità da aggiungere
     */
    public void addShoes(int size, int quantity) {
        shoeSizes.put(size, shoeSizes.getOrDefault(size, 0) + quantity);
    }

    /**
     * Tenta di vendere una quantità specifica di pattini di una determinata taglia.
     *
     * @param size     taglia richiesta
     * @param quantity quantità richiesta
     * @return {@code true} se la vendita è andata a buon fine, {@code false} se la quantità non è sufficiente
     */
    public boolean sellShoes(int size, int quantity) {
        int available = shoeSizes.getOrDefault(size, 0);
        if (available >= quantity) {
            shoeSizes.put(size, available - quantity);
            return true;
        }
        return false;
    }

    /**
     * Restituisce l'inventario sotto forma di lista osservabile di {@link InventoryItem},
     * utile per l'integrazione con JavaFX.
     *
     * @return lista osservabile di oggetti {@code InventoryItem}
     */
    public ObservableList<InventoryItem> getInventoryList() {
        ObservableList<InventoryItem> inventoryList = FXCollections.observableArrayList();
        shoeSizes.forEach((size, quantity) ->
                inventoryList.add(new InventoryItem(size, quantity))
        );
        return inventoryList;
    }

    /**
     * Restituisce la quantità disponibile per una determinata taglia.
     *
     * @param size taglia richiesta
     * @return quantità disponibile (0 se non presente)
     */
    public int getShoesQuantity(int size) {
        return shoeSizes.getOrDefault(size, 0);
    }

    /**
     * Stampa a console l'inventario attuale.
     */
    public void showInventory() {
        shoeSizes.forEach((size, quantity) ->
                System.out.println("Numero scarpa: " + size + " - Disponibili: " + quantity)
        );
    }

    /**
     * Verifica se è disponibile una certa quantità per una determinata taglia.
     *
     * @param size     taglia richiesta
     * @param quantity quantità richiesta
     * @return {@code true} se disponibile, altrimenti {@code false}
     */
    public boolean isAvailable(int size, int quantity) {
        return shoeSizes.getOrDefault(size, 0) >= quantity;
    }

    /**
     * Aggiunge un oggetto {@link InventoryItem} all'inventario.
     *
     * @param newItem oggetto da aggiungere
     */
    public void addItem(InventoryItem newItem) {
        addShoes(newItem.getShoeSize(), newItem.getQuantity());
    }
}
