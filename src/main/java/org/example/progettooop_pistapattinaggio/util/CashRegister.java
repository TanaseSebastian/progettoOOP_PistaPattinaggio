package org.example.progettooop_pistapattinaggio.util;

import java.util.HashMap;
import java.util.Map;

public class CashRegister {
    private static CashRegister instance;  // Istanza Singleton
    private double totalCash;  // Totale incassato in contante
    private double totalCard;  // Totale incassato con carta
    private double totalIncome; // Totale complessivo incassato

    private final Map<String, Integer> paymentMethods;

    // Costruttore privato per il Singleton
    private CashRegister() {
        totalCash = 0.0;
        totalCard = 0.0;
        totalIncome = 0.0;
        paymentMethods = new HashMap<>();
        paymentMethods.put("Cash", 0);
        paymentMethods.put("Card", 0);
    }

    // Metodo per ottenere l'istanza unica di CashRegister
    public static CashRegister getInstance() {
        if (instance == null) {
            instance = new CashRegister();
        }
        return instance;
    }

    // Registra il pagamento
    public void recordPayment(double amount, String method) {
        if ("Cash".equalsIgnoreCase(method)) {
            totalCash += amount;
            paymentMethods.put("Cash", paymentMethods.get("Cash") + 1);
        } else if ("Card".equalsIgnoreCase(method)) {
            totalCard += amount;
            paymentMethods.put("Card", paymentMethods.get("Card") + 1);
        }
        totalIncome += amount;  // Aggiungi l'incasso totale
    }

    // Restituisce il totale incassato in contante
    public double getTotalCash() {
        return totalCash;
    }

    // Restituisce il totale incassato con carta
    public double getTotalCard() {
        return totalCard;
    }

    // Restituisce il totale complessivo
    public double getTotalIncome() {
        return totalIncome;
    }

    // Restituisce un report giornaliero
    public void printReport() {
        System.out.println("### Report Giornata ###");
        System.out.println("Totale incassato: €" + totalIncome);
        System.out.println("Contante: €" + totalCash + " (N° " + paymentMethods.get("Cash") + " pagamenti)");
        System.out.println("Carta: €" + totalCard + " (N° " + paymentMethods.get("Card") + " pagamenti)");
    }

    public Map<String, Integer> getPaymentMethods() {
        return paymentMethods;
    }

    public void resetPayments(){
        totalCash = 0.0;
        totalCard = 0.0;
        totalIncome = 0.0;
        paymentMethods.put("Cash", 0);
        paymentMethods.put("Card", 0);
    }
}
