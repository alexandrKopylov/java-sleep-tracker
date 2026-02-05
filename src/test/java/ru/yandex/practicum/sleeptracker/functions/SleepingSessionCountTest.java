package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleepingSessionCountTest {

    private SleepingSessionCount sessionCounter;

    @BeforeEach
    void setUp() {
        sessionCounter = new SleepingSessionCount();
    }

    /**
     * Тест: несколько сессий разного типа.
     * Ожидаемый результат: общее количество сессий (независимо от качества и длительности).
     */
    @Test
    void apply_multipleSessions_shouldReturnTotalCount() {
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
                SleepQuality.NORMAL
        );

        List<SleepingSession> sessions = List.of(session1, session2, session3);

        SleepAnalysisResult result = sessionCounter.apply(sessions);

        assertEquals("общее количество сессий сна", result.getFunctionTitle());
        assertEquals(3, result.getResult());
    }

    /**
     * Тест: одна сессия.
     * Ожидаемый результат: 1.
     */
    @Test
    void apply_singleSession_shouldReturnOne() {
        SleepingSession session = new SleepingSession(
                LocalDateTime.of(2025, 10, 5, 0, 10),
                LocalDateTime.of(2025, 10, 5, 6, 20),
                SleepQuality.GOOD
        );
        List<SleepingSession> sessions = List.of(session);
        SleepAnalysisResult result = sessionCounter.apply(sessions);
        assertEquals("общее количество сессий сна", result.getFunctionTitle());
        assertEquals(1, result.getResult());
    }

    /**
     * Тест: пустой список сессий.
     * Ожидаемый результат: 0.
     */
    @Test
    void apply_emptyList_shouldReturnZero() {
        List<SleepingSession> sessions = List.of();
        SleepAnalysisResult result = sessionCounter.apply(sessions);
        assertEquals("общее количество сессий сна", result.getFunctionTitle());
        assertEquals(0, result.getResult());
    }

    /**
     * Тест: все сессии с одинаковым качеством (например, GOOD).
     * Ожидаемый результат: подсчёт всех сессий, независимо от качества.
     */
    @Test
    void apply_allGoodSessions_shouldCountAll() {
        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 22, 0),
                LocalDateTime.of(2025, 10, 2, 6, 0),
                SleepQuality.GOOD
        );
        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 7, 0),
                SleepQuality.GOOD
        );

        List<SleepingSession> sessions = List.of(session1, session2);

        SleepAnalysisResult result = sessionCounter.apply(sessions);


        assertEquals("общее количество сессий сна", result.getFunctionTitle());
        assertEquals(2, result.getResult());
    }

    /**
     * Тест: сессии с разными типами качества (GOOD, BAD, NORMAL).
     * Ожидаемый результат: сумма всех сессий, качество не влияет на подсчёт.
     */
    @Test
    void apply_mixedQualitySessions_shouldCountAll() {
        SleepingSession goodSession = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 7, 0),
                SleepQuality.GOOD
        );
        SleepingSession badSession = new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 0, 0),
                LocalDateTime.of(2025, 10, 3, 6, 0),
                SleepQuality.BAD
        );
        SleepingSession normalSession = new SleepingSession(
                LocalDateTime.of(2025, 10, 4, 22, 30),
                LocalDateTime.of(2025, 10, 5, 5, 30),
                SleepQuality.NORMAL
        );

        List<SleepingSession> sessions = List.of(goodSession, badSession, normalSession);

        SleepAnalysisResult result = sessionCounter.apply(sessions);

        assertEquals("общее количество сессий сна", result.getFunctionTitle());
        assertEquals(3, result.getResult());
    }

    /**
     * Тест: сессии, включающие ночные и дневные периоды.
     * Ожидаемый результат: учитываются все сессии, независимо от времени суток.
     */
    @Test
    void apply_dayAndNightSessions_shouldCountAll() {
        // Ночная сессия
        SleepingSession nightSession = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 7, 0),
                SleepQuality.GOOD
        );
        // Дневная сессия
        SleepingSession daySession = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 14, 0),
                LocalDateTime.of(2025, 10, 2, 16, 0),
                SleepQuality.NORMAL
        );

        List<SleepingSession> sessions = List.of(nightSession, daySession);

        SleepAnalysisResult result = sessionCounter.apply(sessions);

        assertEquals("общее количество сессий сна", result.getFunctionTitle());
        assertEquals(2, result.getResult());
    }
}
