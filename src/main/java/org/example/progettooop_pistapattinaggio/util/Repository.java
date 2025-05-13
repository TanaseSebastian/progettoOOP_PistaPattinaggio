package org.example.progettooop_pistapattinaggio.util;

import java.util.ArrayList;
import java.util.List;

public class Repository<T> {
    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    public void remove(T item) {
        items.remove(item);
    }

    public List<T> getAll() {
        return new ArrayList<>(items); // restituisce copia per sicurezza
    }

    public int count() {
        return items.size();
    }

    public T get(int index) {
        return items.get(index);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void setAll(List<T> items) {
        this.items.clear();
        this.items.addAll(items);
    }
}
