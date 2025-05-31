package org.example.progettooop_pistapattinaggio.model;
import org.example.progettooop_pistapattinaggio.iterator.SlotIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Rappresenta una pista singola base con una lista di slot disponibili.
 */
public class PistaBase extends PistaDiPattinaggio {
    private final List<Slot> slotDisponibili = new ArrayList<>();

    /**
     * Costruttore della pista base.
     *
     * @param nome             Nome della pista.
     * @param indirizzo        Indirizzo della pista.
     * @param tipo             Tipo della pista (es: indoor, outdoor).
     * @param superficieMq     Superficie della pista in metri quadrati.
     * @param coperta          True se la pista è coperta, false altrimenti.
     * @param note             Note descrittive.
     * @param capienzaMassima  Capienza massima della pista.
     */
    public PistaBase(String nome, String indirizzo, String tipo, int superficieMq,
                     boolean coperta, String note, int capienzaMassima) {
        super(nome, indirizzo, tipo, superficieMq, coperta, note, capienzaMassima);
    }

    @Override
    public List<Slot> getSlotsDisponibili() {
        return slotDisponibili;
    }

    /**
     * Aggiunge una lista di slot disponibili alla pista.
     *
     * @param slots lista di slot da aggiungere.
     */
    public void addSlots(List<Slot> slots) {
        slotDisponibili.addAll(slots);
    }

    @Override
    public Iterator<Slot> iterator() {
        return new SlotIterator(slotDisponibili);
    }
}
