package com.studytracker;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        SesjaService sesje = new SesjaService();
        String temat;
        String czas;
        String kategoria;
        Petla: while (true) {
            System.out.print(">");
            String komenda = scan.nextLine().trim().toLowerCase();
            switch (komenda) {
                case "add":
                    System.out.println("Podaj temat:");
                    temat = scan.nextLine();
                    System.out.println("Podaj czas(minuty):");
                    czas = scan.nextLine();
                    System.out.println("Podaj kategorie:");
                    kategoria = scan.nextLine();
                    try {
                        Sesja s = new Sesja(temat, czas, kategoria);
                        sesje.dodajSesje(s);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    System.out.println("Dodano sesje");
                    break;
                case "list":
                    try {
                        System.out.println(sesje.getAllAsString());
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "delete":
                    System.out.println("Podaj numer sesji:");
                    String numer = scan.nextLine();
                    try {
                        System.out.println("Usunieto sesje numer "+sesje.usunSesje(numer));
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                    } catch (NumberFormatException e) {
                        System.out.println("Numer sesji musi byc liczba");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "help":
                    System.out.println("add - dodaj sesje\ndelete - usun sesje\nlist - pokaż sesje\nexit - wyjscie\n");
                    break;
                case "exit":
                    break Petla;
                default:
                    System.out.println("Nieznana komenda dostepne: add list delete exit help");
            }
        }
        scan.close();

    }
}
