package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClassifyingUserForChronotype implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Хронотип";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        Map<Chronotype, Long> countForCronotypes = sleepingSessions.stream()
                .filter(SleepingSession::isNightSleep)
                .collect(Collectors.groupingBy(SleepingSession::getChronotype, Collectors.counting()));

        Optional<Chronotype> maxChronotypeOptinal = countForCronotypes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        Chronotype maxChronotype;
        if (maxChronotypeOptinal.isPresent()) {
            maxChronotype = maxChronotypeOptinal.get();
        } else {
            throw new NoSuchElementException("Хронотип с максимальным количеством не нашелся");
        }

        boolean isMoreChronotypesWithThisNumber = countForCronotypes.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(maxChronotype))
                .anyMatch(entry -> entry.getValue().equals(countForCronotypes.get(maxChronotype)));

        Chronotype result = isMoreChronotypesWithThisNumber ? Chronotype.PIGEON : maxChronotype;
        return new SleepAnalysisResult(TITLE, result);
    }
}
