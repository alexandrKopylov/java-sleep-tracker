package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class AverageSessionLength implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "средняя продолжительность сессии (в минутах)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long average = (long) sessions.stream()
                .map(session -> Duration.between(session.getStart(), session.getEnd()))
                .mapToInt(duration -> (int) duration.toMinutes())
                .average()
                .orElse(0.0);
        return new SleepAnalysisResult(TITLE, average);
    }
}
