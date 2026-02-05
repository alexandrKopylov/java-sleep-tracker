package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinSessionDurationTest {

    private MinSessionDuration minSessionDuration;

    @BeforeEach
    void setUp() {
        minSessionDuration = new MinSessionDuration();
    }

    /**
     * Тест: несколько сессий с разной длительностью.
     * Ожидаемый результат: возвращается минимальная длительность в минутах.
     */
    @Test
    void apply_multipleSessions_shouldReturnMinDuration() {
        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 22, 0),
                LocalDateTime.of(2025, 10, 2, 6, 0),
                null
        ); // 8 часов = 480 минут

        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 30),
                LocalDateTime.of(2025, 10, 3, 5, 30),
                null
        ); // 6 часов = 360 минут

        SleepingSession session3 = new SleepingSession(
                LocalDateTime.of(2025, 10, 4, 0, 15),
                LocalDateTime.of(2025, 10, 4, 1, 45),
                null
        ); // 1 час 30 минут = 90 минут

        List<SleepingSession> sessions = List.of(session1, session2, session3);

        SleepAnalysisResult result = minSessionDuration.apply(sessions);

        assertEquals("минимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(90L, result.getResult());
    }

    /**
     * Тест: одна сессия.
     * Ожидаемый результат: длительность этой сессии в минутах.
     */
    @Test
    void apply_singleSession_shouldReturnItsDuration() {
        SleepingSession session = new SleepingSession(
                LocalDateTime.of(2025, 10, 5, 23, 0),
                LocalDateTime.of(2025, 10, 6, 7, 30),
                null
        ); // 8 часов 30 минут = 510 минут

        List<SleepingSession> sessions = List.of(session);

        SleepAnalysisResult result = minSessionDuration.apply(sessions);

        assertEquals("минимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(510L, result.getResult());
    }

    /**
     * Тест: все сессии одинаковой длительности.
     * Ожидаемый результат: эта длительность (в минутах).
     */
    @Test
    void apply_allSessionsSameDuration_shouldReturnThatDuration() {
        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 7, 0),
                null
        ); // 8 часов = 480 минут

        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 22, 30),
                LocalDateTime.of(2025, 10, 4, 6, 30),
                null
        ); // 8 часов = 480 минут

        List<SleepingSession> sessions = List.of(session1, session2);

        SleepAnalysisResult result = minSessionDuration.apply(sessions);

        assertEquals("минимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(480L, result.getResult());
    }

    /**
     * Тест: пустой список сессий.
     * Ожидаемый результат: 0 минут.
     */
    @Test
    void apply_emptyList_shouldReturnZero() {
        List<SleepingSession> sessions = List.of();

        SleepAnalysisResult result = minSessionDuration.apply(sessions);

        assertEquals("минимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(0L, result.getResult());
    }

    /**
     * Тест: сессии с нулевой длительностью (начало = конец).
     * Ожидаемый результат: 0 минут (минимум из нулей).
     */
    @Test
    void apply_zeroDurationSessions_shouldReturnZero() {
        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 12, 0),
                LocalDateTime.of(2025, 10, 1, 12, 0),
                null
        ); // 0 минут

        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 15, 30),
                LocalDateTime.of(2025, 10, 2, 15, 30),
                null
        ); // 0 минут

        List<SleepingSession> sessions = List.of(session1, session2);

        SleepAnalysisResult result = minSessionDuration.apply(sessions);

        assertEquals("минимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(0L, result.getResult());
    }

    /**
     * Тест: среди сессий есть одна длительностью 1 минута (минимум).
     * Ожидаемый результат: 1 минута.
     */
    @Test
    void apply_oneMinuteSession_shouldReturnOne() {
        SleepingSession longSession = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 10, 0),
                LocalDateTime.of(2025, 10, 1, 14, 0),
                null
        ); // 4 часа = 240 минут

        SleepingSession shortSession = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 16, 0),
                LocalDateTime.of(2025, 10, 1, 16, 1),
                null
        ); // 1 минута

        List<SleepingSession> sessions = List.of(longSession, shortSession);

        SleepAnalysisResult result = minSessionDuration.apply(sessions);

        assertEquals("минимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(1L, result.getResult());
    }

    /**
     * Тест: сессия длительностью менее минуты (например, 30 секунд).
     * Ожидаемый результат: округляется до 0 минут (так как считаем в целых минутах).
     */
    @Test
    void apply_lessThanOneMinute_shouldRoundToZero() {
        SleepingSession veryShortSession = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 12, 0, 0),
                LocalDateTime.of(2025, 10, 1, 12, 0, 30), // 30 секунд
                null
        );

        List<SleepingSession> sessions = List.of(veryShortSession);

        SleepAnalysisResult result = minSessionDuration.apply(sessions);

        assertEquals("минимальная продолжительность сессии (в минутах)", result.getFunctionTitle());
        assertEquals(0L, result.getResult());
    }
}
