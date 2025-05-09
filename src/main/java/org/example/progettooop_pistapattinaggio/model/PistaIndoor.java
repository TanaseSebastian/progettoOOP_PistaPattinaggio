package org.example.progettooop_pistapattinaggio.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PistaIndoor extends PistaDiPattinaggio {

    private final List<Slot> slotsDisponibili;

    public PistaIndoor(String nome, String indirizzo) {
        super(nome, indirizzo);
        this.slotsDisponibili = new ArrayList<>();
        // Aggiungo alcuni slot orari predefiniti
        addSampleSlots();
    }

    // Implementazione dei metodi astratti
    @Override
    public List<Slot> getSlotsDisponibili() {
        return slotsDisponibili;
    }

    // Aggiungi slot orari fittizi (per esempio)
    private void addSampleSlots() {
        slotsDisponibili.add(new Slot(LocalDateTime.now().plusHours(1), 60, 10)); // 1 ora, 10 posti
        slotsDisponibili.add(new Slot(LocalDateTime.now().plusHours(2), 60, 10)); // 1 ora, 10 posti
    }
}
