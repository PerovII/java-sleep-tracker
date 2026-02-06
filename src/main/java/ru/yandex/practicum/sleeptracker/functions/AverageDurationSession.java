package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class AverageDurationSession implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {

@Override
public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
    long average = (long) sessions.stream()
            .mapToLong(s -> Duration.between(s.getStartSleeping(), s.getEndSleeping()).toMinutes())
            .average()
            .orElse(0);
    return new SleepAnalysisResult<>("Средняя продолжительность сна (минут)", average);

}

}
