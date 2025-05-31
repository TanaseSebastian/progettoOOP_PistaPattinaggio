package org.example.progettooop_pistapattinaggio.util;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * {@code InventoryItem} rappresenta un elemento dell'inventario visibile nell'interfaccia JavaFX.
 * <p>Contiene le informazioni relative a una taglia di pattino e alla quantità disponibile,
 * incapsulate in proprietà osservabili per l'integrazione con componenti grafici come TableView.</p>
 */
public class InventoryItem {

    private final SimpleIntegerProperty shoeSize;
    private final SimpleIntegerProperty quantity;

    /**
     * Costruisce un nuovo oggetto {@code InventoryItem} con la taglia e la quantità specificate.
     *
     * @param shoeSize taglia del pattino
     * @param quantity quantità disponibile
     */
    public InventoryItem(int shoeSize, int quantity) {
        this.shoeSize = new SimpleIntegerProperty(shoeSize);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    /**
     * Restituisce la taglia del pattino.
     *
     * @return numero della taglia
     */
    public int getShoeSize() {
        return shoeSize.get();
    }

    /**
     * Imposta una nuova taglia del pattino.
     *
     * @param size nuova taglia
     */
    public void setShoeSize(int size) {
        this.shoeSize.set(size);
    }

    /**
     * Restituisce la proprietà osservabile della taglia del pattino.
     *
     * @return proprietà JavaFX per il binding
     */
    public SimpleIntegerProperty getShoeSizeProperty() {
        return shoeSize;
    }

    /**
     * Restituisce la quantità disponibile.
     *
     * @return quantità attuale
     */
    public int getQuantity() {
        return quantity.get();
    }

    /**
     * Imposta una nuova quantità disponibile.
     *
     * @param quantity nuova quantità
     */
    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    /**
     * Restituisce la proprietà osservabile della quantità disponibile.
     *
     * @return proprietà JavaFX per il binding
     */
    public SimpleIntegerProperty getQuantityProperty() {
        return quantity;
    }
}
