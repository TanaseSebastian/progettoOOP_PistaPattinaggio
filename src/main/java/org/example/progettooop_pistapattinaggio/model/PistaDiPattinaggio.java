package org.example.progettooop_pistapattinaggio.model;

import java.io.Serializable;
import java.util.List;

/**
 * Classe astratta che rappresenta una pista di pattinaggio.
 * Fornisce le proprietà comuni e l'interfaccia per accedere agli slot disponibili.
 * <p>Le sottoclassi concrete dovranno implementare la logica per la gestione degli slot disponibili.</p>
 */
public abstract class PistaDiPattinaggio implements Iterable<Slot>, Serializable {

    private final String nome;
    private final String indirizzo;
    private final String tipo;
    private final int superficieMq;
    private final boolean coperta;
    private final String note;
    private final int capienzaMassima;

    /**
     * Costruttore della pista di pattinaggio.
     *
     * @param nome             nome della pista
     * @param indirizzo        indirizzo della pista
     * @param tipo             tipo di pista (es. indoor, outdoor, sintetica, ghiaccio...)
     * @param superficieMq     superficie in metri quadrati
     * @param coperta          {@code true} se la pista è coperta, altrimenti {@code false}
     * @param note             eventuali note aggiuntive
     * @param capienzaMassima  numero massimo di persone consentite sulla pista
     */
    public PistaDiPattinaggio(String nome, String indirizzo, String tipo, int superficieMq,
                              boolean coperta, String note, int capienzaMassima) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.tipo = tipo;
        this.superficieMq = superficieMq;
        this.coperta = coperta;
        this.note = note;
        this.capienzaMassima = capienzaMassima;
    }

    /**
     * Restituisce il nome della pista.
     *
     * @return nome della pista
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce l'indirizzo della pista.
     *
     * @return indirizzo
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * Restituisce il tipo di pista (es. indoor, outdoor...).
     *
     * @return tipo della pista
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Restituisce la superficie della pista in metri quadrati.
     *
     * @return superficie in m²
     */
    public int getSuperficieMq() {
        return superficieMq;
    }

    /**
     * Indica se la pista è coperta.
     *
     * @return {@code true} se è coperta, {@code false} altrimenti
     */
    public boolean isCoperta() {
        return coperta;
    }

    /**
     * Restituisce le eventuali note aggiuntive associate alla pista.
     *
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * Restituisce la capienza massima della pista.
     *
     * @return numero massimo di persone consentite
     */
    public int getCapienzaMassima() {
        return capienzaMassima;
    }

    /**
     * Metodo astratto da implementare per ottenere la lista degli slot disponibili
     * per la pista di pattinaggio.
     *
     * @return lista di {@link Slot} disponibili
     */
    public abstract List<Slot> getSlotsDisponibili();
}
