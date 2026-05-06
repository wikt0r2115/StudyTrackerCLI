package com.studytracker;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        int exitCode = run(System.in, System.out, System.err);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    static int run(InputStream input, PrintStream out, PrintStream err) {
        SesjaService sesje;
        try {
            sesje = new SesjaService();
        } catch (RuntimeException e) {
            err.println("Nie udalo sie wczytac danych: " + e.getMessage());
            return 1;
        }
        return run(input, out, err, sesje);
    }

    static int run(InputStream input, PrintStream out, PrintStream err, SesjaService sesje) {
        Scanner scan = new Scanner(input, "UTF-8");
        String temat;
        String czas;
        String kategoria;

        try {
            while (true) {
                out.print("> ");
                if (!scan.hasNextLine()) {
                    out.println();
                    return 0;
                }

                String komenda = scan.nextLine().trim().toLowerCase();
                switch (komenda) {
                    case "":
                        break;
                    case "add":
                        out.println("Podaj temat:");
                        temat = wczytajWymaganaLinie(scan, err);
                        if (temat == null)
                            return 1;
                        out.println("Podaj czas(minuty):");
                        czas = wczytajWymaganaLinie(scan, err);
                        if (czas == null)
                            return 1;
                        out.println("Podaj kategorie:");
                        kategoria = wczytajWymaganaLinie(scan, err);
                        if (kategoria == null)
                            return 1;
                        try {
                            Sesja s = new Sesja(temat, czas, kategoria);
                            sesje.dodajSesje(s);
                            out.println("Dodano sesje");
                        } catch (IllegalArgumentException e) {
                            err.println(e.getMessage());
                        } catch (RuntimeException e) {
                            err.println("Nie udalo sie zapisac danych: " + e.getMessage());
                            return 1;
                        }
                        break;
                    case "list":
                        String lista = sesje.getAllAsString();
                        if (lista.isEmpty())
                            out.println("Lista jest pusta");
                        else
                            out.println(lista);
                        break;
                    case "delete":
                        out.println("Podaj numer sesji:");
                        String numer = wczytajWymaganaLinie(scan, err);
                        if (numer == null)
                            return 1;
                        try {
                            int c = parseInt(numer);
                            out.println("Usunieto sesje numer " + sesje.usunSesje(c));
                        } catch (IllegalArgumentException e) {
                            err.println(e.getMessage());
                            break;
                        } catch (IllegalStateException e) {
                            err.println(e.getMessage());
                            break;
                        } catch (RuntimeException e) {
                            err.println("Nie udalo sie zapisac danych: " + e.getMessage());
                            return 1;
                        }
                        break;
                    case "help":
                        wyswietlPomoc(out);
                        break;
                    case "exit":
                        return 0;
                    default:
                        err.println("Nieznana komenda. Dostepne: add list delete exit help");
                }
            }
        } finally {
            scan.close();
        }
    }

    private static int parseInt(String numer) {
        if (numer == null)
            throw new IllegalArgumentException("Numer sesji nie moze byc null");
        numer = numer.trim();
        if (numer.isEmpty())
            throw new IllegalArgumentException("Numer sesji nie moze byc pusty");
        try {
            return Integer.parseInt(numer);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Numer sesji musi byc liczba", e);
        }
    }

    private static String wczytajWymaganaLinie(Scanner scan, PrintStream err) {
        if (!scan.hasNextLine()) {
            err.println("Przerwano wprowadzanie danych");
            return null;
        }
        return scan.nextLine();
    }

    private static void wyswietlPomoc(PrintStream out) {
        out.println("Dostepne komendy:");
        out.println("  add    - dodaj sesje");
        out.println("  list   - pokaz sesje");
        out.println("  delete - usun sesje");
        out.println("  help   - pokaz pomoc");
        out.println("  exit   - wyjscie");
    }
}
