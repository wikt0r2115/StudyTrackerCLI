package com.studytracker;

public class Sesja {
    private String temat;
    private int czas;
    private String kategoria;

    public Sesja(String temat, String czas, String kategoria) {
        this(temat, validateCzas(czas), kategoria);
    }

    public Sesja(String temat, int czas, String kategoria) {
        this.temat = validateTemat(temat);
        this.czas = validateCzas(czas);
        this.kategoria = validateKategorie(kategoria);
    }

    public void setTemat(String temat) {
        this.temat = validateTemat(temat);
    }

    public void setCzas(String czas) {
        this.czas = validateCzas(czas);
    }

    public void setKategoria(String kategoria) {
        this.kategoria = validateKategorie(kategoria);
    }

    public String getTemat() {
        return temat;
    }

    public int getCzas() {
        return czas;
    }

    public String getKategoria() {
        return kategoria;
    }

    private String validateTemat(String string) {
        if (string == null)
            throw new IllegalArgumentException("Temat nie moze byc nullem");
        string = string.trim();
        if (string.isEmpty())
            throw new IllegalArgumentException("Temat nie moze byc pusty");
        return string;
    }

    private String validateKategorie(String string) {
        if (string == null)
            throw new IllegalArgumentException("Kategoria nie moze byc nullem");
        string = string.trim().toLowerCase();
        if (string.isEmpty())
            throw new IllegalArgumentException("Kategoria nie moze byc pusty");
        return string;
    }

    private static int validateCzas(String czas) {
        if (czas == null)
            throw new IllegalArgumentException("Czas nie moze byc nullem");
        czas = czas.trim();
        int c;
        try {
            c = Integer.parseInt(czas);
            validateCzas(c);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Czas musi byc liczba");
        }
        return c;
    }

    private static int validateCzas(int czas) {
        if (czas <= 0)
            throw new IllegalArgumentException("Czas musi byc wiekszy od 0");
        return czas;
    }

    public String toString() {
        return this.temat + " - " + this.czas + " - " + this.kategoria;
    }
}
