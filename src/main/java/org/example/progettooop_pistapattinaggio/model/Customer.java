package org.example.progettooop_pistapattinaggio.model;

import javafx.beans.value.ObservableValue;

import java.io.Serializable;

public class Customer implements Serializable {
    private String name;
    private int age;
    private String phone;
    private boolean isMember; // tessera fedeltà o abbonamento

    public Customer(String name, int age, String phone, boolean isMember) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.isMember = isMember;
    }

    public Customer(String name) {
        this.name = name;
        this.age = 0;
        this.phone = "";
        this.isMember = false;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    @Override
    public String toString() {
        return name + " (" + age + " anni) - Tel: " + phone + (isMember ? " [ABBONATO]" : "");
    }

}
