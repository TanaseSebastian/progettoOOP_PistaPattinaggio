package org.example.progettooop_pistapattinaggio.model;

import java.io.Serializable;

public class ShoeRental implements Serializable {
    private final int size;
    private final int quantity;

    public ShoeRental(int size, int quantity) {
        this.size = size;
        this.quantity = quantity;
    }

    public int getSize() {
        return size;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return quantity + " x taglia " + size;
    }
}
