package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class CountSessionsQualityBad implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Колличество сесий сна c плохим качеством";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        long count = sleepingSessions.stream()
                .filter(ses -> ses.getQuality().equals(SleepQuality.BAD))
                .count();
        return new SleepAnalysisResult(TITLE, count);
    }
}
