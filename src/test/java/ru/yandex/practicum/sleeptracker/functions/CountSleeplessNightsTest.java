package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CountSleeplessNightsTest {

    private CountSleeplessNights counter;

    @BeforeEach
    void setUp() {
        counter = new CountSleeplessNights();
    }

    /**
     * Тест: есть несколько ночных сессий, промежуток охватывает 5 ночей.
     * Ожидаемый результат: 5 − число ночей со сном = количество бессонных.
     */
    @Test
    void apply_withSeveralNightSessions_shouldCalculateCorrectly() {
        // Сессии охватывают период с 01.10.25 по 06.10.25 → 5 ночей (01–02, 02–03, 03–04, 04–05, 05–06)
        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 15),
                LocalDateTime.of(2025, 10, 2, 7, 30),
                null  // качество не важно для этого теста
        ); // ночь 01–02
        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 23, 40),
                LocalDateTime.of(2025, 10, 4, 8, 0),
                null
        ); // ночь 03–04
        SleepingSession session3 = new SleepingSession(
                LocalDateTime.of(2025, 10, 5, 0, 10),
                LocalDateTime.of(2025, 10, 5, 6, 20),
                null
        ); // ночь 04–05 (начало до 12:00 следующего дня)

        List<SleepingSession> sessions = List.of(session1, session2, session3);

        SleepAnalysisResult result = counter.apply(sessions);

        assertEquals("Колличество бессонных ночей", result.getFunctionTitle());
        assertEquals(1L, result.getResult());  // 5 ночей всего − 3 ночи со сном = 2 бессонные
    }

    /**
     * Тест: нет ночных сессий (все дневные).
     * Ожидаемый результат: все ночи в периоде считаются бессонными.
     */
    @Test
    void apply_onlyDaytimeSessions_shouldCountAllNightsAsSleepless() {
        SleepingSession daySession1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 14, 0),
                LocalDateTime.of(2025, 10, 1, 15, 30),
                null
        );
        SleepingSession daySession2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 13, 0),
                LocalDateTime.of(2025, 10, 3, 14, 45),
                null
        );

        List<SleepingSession> sessions = List.of(daySession1, daySession2);

        SleepAnalysisResult result = counter.apply(sessions);

        assertEquals("Колличество бессонных ночей", result.getFunctionTitle());
        assertEquals(2L, result.getResult());  // период: 01.10–03.10 → 2 ночи (01–02, 02–03)
    }

    /**
     * Тест: одна ночная сессия, период охватывает 1 ночь.
     * Ожидаемый результат: 0 бессонных ночей (вся ночь со сном).
     */
    @Test
    void apply_singleNightSession_shouldReturnZeroSleepless() {
        SleepingSession nightSession = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 15),
                LocalDateTime.of(2025, 10, 2, 7, 30),
                null
        ); // ночь 01–02

        List<SleepingSession> sessions = List.of(nightSession);

        SleepAnalysisResult result = counter.apply(sessions);

        assertEquals("Колличество бессонных ночей", result.getFunctionTitle());
        assertEquals(0L, result.getResult());  // 1 ночь всего − 1 ночь со сном = 0
    }

    /**
     * Тест: сессии с промежутком в несколько ночей без сна.
     * Ожидаемый результат: учитывает все ночи между первой и последней датой.
     */
    @Test
    void apply_spannedPeriodWithGaps_shouldCountGapsAsSleepless() {
        // Период: 01.10.25 – 10.10.25 → 9 ночей (01–02, ..., 09–10)
        SleepingSession first = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 7, 0),
                null
        ); // ночь 01–02
        SleepingSession last = new SleepingSession(
                LocalDateTime.of(2025, 10, 9, 23, 30),
                LocalDateTime.of(2025, 10, 10, 6, 30),
                null
        ); // ночь 09–10

        List<SleepingSession> sessions = List.of(first, last);
        SleepAnalysisResult result = counter.apply(sessions);
        assertEquals("Колличество бессонных ночей", result.getFunctionTitle());
        assertEquals(7L, result.getResult());
    }
}
