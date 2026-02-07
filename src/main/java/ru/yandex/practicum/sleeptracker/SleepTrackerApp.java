package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.functions.*;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SleepTrackerApp {
    public static final String SEPARATOR = ";";
    private static final DateTimeFormatter LOG_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    private final List<Function<List<SleepingSession>, SleepAnalysisResult>> analyticFunction = List.of(
            new SleepingSessionCount(),
            new CountSessionsQualityBad(),
            new MinSessionDuration(),
            new MaxSessionDuration(),
            new AverageSessionLength(),
            new CountSleeplessNights(),
            new ClassifyingUserForChronotype()
    );

    public static void main(String[] args) {
        String filePath = args[0];
        SleepTrackerApp app = new SleepTrackerApp();
        try {
            List<SleepingSession> sessions = app.readFile(app.getFile(filePath));
            List<SleepAnalysisResult> results = app.analyzeSession(sessions);
            results.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла" + e.getMessage());
        }
    }

    private List<SleepAnalysisResult> analyzeSession(List<SleepingSession> sessions) {
        return analyticFunction.stream()
                .map(it -> it.apply(sessions))
                .toList();
    }

    private File getFile(String filename) throws FileNotFoundException {
        Path filePath = Paths.get(filename);
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("Не существует файла с именем %s", filename));
        }
        return file;
    }

    private List<SleepingSession> readFile(File file) {
        List<SleepingSession> sessions = new ArrayList<>();
        try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            sessions = bufferedReader.lines()
                    .map(this::parseLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            if (sessions.isEmpty()) {
                System.out.println("Прочитан пустой файл");
            }
        } catch (IOException exception) {
            System.out.println("Произошла ошибка при чтении файла " + file.getName());
        }
        return sessions;
    }

    private Optional<SleepingSession> parseLine(String line) {
        try {
            String[] parts = line.split(SEPARATOR);
            LocalDateTime start = LocalDateTime.parse(parts[0].trim(), LOG_TIME_FORMATTER);
            LocalDateTime end = LocalDateTime.parse(parts[1].trim(), LOG_TIME_FORMATTER);
            if (start.isAfter(end)) {
                return Optional.empty();
            }
            SleepQuality sleepQuality = SleepQuality.valueOf(parts[2].trim().toUpperCase());
            return Optional.of(new SleepingSession(start, end, sleepQuality));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}