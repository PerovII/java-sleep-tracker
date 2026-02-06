package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SleepLogReader {

    public List<SleepingSession> getSleepSessions(String filename) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        try (var lines = Files.lines(Path.of(filename))) {
            return lines
                    .map(line -> line.split(";"))
                    .map(parts -> new SleepingSession(
                            LocalDateTime.parse(parts[0], formatter),
                            LocalDateTime.parse(parts[1], formatter),
                            SleepQuality.valueOf(parts[2])))
                    .toList();
        }
    }
}
