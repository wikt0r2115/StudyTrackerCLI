package com.studytracker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.lang.RuntimeException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class SesjaRepository {
    private final File plik;

    public SesjaRepository() {
        this(new File(System.getProperty("user.home"), ".studytracker/sesje.csv"));
    }

    public SesjaRepository(File plik) {
        this.plik = plik;
    }

    public void zapiszDoPliku(List<Sesja> lista) {
        Path docelowyPlik = plik.toPath().toAbsolutePath();
        Path katalog = docelowyPlik.getParent();
        Path tymczasowyPlik = null;

        try {
            Files.createDirectories(katalog);
            tymczasowyPlik = Files.createTempFile(katalog, "sesje-", ".tmp");
            try (BufferedWriter zapis = Files.newBufferedWriter(tymczasowyPlik, StandardCharsets.UTF_8);
                CSVPrinter csvPrinter = new CSVPrinter(zapis, CSVFormat.DEFAULT
                        .builder()
                        .setHeader("temat", "czas", "kategoria")
                        .get())) {
                for (int i = 0; i < lista.size(); i++) {
                    Sesja s = lista.get(i);
                    csvPrinter.printRecord(s.getTemat(), s.getCzas(), s.getKategoria());
                }
            }
            przeniesPlik(tymczasowyPlik, docelowyPlik);
        } catch (IOException e) {
            usunTymczasowyPlik(tymczasowyPlik);
            throw new RuntimeException("Nie mozna zapisac pliku", e);
        }
    }

    public ArrayList<Sesja> wczytajZPliku() {
        ArrayList<Sesja> lista = new ArrayList<>();
        int i = 2;
        Path sciezka = plik.toPath();
        if (!Files.exists(sciezka))
            return lista;
        try (BufferedReader odczyt = Files.newBufferedReader(sciezka, StandardCharsets.UTF_8);
                CSVParser parser = CSVFormat.DEFAULT
                        .builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .get()
                        .parse(odczyt)) {
            for (CSVRecord wiersz : parser) {
                try {
                    lista.add(new Sesja(wiersz.get("temat"), wiersz.get("czas"), wiersz.get("kategoria")));
                } catch (IllegalArgumentException e) {
                    throw new IllegalStateException("W linii " + i + " - " + e.getMessage());
                }
                i++;
            }
            return lista;
        } catch (IOException e) {
            throw new RuntimeException("Blad odczytu pliku");
        }
    }

    private void przeniesPlik(Path zrodlo, Path cel) throws IOException {
        try {
            Files.move(zrodlo, cel, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(zrodlo, cel, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void usunTymczasowyPlik(Path tymczasowyPlik) {
        if (tymczasowyPlik == null)
            return;
        try {
            Files.deleteIfExists(tymczasowyPlik);
        } catch (IOException e) {
            // Nie przeslaniamy pierwotnego bledu zapisu.
        }
    }
}
