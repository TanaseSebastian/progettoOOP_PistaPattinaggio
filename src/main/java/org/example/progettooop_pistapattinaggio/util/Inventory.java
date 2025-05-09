package org.example.progettooop_pistapattinaggio.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<Integer, Integer> shoeSizes; // <size, available_quantity>

    public Inventory() {
        shoeSizes = new HashMap<>();
    }

    // Aggiungi scarponi all'inventario
    public void addShoes(int size, int quantity) {
        shoeSizes.put(size, shoeSizes.getOrDefault(size, 0) + quantity);
    }

    // Vendi i pattini
    public boolean sellShoes(int size, int quantity) {
        int available = shoeSizes.getOrDefault(size, 0);
        if (available >= quantity) {
            shoeSizes.put(size, available - quantity);  // Riduce la quantità disponibile
            return true;  // Vendita riuscita
        }
        return false;  // Non ci sono abbastanza pattini
    }

    // Restituisci una lista di InventoryItem per la visualizzazione
    public ObservableList<InventoryItem> getInventoryList() {
        ObservableList<InventoryItem> inventoryList = FXCollections.observableArrayList();
        shoeSizes.forEach((size, quantity) -> {
            inventoryList.add(new InventoryItem(size, quantity));  // Crea oggetti InventoryItem da visualizzare
        });
        return inventoryList;
    }

    public int getShoesQuantity(int size) {
        return shoeSizes.getOrDefault(size, 0);
    }

    // Mostra l'inventario
    public void showInventory() {
        shoeSizes.forEach((size, quantity) -> {
            System.out.println("Numero scarpa: " + size + " - Disponibili: " + quantity);
        });
    }

    public boolean isAvailable(int size, int quantity) {
        return shoeSizes.getOrDefault(size, 0) >= quantity;
    }
}
