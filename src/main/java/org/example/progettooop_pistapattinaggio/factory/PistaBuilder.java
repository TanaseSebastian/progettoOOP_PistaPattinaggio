package org.example.progettooop_pistapattinaggio.factory;

import org.example.progettooop_pistapattinaggio.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder per la creazione di oggetti {@link PistaDiPattinaggio}.
 * Permette di configurare sia piste singole (base) che piste multiple composte da sottopiste.
 */
public class PistaBuilder {

    private String nome;
    private String indirizzo;
    private String tipo = "indoor";
    private int superficieMq = 0;
    private boolean coperta = true;
    private String note = "";
    private int capienzaMassima = 0;

    private final List<Slot> slotDisponibili = new ArrayList<>();
    private final List<PistaDiPattinaggio> sottoPiste = new ArrayList<>();

    // Parametri per generazione automatica slot
    private int inizioOre = -1, fineOre = -1, durataMinuti = -1, posti = -1;

    /** Imposta il nome della pista. */
    public PistaBuilder setNome(String nome) {
        this.nome = nome;
        return this;
    }

    /** Imposta l'indirizzo della pista. */
    public PistaBuilder setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
        return this;
    }

    /** Imposta il tipo della pista (es. indoor, outdoor). */
    public PistaBuilder setTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    /** Imposta la superficie della pista in metri quadrati. */
    public PistaBuilder setSuperficieMq(int superficieMq) {
        this.superficieMq = superficieMq;
        return this;
    }

    /** Imposta se la pista è coperta. */
    public PistaBuilder setCoperta(boolean coperta) {
        this.coperta = coperta;
        return this;
    }

    /** Imposta note descrittive. */
    public PistaBuilder setNote(String note) {
        this.note = note;
        return this;
    }

    /** Imposta la capienza massima della pista. */
    public PistaBuilder setCapienzaMassima(int capienzaMassima) {
        this.capienzaMassima = capienzaMassima;
        return this;
    }

    /**
     * Aggiunge slot manualmente definiti alla pista.
     * @param slots Lista di slot da aggiungere
     */
    public PistaBuilder setSlotDisponibili(List<Slot> slots) {
        if (slots != null) {
            this.slotDisponibili.addAll(slots);
        }
        return this;
    }

    /**
     * Configura la generazione automatica degli slot giornalieri tramite {@link SlotFactory}.
     * @param inizioOre Ora di inizio (es. 10)
     * @param fineOre Ora di fine (es. 20)
     * @param durataMinuti Durata di ciascuno slot (es. 60)
     * @param posti Capienza per ogni slot
     */
    public PistaBuilder configuraSlotGiornalieri(int inizioOre, int fineOre, int durataMinuti, int posti) {
        this.inizioOre = inizioOre;
        this.fineOre = fineOre;
        this.durataMinuti = durataMinuti;
        this.posti = posti;
        return this;
    }

    /**
     * Aggiunge una sottopista per comporre una {@link PistaMultipla}.
     * @param pista Oggetto {@link PistaDiPattinaggio} da aggiungere
     */
    public PistaBuilder addSottoPista(PistaDiPattinaggio pista) {
        if (pista != null) {
            this.sottoPiste.add(pista);
        }
        return this;
    }

    /**
     * Costruisce l'oggetto {@link PistaDiPattinaggio} configurato.
     * Se contiene sottopiste, restituisce una {@link PistaMultipla},
     * altrimenti una {@link PistaBase}.
     */
    public PistaDiPattinaggio build() {
        if (!sottoPiste.isEmpty()) {
            PistaMultipla multipla = new PistaMultipla(nome, indirizzo);
            sottoPiste.forEach(multipla::aggiungiPista);
            return multipla;
        }

        PistaBase pista = new PistaBase(nome, indirizzo, tipo, superficieMq, coperta, note, capienzaMassima);

        // Slot generati automaticamente
        if (inizioOre >= 0 && fineOre >= 0 && durataMinuti > 0 && posti > 0) {
            List<Slot> autoSlots = SlotFactory.creaSlotGiornalieri(inizioOre, fineOre, durataMinuti, posti);
            pista.addSlots(autoSlots);
        }

        // Slot manuali
        pista.addSlots(slotDisponibili);

        return pista;
    }
}
