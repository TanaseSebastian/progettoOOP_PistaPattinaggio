package org.example.progettooop_pistapattinaggio.model;

import org.example.progettooop_pistapattinaggio.iterator.SlotIterator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Day implements Iterable<Slot> {
    private final LocalDate date;
    private final List<Slot> slots;

    public Day(LocalDate date) {
        this.date = date;
        this.slots = new ArrayList<>();
    }

    public void addSlot(Slot slot) {
        slots.add(slot);
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getTotalCapacity() {
        return slots.stream().mapToInt(Slot::getCapacity).sum();
    }

    public int getTotalBookings() {
        return slots.stream().mapToInt(s -> s.getBookings().size()).sum();
    }

    @Override
    public Iterator<Slot> iterator() {
        return new SlotIterator(slots);
    }

    @Override
    public String toString() {
        return "Giornata: " + date + " (" + slots.size() + " slot)";
    }
}
