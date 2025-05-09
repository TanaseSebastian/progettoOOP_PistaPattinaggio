package org.example.progettooop_pistapattinaggio.iterator;

import org.example.progettooop_pistapattinaggio.model.Slot;

import java.util.Iterator;
import java.util.List;

public class SlotIterator implements Iterator<Slot> {

    private final List<Slot> slots;
    private int currentIndex = 0;

    public SlotIterator(List<Slot> slots) {
        this.slots = slots;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < slots.size();
    }

    @Override
    public Slot next() {
        return slots.get(currentIndex++);
    }
}
