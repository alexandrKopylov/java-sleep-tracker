package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AverageSessionLengthTest {

    private AverageSessionLength averageSessionLength;

    @BeforeEach
    void setUp() {
        averageSessionLength = new AverageSessionLength();
    }

    @Test
    void apply_normalSessions_shouldReturnCorrectAverage() {

        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 15),
                LocalDateTime.of(2025, 10, 2, 7, 30),
                SleepQuality.GOOD
        ); //    495 мин

        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 50),
                LocalDateTime.of(2025, 10, 3, 6, 40),
                SleepQuality.NORMAL
        ); //  410 мин

        SleepingSession session3 = new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 23, 40),
                LocalDateTime.of(2025, 10, 4, 8, 0),
                SleepQuality.BAD
        ); // 500 мин

        List<SleepingSession> sessions = List.of(session1, session2, session3);
        SleepAnalysisResult result = averageSessionLength.apply(sessions);
        //  1405 / 3 ≈ 468.33
        assertEquals("средняя продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(468L, result.getResult());
    }

    @Test
    void apply_singleSession_shouldReturnItsDuration() {
        SleepingSession session = new SleepingSession(
                LocalDateTime.of(2025, 10, 5, 0, 10),
                LocalDateTime.of(2025, 10, 5, 6, 20),
                SleepQuality.GOOD
        ); //  370   мин

        List<SleepingSession> sessions = List.of(session);
        SleepAnalysisResult result = averageSessionLength.apply(sessions);
        assertEquals("средняя продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(370L, result.getResult());
    }
}
