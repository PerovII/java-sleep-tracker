package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;



public class NumberOfSleeplessNights implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {

    private static final int MORNING_HOUR = 6;
    private static final int AFTERNOON_HOUR = 12;

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long nightSleeps = sessions
                .stream()
                .filter(s ->
                        (!s.getStartSleeping().toLocalDate().equals(s.getEndSleeping().toLocalDate())) ||
                                (s.getStartSleeping().getHour() < MORNING_HOUR))
                .count();

        LocalDate startRecording = sessions
                .stream()
                .map(s -> {
                            if (s.getStartSleeping().getHour() < AFTERNOON_HOUR) {
                                return s.getStartSleeping().toLocalDate();
                            } else {
                                return s.getStartSleeping().toLocalDate().plusDays(1);
                            }
                        })
                .min(LocalDate::compareTo)
                .orElse(null);

        LocalDate stopRecording = sessions
                .stream()
                .map(s -> {
                    if (s.getEndSleeping().getHour() < AFTERNOON_HOUR) return s.getEndSleeping().toLocalDate();
                    else return s.getEndSleeping().toLocalDate().plusDays(1);
                })
                .max(LocalDate::compareTo)
                .orElse(null);

        long totalNights = ChronoUnit.DAYS.between(startRecording, stopRecording) + 1;
        long sleplessNigths = totalNights - nightSleeps;

        return new SleepAnalysisResult<>("Количество бессонных ночей", sleplessNigths);
    }
}
