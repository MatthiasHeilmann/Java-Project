package com.example.javaproject;

/**
 * Kurs class
 */
public class Kurs {
    private int kId;
    private String bezeichnung;
    private String raum;

    public Kurs(int kId, String bezeichnung, String raum) {
        this.kId = kId;
        this.bezeichnung = bezeichnung;
        this.raum = raum;
    }

    public int getkId() {
        return kId;
    }

    public void setkId(int kId) {
        this.kId = kId;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getRaum() {
        return raum;
    }

    public void setRaum(String raum) {
        this.raum = raum;
    }

    public void update(Kurs kurs) {
        this.kId=kurs.getkId();
        this.bezeichnung=kurs.getBezeichnung();
        this.raum=kurs.getRaum();
    }
}
