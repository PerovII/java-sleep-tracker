package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {


    private List<SleepingSession> sleepingSessions;

    private static SleepingSession session(String startTime, String endTime, SleepQuality sleepQuality) {
        return new SleepingSession(
                LocalDateTime.parse(startTime),
                LocalDateTime.parse(endTime),
                sleepQuality);
    }

    @BeforeEach
    void setUp() {
        sleepingSessions = List.of(

                session("2025-10-01T23:00", "2025-10-02T07:00", SleepQuality.GOOD), // 480 мин.
                session("2025-10-02T14:00", "2025-10-02T15:00", SleepQuality.BAD), // 60 мин.
                session("2025-10-03T13:30", "2025-10-03T14:00", SleepQuality.BAD), // 30 мин.
                session("2025-10-04T00:00", "2025-10-04T09:00", SleepQuality.NORMAL), // 540 мин.
                session("2025-10-05T22:00", "2025-10-06T05:00", SleepQuality.NORMAL) // 420 мин.
        );
    }

    @Test
    void numberOfSleepSessions_countsAll() {
        assertEquals(5, new NumberOfSleepSessions().apply(sleepingSessions).getValue());
    }

    @Test
    void minimumDurationSession_returnsMinMinutes() {
        assertEquals(30L, new MinimumDurationSession().apply(sleepingSessions).getValue());
    }

    @Test
    void maximumDurationSession_returnsMaxMinutes() {
        assertEquals(540L, new MaximumDurationSession().apply(sleepingSessions).getValue());
    }

    @Test
    void averageDurationSession_returnsAverageMinutes() {
        SleepAnalysisResult<Long> result = new AverageDurationSession().apply(sleepingSessions);
        assertEquals(306L, result.getValue());
    }

    @Test
    void numberOfBadSession_returnsCountOfBadSessions() {
        assertEquals(2L, new NumberOfBadSessions().apply(sleepingSessions).getValue());
    }

    @Test
    void minMaxAverageBad_onEmptyList_return_0() {
        List<SleepingSession> empty = List.of();
        assertEquals(0L, new MinimumDurationSession().apply(empty).getValue());
        assertEquals(0L, new MaximumDurationSession().apply(empty).getValue());
        assertEquals(0L, new AverageDurationSession().apply(empty).getValue());
        assertEquals(0L, new NumberOfBadSessions().apply(empty).getValue());
    }

    @Test
    void numberOfSleeplessNights_return_2() {

        sleepingSessions = List.of(

                session("2025-10-01T23:00", "2025-10-02T01:00", SleepQuality.GOOD),
                session("2025-10-02T02:00", "2025-10-02T07:00", SleepQuality.GOOD),
                session("2025-10-02T14:00", "2025-10-02T15:00", SleepQuality.BAD),
                session("2025-10-03T13:30", "2025-10-03T14:00", SleepQuality.BAD)
        );

        assertEquals(2L, new NumberOfSleeplessNights().apply(sleepingSessions).getValue());
    }

    @Test
    void numberOfSleeplessNights_return_0_for_startSleepOnMidNight() {

        sleepingSessions = List.of(

                session("2025-10-01T23:59", "2025-10-02T07:00", SleepQuality.GOOD),
                session("2025-10-03T00:00", "2025-10-03T07:00", SleepQuality.BAD),
                session("2025-10-04T00:01", "2025-10-04T07:00", SleepQuality.BAD)
        );

        assertEquals(0L, new NumberOfSleeplessNights().apply(sleepingSessions).getValue());
    }

    @Test
    void numberOfSleeplessNights_return_0_for_endSleepAfterMorning() {

        sleepingSessions = List.of(

                session("2025-10-01T01:00", "2025-10-01T05:59", SleepQuality.GOOD),
                session("2025-10-02T01:00", "2025-10-02T06:00", SleepQuality.BAD),
                session("2025-10-03T01:00", "2025-10-03T06:01", SleepQuality.BAD)
        );

        assertEquals(0L, new NumberOfSleeplessNights().apply(sleepingSessions).getValue());
    }

    @Test
    void numberOfSleeplessNights_return_2_for_StartSleepAfterMorning() {

        sleepingSessions = List.of(

                session("2025-10-01T05:59", "2025-10-01T10:00", SleepQuality.GOOD),
                session("2025-10-02T06:00", "2025-10-02T10:00", SleepQuality.BAD),
                session("2025-10-03T06:01", "2025-10-03T10:00", SleepQuality.BAD)
        );

        assertEquals(2L, new NumberOfSleeplessNights().apply(sleepingSessions).getValue());
    }

    @Test
    void numberOfSleeplessNights_return_10_with_SleeplessAllDays() {

        sleepingSessions = List.of(

                session("2025-10-01T12:00", "2025-10-01T15:00", SleepQuality.GOOD),
                session("2025-10-10T12:00", "2025-10-10T15:00", SleepQuality.BAD)
        );

        assertEquals(10L, new NumberOfSleeplessNights().apply(sleepingSessions).getValue());
    }

    @Test
    void numberOfSleeplessNights_return_0_with_EmptyList() {

        sleepingSessions = List.of();

        assertEquals(0L, new NumberOfSleeplessNights().apply(sleepingSessions).getValue());
    }

    @Test
    void userChronotypeClassification_return_OWL() {
        List<SleepingSession> sleepingSession1;
        List<SleepingSession> sleepingSession2;
        List<SleepingSession> sleepingSession3;

        sleepingSession1 = List.of(session("2025-10-01T23:30", "2025-10-02T09:30", SleepQuality.GOOD));
        sleepingSession2 = List.of(session("2025-10-01T23:00", "2025-10-02T09:00", SleepQuality.GOOD));
        sleepingSession3 = List.of(
                session("2025-10-01T21:00", "2025-10-02T06:30", SleepQuality.GOOD),
                session("2025-10-01T22:30", "2025-10-02T07:00", SleepQuality.GOOD),
                session("2025-10-01T23:30", "2025-10-02T10:00", SleepQuality.GOOD),
                session("2025-10-02T00:00", "2025-10-02T10:00", SleepQuality.GOOD)
        );

        assertEquals(UserChronotype.OWL, new UserChronotypeClassification().apply(sleepingSession1).getValue());
        assertEquals(UserChronotype.OWL, new UserChronotypeClassification().apply(sleepingSession2).getValue());
        assertEquals(UserChronotype.OWL, new UserChronotypeClassification().apply(sleepingSession3).getValue());

    }

    @Test
    void userChronotypeClassification_return_DOVE() {
        List<SleepingSession> sleepingSession1;
        List<SleepingSession> sleepingSession2;
        List<SleepingSession> sleepingSession3;
        List<SleepingSession> sleepingSession4;
        List<SleepingSession> sleepingSession5;

        sleepingSession1 = List.of(session("2025-10-01T22:00", "2025-10-02T09:00", SleepQuality.GOOD));
        sleepingSession2 = List.of(session("2025-10-02T00:00", "2025-10-02T08:00", SleepQuality.GOOD));
        sleepingSession3 = List.of(session("2025-10-01T21:00", "2025-10-02T09:00", SleepQuality.GOOD));
        sleepingSession4 = List.of(session("2025-10-01T21:00", "2025-10-02T07:00", SleepQuality.GOOD));
        sleepingSession5 = List.of(session("2025-10-01T22:00", "2025-10-02T06:00", SleepQuality.GOOD));

        assertEquals(UserChronotype.DOVE, new UserChronotypeClassification().apply(sleepingSession1).getValue());
        assertEquals(UserChronotype.DOVE, new UserChronotypeClassification().apply(sleepingSession2).getValue());
        assertEquals(UserChronotype.DOVE, new UserChronotypeClassification().apply(sleepingSession3).getValue());
        assertEquals(UserChronotype.DOVE, new UserChronotypeClassification().apply(sleepingSession4).getValue());
        assertEquals(UserChronotype.DOVE, new UserChronotypeClassification().apply(sleepingSession5).getValue());

    }

    @Test
    void userChronotypeClassificationWithEqualsCount_return_DOVE() {
        List<SleepingSession> sleepingSession;

        sleepingSession = List.of(
                session("2025-10-01T23:30", "2025-10-02T10:00", SleepQuality.GOOD), //OWL
                session("2025-10-02T00:00", "2025-10-02T10:00", SleepQuality.GOOD), //OWL
                session("2025-10-01T21:30", "2025-10-02T06:30", SleepQuality.GOOD), //LARK
                session("2025-10-01T21:00", "2025-10-02T06:00", SleepQuality.GOOD) //LARK
        );

        assertEquals(UserChronotype.DOVE, new UserChronotypeClassification().apply(sleepingSession).getValue());
    }

    @Test
    void userChronotypeClassification_return_LARK() {
        List<SleepingSession> sleepingSession1;
        List<SleepingSession> sleepingSession2;
        List<SleepingSession> sleepingSession3;

        sleepingSession1 = List.of(session("2025-10-01T21:00", "2025-10-02T06:00", SleepQuality.GOOD));
        sleepingSession2 = List.of(session("2025-10-01T20:00", "2025-10-02T05:00", SleepQuality.GOOD));
        sleepingSession3 = List.of(
                session("2025-10-01T21:00", "2025-10-02T06:00", SleepQuality.GOOD),
                session("2025-10-01T21:30", "2025-10-02T06:30", SleepQuality.GOOD),
                session("2025-10-01T23:30", "2025-10-02T10:00", SleepQuality.GOOD),
                session("2025-10-02T22:00", "2025-10-02T08:00", SleepQuality.GOOD)
        );

        assertEquals(UserChronotype.LARK, new UserChronotypeClassification().apply(sleepingSession1).getValue());
        assertEquals(UserChronotype.LARK, new UserChronotypeClassification().apply(sleepingSession2).getValue());
        assertEquals(UserChronotype.LARK, new UserChronotypeClassification().apply(sleepingSession3).getValue());

    }
}