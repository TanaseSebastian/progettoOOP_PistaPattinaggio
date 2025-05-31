package org.example.progettooop_pistapattinaggio.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code CashRegister} è una classe Singleton che gestisce l'incasso
 * della pista di pattinaggio, tenendo traccia dei pagamenti effettuati in contanti e con carta.
 * <p>Fornisce metodi per registrare i pagamenti, ottenere i totali e stampare un report giornaliero.</p>
 */
public class CashRegister implements Serializable {

    private static CashRegister instance;  // Istanza Singleton

    private double totalCash;              // Totale incassato in contante
    private double totalCard;              // Totale incassato con carta
    private double totalIncome;            // Totale complessivo

    private final Map<String, Integer> paymentMethods; // Mappa dei conteggi per ogni metodo di pagamento

    /**
     * Costruttore privato per impedire l'istanziazione esterna (pattern Singleton).
     */
    private CashRegister() {
        totalCash = 0.0;
        totalCard = 0.0;
        totalIncome = 0.0;
        paymentMethods = new HashMap<>();
        paymentMethods.put("Cash", 0);
        paymentMethods.put("Card", 0);
    }

    /**
     * Restituisce l'unica istanza di {@code CashRegister}.
     * Se non esiste ancora, viene creata.
     *
     * @return istanza unica di {@code CashRegister}
     */
    public static CashRegister getInstance() {
        if (instance == null) {
            instance = new CashRegister();
        }
        return instance;
    }

    /**
     * Registra un pagamento specificando l'importo e il metodo ("Cash" o "Card").
     *
     * @param amount importo del pagamento
     * @param method metodo di pagamento (case-insensitive): "Cash" o "Card"
     */
    public void recordPayment(double amount, String method) {
        if ("Cash".equalsIgnoreCase(method)) {
            totalCash += amount;
            paymentMethods.put("Cash", paymentMethods.get("Cash") + 1);
        } else if ("Card".equalsIgnoreCase(method)) {
            totalCard += amount;
            paymentMethods.put("Card", paymentMethods.get("Card") + 1);
        }
        totalIncome += amount;
    }

    /**
     * Restituisce il totale incassato in contanti.
     *
     * @return totale in contanti
     */
    public double getTotalCash() {
        return totalCash;
    }

    /**
     * Restituisce il totale incassato tramite carta.
     *
     * @return totale con carta
     */
    public double getTotalCard() {
        return totalCard;
    }

    /**
     * Restituisce il totale complessivo incassato (contanti + carta).
     *
     * @return totale incassato
     */
    public double getTotalIncome() {
        return totalIncome;
    }

    /**
     * Stampa un report riepilogativo dei pagamenti effettuati nella giornata.
     */
    public void printReport() {
        System.out.println("### Report Giornata ###");
        System.out.println("Totale incassato: €" + totalIncome);
        System.out.println("Contante: €" + totalCash + " (N° " + paymentMethods.get("Cash") + " pagamenti)");
        System.out.println("Carta: €" + totalCard + " (N° " + paymentMethods.get("Card") + " pagamenti)");
    }

    /**
     * Restituisce una mappa che rappresenta il numero di pagamenti per ciascun metodo.
     *
     * @return mappa dei metodi di pagamento e conteggi
     */
    public Map<String, Integer> getPaymentMethods() {
        return paymentMethods;
    }

    /**
     * Reimposta tutti i valori di incasso e i conteggi dei pagamenti.
     * Utile per inizializzare la cassa a inizio giornata.
     */
    public void resetPayments() {
        totalCash = 0.0;
        totalCard = 0.0;
        totalIncome = 0.0;
        paymentMethods.put("Cash", 0);
        paymentMethods.put("Card", 0);
    }
}
