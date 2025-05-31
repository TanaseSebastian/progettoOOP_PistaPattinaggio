package org.example.progettooop_pistapattinaggio.util;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Repository<T>} è una classe generica che rappresenta un contenitore semplice e riutilizzabile
 * per la gestione di collezioni di oggetti in memoria.
 *
 * <p>Fornisce operazioni base come aggiunta, rimozione, accesso per indice, conteggio e sostituzione
 * dell'intero contenuto. La lista restituita da {@code getAll()} è una copia, per proteggere
 * l'integrità dei dati interni.</p>
 *
 * @param <T> il tipo di oggetto gestito dal repository
 */
public class Repository<T> {

    private final List<T> items = new ArrayList<>();

    /**
     * Aggiunge un elemento al repository.
     *
     * @param item l'elemento da aggiungere
     */
    public void add(T item) {
        items.add(item);
    }

    /**
     * Rimuove un elemento dal repository.
     *
     * @param item l'elemento da rimuovere
     */
    public void remove(T item) {
        items.remove(item);
    }

    /**
     * Restituisce una copia della lista di tutti gli elementi contenuti.
     *
     * @return una nuova {@code List} con tutti gli elementi
     */
    public List<T> getAll() {
        return new ArrayList<>(items); // restituisce copia per sicurezza
    }

    /**
     * Restituisce il numero totale di elementi presenti nel repository.
     *
     * @return numero di elementi
     */
    public int count() {
        return items.size();
    }

    /**
     * Restituisce l'elemento alla posizione specificata.
     *
     * @param index indice dell'elemento (basato su 0)
     * @return elemento alla posizione indicata
     * @throws IndexOutOfBoundsException se l'indice è fuori dal range valido
     */
    public T get(int index) {
        return items.get(index);
    }

    /**
     * Verifica se il repository è vuoto.
     *
     * @return {@code true} se non ci sono elementi, altrimenti {@code false}
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Sostituisce l'intero contenuto del repository con una nuova lista.
     *
     * @param items nuova lista di elementi da impostare
     */
    public void setAll(List<T> items) {
        this.items.clear();
        this.items.addAll(items);
    }
}
