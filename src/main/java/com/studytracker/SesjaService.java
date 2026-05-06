package com.studytracker;

import java.util.ArrayList;

public class SesjaService {
    private ArrayList<Sesja> sesje;
    private final SesjaRepository repository;

    public SesjaService() {
        this(new SesjaRepository());
    }

    public SesjaService(SesjaRepository repository) {
        sesje = new ArrayList<>();
        this.repository = repository;
        load();
    }

    private void load() {
        sesje = repository.wczytajZPliku();
    }

    public void dodajSesje(Sesja s) {
        ArrayList<Sesja> noweSesje = new ArrayList<>(sesje);
        noweSesje.add(s);
        repository.zapiszDoPliku(noweSesje);
        sesje = noweSesje;
    }

    public int usunSesje(int numer) {
        if (sesje.isEmpty())
            throw new IllegalStateException("Lista jest pusta");
        if (numer <= 0 || numer > sesje.size())
            throw new IllegalArgumentException("Nie ma takiej sesji");
        ArrayList<Sesja> noweSesje = new ArrayList<>(sesje);
        noweSesje.remove(numer - 1);
        repository.zapiszDoPliku(noweSesje);
        sesje = noweSesje;
        return numer;
    }

    public String getAllAsString() {
        if (sesje.isEmpty())
            return "";
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < sesje.size(); i++) {
            s.append(i + 1 + ". " + sesje.get(i) + "\n");
        }
        return s.toString();
    }
}
