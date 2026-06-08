# StudyTracker CLI

[![CI](https://github.com/wikt0r2115/StudyTrackerCLI/actions/workflows/ci.yml/badge.svg)](https://github.com/wikt0r2115/StudyTrackerCLI/actions/workflows/ci.yml)

[Polska wersja](README.pl.md)

StudyTracker CLI is a small Java command-line application for recording study
sessions. It is built as a portfolio project, with emphasis on readable code,
testable command handling, validation, and simple durable storage.

The CLI text is currently in Polish.

## Features

- Add, list, and delete study sessions.
- Validate session topic, duration, and category.
- Store data in `~/.studytracker/sesje.csv`.
- Read and write CSV with UTF-8 encoding through Apache Commons CSV.
- Preserve commas and non-ASCII characters in session data.
- Write data through a temporary file before replacing the target CSV.
- Keep service state unchanged when persistence fails.
- Separate CLI input parsing from application service logic.
- Return non-zero exit codes for interrupted input or storage failures.
- Cover model, repository, service, and CLI behavior with JUnit tests.

## Requirements

- Java 8 or newer.
- No local Maven installation is required. The Maven Wrapper is included.

## Run

From the repository root:

```bash
./mvnw package
java -jar target/studytracker-1.0-SNAPSHOT.jar
```

On Windows:

```bat
mvnw.cmd package
java -jar target\studytracker-1.0-SNAPSHOT.jar
```

## Commands

```text
help   - show available commands
add    - add a study session
list   - show saved sessions
delete - delete a session by number
exit   - close the application
```

Example session:

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

## Test

```bash
./mvnw test
```

The test suite covers validation, CSV persistence, UTF-8 handling, CLI flows,
delete command parsing, and failure behavior during writes.

## Data Storage

By default, production data is stored outside the repository:

```text
~/.studytracker/sesje.csv
```

The CSV file contains a header and three fields:

```text
temat,czas,kategoria
```

The repository layer uses UTF-8 and Apache Commons CSV, so values such as
`angielski,Present Simple` or `Zażółć gęślą jaźń` are handled correctly.

## Project Structure

```text
src/main/java/com/studytracker/App.java
src/main/java/com/studytracker/Sesja.java
src/main/java/com/studytracker/SesjaService.java
src/main/java/com/studytracker/SesjaRepository.java
src/test/java/com/studytracker/AppTest.java
```

- `App` handles CLI input, output, parsing, and exit codes.
- `Sesja` represents and validates a study session.
- `SesjaService` owns the in-memory list and business operations.
- `SesjaRepository` handles CSV persistence.
- `AppTest` covers the current behavior end to end at unit and CLI level.

## Design Notes

The `delete` command keeps text parsing in the CLI layer. `SesjaService`
receives a typed `int` session number and validates it against the current list.
This keeps command-line concerns out of the service layer.

The service updates its in-memory state only after the repository write
succeeds. This prevents the running application from drifting away from the
saved CSV when persistence fails.

## Next Improvements

- Add dates or stable IDs for sessions.
- Add basic statistics, filtering, or export commands.
- Consider extracting command handlers if the CLI grows further.
