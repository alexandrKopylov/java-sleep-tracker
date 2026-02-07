package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class SleepingSessionCount implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "общее количество сессий сна";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        Integer count = sleepingSessions.size();
        return new SleepAnalysisResult(TITLE, count);
    }
}
