package com.studytracker;

public class Sesja {
    private String temat;
    private int czas;
    private String kategoria;

    public Sesja(String temat, String czas, String kategoria) {
        this.temat = validateString(temat, "Temat");
        this.czas = validateCzas(czas);
        this.kategoria = validateString(kategoria, "Kategoria");
    }

    public void setTemat(String temat) {
        this.temat = validateString(temat, "Temat");
    }

    public void setCzas(String czas) {
        this.czas = validateCzas(czas);
    }

    public void setKategoria(String kategoria) {
        this.kategoria = validateString(kategoria, "Kategoria");
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

    private String validateString(String string, String co) {
        if (string == null)
            throw new IllegalArgumentException(co + " nie moze byc nullem");
        string = string.trim().toLowerCase();
        if (string.isEmpty())
            throw new IllegalArgumentException(co + " nie moze byc pusty");
        return string;
    }

    private int validateCzas(String czas) {
        czas = czas.trim();
        int c;
        try {
            c = Integer.parseInt(czas);
            if (c <= 0)
                throw new IllegalArgumentException("Liczba nie może być mniejsza badz rowna 0");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Czas musi byc liczba");
        }
        return c;
    }

    public String toString() {
        return this.temat + " - " + this.czas + " - " + this.kategoria;
    }
}
