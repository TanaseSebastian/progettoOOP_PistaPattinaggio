package org.example.progettooop_pistapattinaggio.iterator;

import org.example.progettooop_pistapattinaggio.model.Slot;

import java.util.Iterator;
import java.util.List;

/**
 * {@code SlotIterator} è un'implementazione dell'interfaccia {@link Iterator}
 * che consente di scorrere una lista di oggetti {@link Slot}.
 *
 * <p>Questa classe permette di iterare sui {@code Slot} in ordine sequenziale,
 * partendo dall'indice 0 fino alla fine della lista.</p>
 */
public class SlotIterator implements Iterator<Slot> {

    private final List<Slot> slots;
    private int currentIndex = 0;

    /**
     * Costruttore della classe {@code SlotIterator}.
     *
     * @param slots la lista di {@link Slot} da iterare
     */
    public SlotIterator(List<Slot> slots) {
        this.slots = slots;
    }

    /**
     * Verifica se ci sono ancora elementi da iterare nella lista.
     *
     * @return {@code true} se esiste un elemento successivo, {@code false} altrimenti
     */
    @Override
    public boolean hasNext() {
        return currentIndex < slots.size();
    }

    /**
     * Restituisce l'elemento successivo nella lista.
     *
     * @return il prossimo {@link Slot}
     * @throws java.util.NoSuchElementException se non ci sono più elementi
     */
    @Override
    public Slot next() {
        return slots.get(currentIndex++);
    }
}
