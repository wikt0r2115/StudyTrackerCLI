package com.studytracker;

import java.util.ArrayList;

public class SesjaService {
    private ArrayList<Sesja> sesje;
    private SesjaRepository repository;
    public SesjaService() {
        sesje = new ArrayList<>();
        repository = new SesjaRepository();
        load();
    }

    private void load(){
        try{
            sesje = repository.wczytajZPliku();
        }catch(Exception e){
            sesje = new ArrayList<>();
        }
    }

    public void dodajSesje(Sesja s) {
        sesje.add(s);
        repository.zapiszDoPliku(sesje);
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
        sesje.remove(c - 1);
        repository.zapiszDoPliku(sesje);
        return c;
    }

    public String getAllAsString() {
        if (sesje.isEmpty())
            throw new IllegalStateException("Lista jest pusta");
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < sesje.size(); i++) {
            s.append(i + 1 + ". " + sesje.get(i) + "\n");
        }
        return s.toString();
    }
}
