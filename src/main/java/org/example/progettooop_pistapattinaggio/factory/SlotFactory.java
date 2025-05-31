package org.example.progettooop_pistapattinaggio.factory;

import org.example.progettooop_pistapattinaggio.model.Slot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SlotFactory {

    /**
     * Crea una lista di slot per il giorno corrente tra due orari specifici.
     * @param oraInizio Ora di inizio (es. 10)
     * @param oraFine Ora di fine (es. 20)
     * @param durataMinuti Durata di ogni slot in minuti (es. 60)
     * @param capienza Capienza massima per slot
     * @return Lista di slot generati
     */
    public static List<Slot> creaSlotGiornalieri(int oraInizio, int oraFine, int durataMinuti, int capienza) {
        return creaSlotPerData(LocalDate.now(), oraInizio, oraFine, durataMinuti, capienza);
    }

    /**
     * Crea una lista di slot per una data specifica tra due orari.
     * @param data Data degli slot
     * @param oraInizio Ora di inizio (es. 10)
     * @param oraFine Ora di fine (es. 20)
     * @param durataMinuti Durata di ogni slot in minuti (es. 60)
     * @param capienza Capienza massima per slot
     * @return Lista di slot generati
     */
    public static List<Slot> creaSlotPerData(LocalDate data, int oraInizio, int oraFine, int durataMinuti, int capienza) {
        List<Slot> slotList = new ArrayList<>();

        int numeroSlot = (oraFine - oraInizio) * 60 / durataMinuti;
        for (int i = 0; i < numeroSlot; i++) {
            int ora = oraInizio + (i * durataMinuti) / 60;
            int minuti = (i * durataMinuti) % 60;
            slotList.add(new Slot(data.atTime(ora, minuti), durataMinuti, capienza));
        }

        return slotList;
    }

    /**
     * Crea una lista di slot predefiniti dalle 18:00 alle 22:00 con slot di 60 minuti e capienza 100.
     * @return Lista di slot standard
     */
    public static List<Slot> creaSlotDefault() {
        return creaSlotGiornalieri(18, 22, 60, 100);
    }
}
