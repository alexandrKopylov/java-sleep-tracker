package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.*;
import java.util.List;
import java.util.function.Function;

public class CountSleeplessNights implements Function<List<SleepingSession>, SleepAnalysisResult> {

    public static final String TITLE = "Колличество бессонных ночей";
    private static final LocalTime BORDER_DAY = LocalTime.of(12, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        LocalDate firstDay = getDay(sessions.get(0));
        LocalDate lastDay = sessions.get(sessions.size() - 1).getEnd().toLocalDate();
        int totalNights = Period.between(firstDay, lastDay).getDays();
        List<SleepingSession> nightsWithSleep = sessions.stream()
                .filter(SleepingSession::isNightSleep)
                .toList();
        long sleeplessNightsCount = totalNights - nightsWithSleep.size();
        return new SleepAnalysisResult(TITLE, sleeplessNightsCount);
    }

    private LocalDate getDay(SleepingSession session) {
        if (session.getStart().toLocalTime().isBefore(BORDER_DAY)) {
            return session.getStart().minusDays(1).toLocalDate();
        }
        return session.getStart().toLocalDate();
    }
}
