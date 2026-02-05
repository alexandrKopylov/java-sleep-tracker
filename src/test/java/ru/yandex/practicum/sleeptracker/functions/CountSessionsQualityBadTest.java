package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CountSessionsQualityBadTest {

    private CountSessionsQualityBad counter;

    @BeforeEach
    void setUp() {
        counter = new CountSessionsQualityBad();
    }

    /**
     * Тест: есть несколько сессий с качеством BAD.
     * Ожидаемый результат: подсчёт всех BAD-сессий.
     */
    @Test
    void apply_hasBadSessions_shouldCountAllBad() {
        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 15),
                LocalDateTime.of(2025, 10, 2, 7, 30),
                SleepQuality.GOOD
        );
        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 50),
                LocalDateTime.of(2025, 10, 3, 6, 40),
                SleepQuality.BAD
        );
        SleepingSession session3 = new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 23, 40),
                LocalDateTime.of(2025, 10, 4, 8, 0),
                SleepQuality.BAD
        );
        SleepingSession session4 = new SleepingSession(
                LocalDateTime.of(2025, 10, 5, 0, 10),
                LocalDateTime.of(2025, 10, 5, 6, 20),
                SleepQuality.NORMAL
        );

        List<SleepingSession> sessions = List.of(session1, session2, session3, session4);

        SleepAnalysisResult result = counter.apply(sessions);
        assertEquals("Колличество сесий сна c плохим качеством", result.getFunctionTitle());
        assertEquals(2L, result.getResult());  // session2 и session3 — BAD
    }

    /**
     * Тест: нет сессий с качеством BAD.
     * Ожидаемый результат: 0.
     */
    @Test
    void apply_noBadSessions_shouldReturnZero() {
        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 15),
                LocalDateTime.of(2025, 10, 2, 7, 30),
                SleepQuality.GOOD
        );
        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 50),
                LocalDateTime.of(2025, 10, 3, 6, 40),
                SleepQuality.NORMAL
        );

        List<SleepingSession> sessions = List.of(session1, session2);

        SleepAnalysisResult result = counter.apply(sessions);

        assertEquals("Колличество сесий сна c плохим качеством", result.getFunctionTitle());
        assertEquals(0L, result.getResult());
    }
}
