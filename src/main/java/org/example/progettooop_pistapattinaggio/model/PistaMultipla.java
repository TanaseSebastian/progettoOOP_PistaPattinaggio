package org.example.progettooop_pistapattinaggio.model;

import org.example.progettooop_pistapattinaggio.iterator.SlotIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Rappresenta un centro di pattinaggio composto da più piste.
 * Aggrega le informazioni come slot e capienza dalle sotto-piste.
 */
public class PistaMultipla extends PistaDiPattinaggio {
    private final List<PistaDiPattinaggio> sottoPiste = new ArrayList<>();

    /**
     * Costruisce una pista multipla con nome e indirizzo.
     * Le informazioni come superficie, copertura, note e capienza sono aggregative o indicative.
     *
     * @param nome      Nome del centro.
     * @param indirizzo Indirizzo del centro.
     */
    public PistaMultipla(String nome, String indirizzo) {
        super(
                nome,
                indirizzo,
                "multipla",
                0, // superficie aggregata opzionale
                false,
                "Composta da più piste",
                0 // capienza aggregata gestita dinamicamente
        );
    }

    /**
     * Aggiunge una pista alla lista delle sotto-piste.
     *
     * @param pista pista da aggiungere
     */
    public void aggiungiPista(PistaDiPattinaggio pista) {
        sottoPiste.add(pista);
    }

    /**
     * Restituisce tutti gli slot disponibili delle sotto-piste.
     *
     * @return lista aggregata di slot
     */
    @Override
    public List<Slot> getSlotsDisponibili() {
        List<Slot> slots = new ArrayList<>();
        for (PistaDiPattinaggio pista : sottoPiste) {
            slots.addAll(pista.getSlotsDisponibili());
        }
        return slots;
    }

    /**
     * Restituisce solo gli slot disponibili per una specifica sotto-pista.
     *
     * @param nomePista nome della sotto-pista desiderata
     * @return lista di slot disponibili per quella pista, oppure lista vuota se non trovata
     */
    public List<Slot> getSlotsDisponibiliPerPista(String nomePista) {
        return sottoPiste.stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nomePista))
                .findFirst()
                .map(PistaDiPattinaggio::getSlotsDisponibili)
                .orElse(List.of());
    }

    /**
     * Calcola dinamicamente la capienza totale sommando le capienze delle sotto-piste.
     *
     * @return capienza massima aggregata
     */
    @Override
    public int getCapienzaMassima() {
        return sottoPiste.stream()
                .mapToInt(PistaDiPattinaggio::getCapienzaMassima)
                .sum();
    }

    /**
     * Restituisce le sotto-piste contenute in questa pista multipla.
     *
     * @return lista delle piste figlie
     */
    public List<PistaDiPattinaggio> getPiste() {
        return sottoPiste;
    }


    /**
     * Restituisce una sotto-pista in base al nome fornito.
     *
     * @param nome il nome della pista da cercare (case-insensitive)
     * @return la {@link PistaDiPattinaggio} corrispondente al nome, oppure {@code null} se non trovata
     */
    public PistaDiPattinaggio getPistaByNome(String nome) {
        return sottoPiste.stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    /**
     * Restituisce un iteratore per scorrere tutti gli slot disponibili nelle sotto-piste.
     *
     * @return iteratore sugli slot
     */
    @Override
    public Iterator<Slot> iterator() {
        return new SlotIterator(getSlotsDisponibili());
    }
}
