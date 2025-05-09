package org.example.progettooop_pistapattinaggio.util;

import javafx.beans.property.SimpleIntegerProperty;

public class InventoryItem {
    private final SimpleIntegerProperty shoeSize;
    private final SimpleIntegerProperty quantity;

    public InventoryItem(int shoeSize, int quantity) {
        this.shoeSize = new SimpleIntegerProperty(shoeSize);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public int getShoeSize() {
        return shoeSize.get();
    }

    public void setShoeSize(int size) {
        this.shoeSize.set(size);
    }

    public SimpleIntegerProperty getShoeSizeProperty() {
        return shoeSize;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public SimpleIntegerProperty getQuantityProperty() {
        return quantity;
    }
}
