package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MinSessionDuration implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "минимальная продолжительность сессии (в минутах)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        long min = sleepingSessions.stream()
                .map(session -> Duration.between(session.getStart(), session.getEnd()))
                .min(Duration::compareTo)
                .orElse(Duration.ofMinutes(0))
                .toMinutes();
        return new SleepAnalysisResult(TITLE, min);
    }
}
