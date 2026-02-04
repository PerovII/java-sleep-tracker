package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class NumberOfSleeplessNights implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {

    private static final int MORNING_HOUR = 6;
    private static final int AFTERNOON_HOUR = 12;

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {

        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Количество бессонных ночей", 0L);
        }

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

        Set<LocalDate> nightSleeps = sessions.stream()
                .flatMap(s -> {
                    LocalDateTime start = s.getStartSleeping();
                    LocalDateTime end = s.getEndSleeping();

                    if (!end.isAfter(start)) return Stream.empty();

                    LocalDate d1 = start.toLocalDate();
                    LocalDate d2 = end.toLocalDate();

                    return Stream.of(d1, d2)
                            .distinct()
                            .filter(d -> {
                                LocalDateTime windowStart = d.atStartOfDay();
                                LocalDateTime windowEnd = d.atTime(MORNING_HOUR, 0);

                                return start.isBefore(windowEnd) && end.isAfter(windowStart);
                            });
                })
                .collect(Collectors.toSet());

        long totalNights = ChronoUnit.DAYS.between(startRecording, stopRecording) + 1;
        long sleeplessNights = totalNights - nightSleeps.size();

        return new SleepAnalysisResult<>("Количество бессонных ночей", sleeplessNights);
    }
}
