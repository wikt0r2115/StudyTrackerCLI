package com.studytracker;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.RuntimeException;

public class SesjaRepository {
    private String sciezka = "src/main/resources/sesje.csv";
    private File plik = new File(sciezka);
    private String naglowek = "temat,czas,kategoria";

    public void zapiszDoPliku(List<Sesja> lista) {
        try (FileWriter zapis = new FileWriter(plik)) {
            zapis.write(naglowek + "\n");
            for (int i = 0; i < lista.size(); i++) {
                Sesja s = lista.get(i);
                zapis.write(s.getTemat() + "," + s.getCzas() + "," + s.getKategoria() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Nie mozna otworzyc pliku");
        }
    }

    public ArrayList<Sesja> wczytajZPliku(){
        ArrayList<Sesja> lista = new ArrayList<>();
        if(!plik.exists())
            return lista;
        try (BufferedReader odczyt = new BufferedReader(new FileReader(sciezka))) {
            String wiersz;
            odczyt.readLine();
            while ((wiersz = odczyt.readLine()) != null) {
                String[] wartosci = wiersz.split(",");
                if (wartosci.length == 3)
                    try {
                        lista.add(new Sesja(wartosci[0], wartosci[1], wartosci[2]));
                    } catch (IllegalArgumentException e) {

                    }
            }
            return lista;
        } catch (IOException e) {
            throw new RuntimeException("Blad odczytu pliku");
        }
    }
}
