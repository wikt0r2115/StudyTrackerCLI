# StudyTracker CLI

[![CI](https://github.com/wikt0r2115/StudyTrackerCLI/actions/workflows/ci.yml/badge.svg)](https://github.com/wikt0r2115/StudyTrackerCLI/actions/workflows/ci.yml)

[English version](README.md)

StudyTracker CLI to mała aplikacja konsolowa w Javie do zapisywania sesji
nauki. Projekt jest rozwijany jako element portfolio, z naciskiem na czytelny
kod, testowalną obsługę komend, walidację danych i prosty trwały zapis.

Interfejs CLI jest obecnie po polsku.

## Funkcje

- Dodawanie, listowanie i usuwanie sesji nauki.
- Walidacja tematu, czasu trwania i kategorii.
- Zapis danych w `~/.studytracker/sesje.csv`.
- Odczyt i zapis CSV w UTF-8 przez Apache Commons CSV.
- Poprawna obsługa przecinków i znaków spoza ASCII w danych sesji.
- Zapis przez plik tymczasowy przed podmianą docelowego CSV.
- Zachowanie stanu serwisu, gdy zapis na dysk się nie powiedzie.
- Oddzielenie parsowania wejścia CLI od logiki serwisu.
- Niezerowe kody wyjścia przy przerwanym wejściu lub awarii storage.
- Testy JUnit dla modelu, repozytorium, serwisu i zachowania CLI.

## Wymagania

- Java 8 lub nowsza.
- Lokalna instalacja Mavena nie jest potrzebna. Projekt zawiera Maven Wrapper.

## Uruchomienie

Z katalogu głównego repozytorium:

```bash
./mvnw package
java -jar target/studytracker-1.0-SNAPSHOT.jar
```

Na Windowsie:

```bat
mvnw.cmd package
java -jar target\studytracker-1.0-SNAPSHOT.jar
```

## Komendy

```text
help   - pokazuje dostępne komendy
add    - dodaje sesję nauki
list   - pokazuje zapisane sesje
delete - usuwa sesję po numerze
exit   - zamyka aplikację
```

Przykładowa sesja:

```text
> add
Podaj temat:
matematyka
Podaj czas(minuty):
45
Podaj kategorie:
szkola
Dodano sesje
> list
1. matematyka - 45 - szkola
> delete
Podaj numer sesji:
1
Usunieto sesje numer 1
> exit
```

## Testy

```bash
./mvnw test
```

Zestaw testów obejmuje walidację, zapis CSV, obsługę UTF-8, przepływy CLI,
parsowanie komendy `delete` i zachowanie aplikacji przy błędach zapisu.

## Przechowywanie Danych

Domyślnie dane produkcyjne są zapisywane poza repozytorium:

```text
~/.studytracker/sesje.csv
```

Plik CSV zawiera nagłówek i trzy pola:

```text
temat,czas,kategoria
```

Warstwa repozytorium używa UTF-8 i Apache Commons CSV, więc wartości takie jak
`angielski,Present Simple` albo `Zażółć gęślą jaźń` są obsługiwane poprawnie.

## Struktura Projektu

```text
src/main/java/com/studytracker/App.java
src/main/java/com/studytracker/Sesja.java
src/main/java/com/studytracker/SesjaService.java
src/main/java/com/studytracker/SesjaRepository.java
src/test/java/com/studytracker/AppTest.java
```

- `App` obsługuje wejście CLI, wyjście, parsowanie i kody wyjścia.
- `Sesja` reprezentuje i waliduje pojedynczą sesję nauki.
- `SesjaService` zarządza listą w pamięci i operacjami biznesowymi.
- `SesjaRepository` odpowiada za zapis i odczyt CSV.
- `AppTest` testuje obecne zachowanie na poziomie jednostkowym i CLI.

## Decyzje Techniczne

Komenda `delete` parsuje tekst w warstwie CLI. `SesjaService` otrzymuje już
typowany numer sesji jako `int` i sprawdza go względem aktualnej listy. Dzięki
temu format wejścia konsolowego nie przecieka do warstwy serwisu.

Serwis aktualizuje stan w pamięci dopiero po udanym zapisie w repozytorium.
To chroni aplikację przed rozjazdem między stanem działającego programu a
zapisanym plikiem CSV, jeśli persistence zakończy się błędem.

## Następne Kroki

- Dodać GitHub Actions CI dla `./mvnw test`.
- Dodać daty albo stabilne ID sesji.
- Dodać podstawowe statystyki, filtrowanie albo eksport.
- Rozważyć wydzielenie handlerów komend, jeśli CLI urośnie.
