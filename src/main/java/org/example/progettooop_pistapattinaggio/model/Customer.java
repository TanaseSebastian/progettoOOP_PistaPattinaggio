package org.example.progettooop_pistapattinaggio.model;

import java.io.Serializable;

/**
 * Rappresenta un cliente della pista di pattinaggio.
 * Contiene informazioni personali come nome, età, telefono e stato di abbonamento.
 */
public class Customer implements Serializable {
    private String name;
    private int age;
    private String phone;
    private boolean isMember;

    /**
     * Costruttore completo per creare un cliente con tutte le informazioni.
     *
     * @param name      nome del cliente
     * @param age       età del cliente
     * @param phone     numero di telefono del cliente
     * @param isMember  {@code true} se il cliente ha una tessera fedeltà o abbonamento, altrimenti {@code false}
     */
    public Customer(String name, int age, String phone, boolean isMember) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.isMember = isMember;
    }

    /**
     * Costruttore semplificato per creare un cliente con solo il nome.
     * Gli altri campi vengono inizializzati a valori predefiniti.
     *
     * @param name nome del cliente
     */
    public Customer(String name) {
        this.name = name;
        this.age = 0;
        this.phone = "";
        this.isMember = false;
    }

    /**
     * Restituisce il nome del cliente.
     *
     * @return nome del cliente
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce l'età del cliente.
     *
     * @return età del cliente
     */
    public int getAge() {
        return age;
    }

    /**
     * Restituisce il numero di telefono del cliente.
     *
     * @return numero di telefono
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Indica se il cliente è abbonato.
     *
     * @return {@code true} se abbonato, {@code false} altrimenti
     */
    public boolean isMember() {
        return isMember;
    }

    /**
     * Imposta lo stato di abbonamento del cliente.
     *
     * @param member {@code true} se il cliente diventa abbonato, altrimenti {@code false}
     */
    public void setMember(boolean member) {
        isMember = member;
    }

    /**
     * Restituisce una rappresentazione testuale del cliente, utile per visualizzazioni nell'interfaccia.
     *
     * @return stringa descrittiva del cliente
     */
    @Override
    public String toString() {
        return name + " (" + age + " anni) - Tel: " + phone + (isMember ? " [ABBONATO]" : "");
    }
}
