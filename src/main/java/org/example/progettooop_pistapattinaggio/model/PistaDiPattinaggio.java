package org.example.progettooop_pistapattinaggio.model;

import org.example.progettooop_pistapattinaggio.util.Inventory;

import java.util.List;

public abstract class PistaDiPattinaggio {
    private String nome;
    private String indirizzo;
    private Inventory inventario;  // Gestisce scarponi e disponibilità

    // Costruttore
    public PistaDiPattinaggio(String nome, String indirizzo) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.inventario = new Inventory();  // Inizializza inventario
    }

    public String getNome() {
        return nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public Inventory getInventario() {
        return inventario;
    }

    public abstract List<Slot> getSlotsDisponibili(); // Metodo per ottenere gli slot disponibili
}
