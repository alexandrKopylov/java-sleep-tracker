package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaxSessionDurationTest {
    private MaxSessionDuration maxDuration;

    @BeforeEach
    public void setUp() {
        maxDuration = new MaxSessionDuration();
    }

    /**
     * Тест: несколько сессий разной длительности.
     * Ожидаемый результат: возвращается максимальная длительность (в минутах).
     */
    @Test
    public void apply_multipleSessions_shouldReturnMaxDuration() {
        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 15),
                LocalDateTime.of(2025, 10, 2, 7, 30),
                null
        ); // 8 ч 15 мин = 495 мин
        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 50),
                LocalDateTime.of(2025, 10, 3, 6, 40),
                null
        ); // 6 ч 50 мин = 410 мин
        SleepingSession session3 = new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 23, 40),
                LocalDateTime.of(2025, 10, 4, 8, 0),
                null
        ); // 8 ч 20 мин = 500 мин

        List<SleepingSession> sessions = List.of(session1, session2, session3);
        SleepAnalysisResult result = maxDuration.apply(sessions);
        assertEquals("максимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(500L, result.getResult());  // session3 — самая длинная
    }

    /**
     * Тест: одна сессия.
     * Ожидаемый результат: длительность этой сессии.
     */
    @Test
    public void apply_singleSession_shouldReturnItsDuration() {
        SleepingSession session = new SleepingSession(
                LocalDateTime.of(2025, 10, 5, 0, 10),
                LocalDateTime.of(2025, 10, 5, 6, 20),
                null
        ); // 6 ч 10 мин = 370 мин
        List<SleepingSession> sessions = List.of(session);
        SleepAnalysisResult result = maxDuration.apply(sessions);
        assertEquals("максимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(370L, result.getResult());
    }

    /**
     * Тест: все сессии одинаковой длительности.
     * Ожидаемый результат: эта длительность.
     */
    @Test
    public void apply_allSessionsSameDuration_shouldReturnThatDuration() {
        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 22, 0),
                LocalDateTime.of(2025, 10, 2, 6, 0),
                null
        ); // 8 ч = 480 мин
        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 7, 0),
                null
        ); // 8 ч = 480 мин

        List<SleepingSession> sessions = List.of(session1, session2);
        SleepAnalysisResult result = maxDuration.apply(sessions);
        assertEquals("максимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(480L, result.getResult());
    }

    /**
     * Тест: пустой список сессий.
     * Ожидаемый результат: 0.
     */
    @Test
    public void apply_emptyList_shouldReturnZero() {
        List<SleepingSession> sessions = List.of();
        SleepAnalysisResult result = maxDuration.apply(sessions);
        assertEquals("максимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(0L, result.getResult());
    }

    /**
     * Тест: сессии с нулевой длительностью (начало = конец).
     * Ожидаемый результат: 0.
     */
    @Test
    public void apply_zeroDurationSessions_shouldReturnZero() {
        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 15),
                LocalDateTime.of(2025, 10, 1, 23, 15),
                null
        ); // 0 мин
        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 0, 0),
                LocalDateTime.of(2025, 10, 2, 0, 0),
                null
        ); // 0 мин
        List<SleepingSession> sessions = List.of(session1, session2);
        SleepAnalysisResult result = maxDuration.apply(sessions);
        assertEquals("максимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(0L, result.getResult());
    }

    /**
     * Тест: сессия длительностью ровно 1 минута.
     * Ожидаемый результат: 1.
     */
    @Test
    public void apply_oneMinuteSession_shouldReturnOne() {
        SleepingSession session = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 12, 0),
                LocalDateTime.of(2025, 10, 1, 12, 1),
                null
        ); // 1 мин
        List<SleepingSession> sessions = List.of(session);
        SleepAnalysisResult result = maxDuration.apply(sessions);
        assertEquals("максимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(1L, result.getResult());
    }

    /**
     * Тест: сессии с очень большой длительностью (более суток).
     * Ожидаемый результат: корректное вычисление в минутах.
     */
    @Test
    public void apply_longSessionOver24Hours_shouldCalculateCorrectly() {
        SleepingSession longSession = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 10, 0),
                LocalDateTime.of(2025, 10, 2, 14, 0),  // 28 часов = 1680 минут
                null
        );
        List<SleepingSession> sessions = List.of(longSession);
        SleepAnalysisResult result = maxDuration.apply(sessions);
        assertEquals("максимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(1680L, result.getResult());
    }
}
