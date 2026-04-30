package com.studytracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AppTest {

    @Test
    void sesjaNormalizujeDaneWejsciowe() {
        Sesja sesja = new Sesja(" Matematyka ", " 30 ", " Szkola ");

        assertEquals("Matematyka", sesja.getTemat());
        assertEquals(30, sesja.getCzas());
        assertEquals("szkola", sesja.getKategoria());
    }

    @Test
    void sesjaOdrzucaNiepoprawnyCzas() {
        assertThrows(IllegalArgumentException.class, () -> new Sesja("matematyka", "0", "szkola"));
        assertThrows(IllegalArgumentException.class, () -> new Sesja("matematyka", "abc", "szkola"));
        assertThrows(IllegalArgumentException.class, () -> new Sesja("matematyka", "-10", "szkola"));
        assertThrows(IllegalArgumentException.class, () -> new Sesja("matematyka", -10, "szkola"));
    }

    @Test
    void repositoryZapisujeIOdczytujeSesje(@TempDir Path tempDir) {
        File plik = tempDir.resolve("data/sesje.csv").toFile();
        SesjaRepository repository = new SesjaRepository(plik);

        repository.zapiszDoPliku(Arrays.asList(new Sesja("matematyka", "30", "szkola")));

        ArrayList<Sesja> sesje = repository.wczytajZPliku();
        assertEquals(1, sesje.size());
        assertEquals("matematyka", sesje.get(0).getTemat());
        assertEquals(30, sesje.get(0).getCzas());
        assertEquals("szkola", sesje.get(0).getKategoria());
    }

    @Test
    void serviceDodajeListujeIUsuwaSesje(@TempDir Path tempDir) {
        SesjaService service = new SesjaService(new SesjaRepository(tempDir.resolve("sesje.csv").toFile()));

        service.dodajSesje(new Sesja("historia", "45", "szkola"));

        assertEquals("1. historia - 45 - szkola\n", service.getAllAsString());
        assertEquals(Integer.valueOf(1), service.usunSesje("1"));
        assertEquals("", service.getAllAsString());
    }

    @Test
    void serviceOdrzucaNiepoprawnyNumerUsuwanejSesji(@TempDir Path tempDir) {
        SesjaService service = new SesjaService(new SesjaRepository(tempDir.resolve("sesje.csv").toFile()));
        service.dodajSesje(new Sesja("angielski", "20", "jezyki"));

        assertThrows(NumberFormatException.class, () -> service.usunSesje("abc"));
        assertThrows(IllegalArgumentException.class, () -> service.usunSesje("2"));
        assertThrows(IllegalArgumentException.class, () -> service.usunSesje(""));
    }

    @Test
    void repositoryZachowujePrzecinekWTemacie(@TempDir Path tempDir) {
        SesjaRepository repository = new SesjaRepository(tempDir.resolve("sesje.csv").toFile());
        repository.zapiszDoPliku(Arrays.asList(new Sesja("angielski,Present Simple", "20", "jezyki")));

        ArrayList<Sesja> sesje = repository.wczytajZPliku();
        assertEquals(1, sesje.size());
        Sesja sesja = sesje.get(0);
        assertEquals("angielski,Present Simple", sesja.getTemat());
        assertEquals(20, sesja.getCzas());
        assertEquals("jezyki", sesja.getKategoria());
    }

    @Test
    void repositoryZapisujeIOdczytujeUtf8(@TempDir Path tempDir) {
        SesjaRepository repository = new SesjaRepository(tempDir.resolve("sesje.csv").toFile());
        repository.zapiszDoPliku(Arrays.asList(new Sesja("Zażółć gęślą jaźń", "25", "języki")));

        ArrayList<Sesja> sesje = repository.wczytajZPliku();
        assertEquals(1, sesje.size());
        assertEquals("Zażółć gęślą jaźń", sesje.get(0).getTemat());
        assertEquals("języki", sesje.get(0).getKategoria());
    }

    @Test
    void serviceZachowujeStanGdyZapisSieNiePowiedzie() {
        ArrayList<Sesja> poczatkoweSesje = new ArrayList<>(Arrays.asList(new Sesja("historia", "45", "szkola")));
        SesjaService service = new SesjaService(new AwaryjnyRepository(poczatkoweSesje));

        assertThrows(RuntimeException.class, () -> service.dodajSesje(new Sesja("matematyka", "30", "szkola")));
        assertEquals("1. historia - 45 - szkola\n", service.getAllAsString());

        assertThrows(RuntimeException.class, () -> service.usunSesje("1"));
        assertEquals("1. historia - 45 - szkola\n", service.getAllAsString());
    }

    private static class AwaryjnyRepository extends SesjaRepository {
        private final ArrayList<Sesja> poczatkoweSesje;

        AwaryjnyRepository(ArrayList<Sesja> poczatkoweSesje) {
            super(new File("unused.csv"));
            this.poczatkoweSesje = poczatkoweSesje;
        }

        @Override
        public ArrayList<Sesja> wczytajZPliku() {
            return new ArrayList<>(poczatkoweSesje);
        }

        @Override
        public void zapiszDoPliku(List<Sesja> lista) {
            throw new RuntimeException("Awaria zapisu");
        }
    }
}
