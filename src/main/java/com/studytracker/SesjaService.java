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

    public Integer usunSesje(String s) {
        if (sesje.isEmpty())
            throw new IllegalStateException("Lista jest pusta");
        if (s == null)
            throw new IllegalArgumentException("Numer jest nullem");
        s = s.trim();
        if (s.isEmpty())
            throw new IllegalArgumentException("Numer sesji nie moze byc pusty");
        int c = Integer.parseInt(s);
        if (c <= 0 || c > sesje.size())
            throw new IllegalArgumentException("Nie ma takiej sesji");
        ArrayList<Sesja> noweSesje = new ArrayList<>(sesje);
        noweSesje.remove(c - 1);
        repository.zapiszDoPliku(noweSesje);
        sesje = noweSesje;
        return c;
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
