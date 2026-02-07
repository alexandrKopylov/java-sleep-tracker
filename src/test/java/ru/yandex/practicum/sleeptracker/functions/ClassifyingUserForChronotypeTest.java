package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassifyingUserForChronotypeTest {
    private ClassifyingUserForChronotype classifier;

    @BeforeEach
    public void setUp() {
        classifier = new ClassifyingUserForChronotype();
    }

    /**
     * Тест: все ночные сессии соответствуют хронотипу LARK.
     * Ожидаемый результат: LARK.
     */
    @Test
    public void apply_allLarkSessions_shouldReturnLark() {
        // LARK: засыпает 18:00–22:00, просыпается до 07:00
        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 20, 0),   // 20:00
                LocalDateTime.of(2025, 10, 2, 6, 0),     // 06:00 → LARK
                SleepQuality.GOOD
        );
        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 19, 0),   // 19:00
                LocalDateTime.of(2025, 10, 4, 6, 30),    // 06:30 → LARK
                SleepQuality.NORMAL
        );

        List<SleepingSession> sessions = List.of(session1, session2);
        SleepAnalysisResult result = classifier.apply(sessions);
        assertEquals("Хронотип", result.getFunctionTitle());
        assertEquals(Chronotype.LARK, result.getResult());
    }

    /**
     * Тест: все ночные сессии соответствуют хронотипу OWL.
     * Ожидаемый результат: OWL.
     */
    @Test
    public void apply_allOwlSessions_shouldReturnOwl() {
        // OWL: засыпает после 23:00, просыпается после 09:00
        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 5, 23, 30),  // 23:30
                LocalDateTime.of(2025, 10, 6, 9, 30),     // 09:30 → OWL
                SleepQuality.BAD
        );
        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 7, 0, 15),   // 00:15
                LocalDateTime.of(2025, 10, 8, 10, 0),    // 10:00 → OWL
                SleepQuality.GOOD
        );
        List<SleepingSession> sessions = List.of(session1, session2);
        SleepAnalysisResult result = classifier.apply(sessions);
        assertEquals("Хронотип", result.getFunctionTitle());
        assertEquals(Chronotype.OWL, result.getResult());
    }

    /**
     * Тест: равное количество сессий для LARK и OWL.
     * Ожидаемый результат: PIGEON (ничья).
     */
    @Test
    public void apply_tieBetweenLarkAndOwl_shouldReturnPigeon() {
        SleepingSession larkSession = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 21, 0),
                LocalDateTime.of(2025, 10, 2, 6, 45),
                SleepQuality.GOOD
        ); // LARK
        SleepingSession owlSession = new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 23, 45),
                LocalDateTime.of(2025, 10, 4, 9, 15),
                SleepQuality.NORMAL
        ); // OWL
        List<SleepingSession> sessions = List.of(larkSession, owlSession);
        SleepAnalysisResult result = classifier.apply(sessions);
        assertEquals("Хронотип", result.getFunctionTitle());
        assertEquals(Chronotype.PIGEON, result.getResult());
    }

    /**
     * Тест: большинство сессий — LARK, есть одна OWL.
     * Ожидаемый результат: LARK (преобладание).
     */
    @Test
    public void apply_majorityLark_shouldReturnLark() {
        SleepingSession lark1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 18, 30),
                LocalDateTime.of(2025, 10, 2, 6, 15),
                SleepQuality.GOOD
        );
        SleepingSession lark2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 20, 0),
                LocalDateTime.of(2025, 10, 4, 6, 45),
                SleepQuality.NORMAL
        );
        SleepingSession owl = new SleepingSession(
                LocalDateTime.of(2025, 10, 5, 0, 10),
                LocalDateTime.of(2025, 10, 6, 9, 20),
                SleepQuality.BAD
        );
        List<SleepingSession> sessions = List.of(lark1, lark2, owl);
        SleepAnalysisResult result = classifier.apply(sessions);
        assertEquals("Хронотип", result.getFunctionTitle());
        assertEquals(Chronotype.LARK, result.getResult());
    }
}