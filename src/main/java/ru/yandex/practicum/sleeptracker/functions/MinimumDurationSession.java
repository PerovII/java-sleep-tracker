package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MinimumDurationSession implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {

@Override
public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
    long minimum = sessions.stream()
            .mapToLong(s -> Duration.between(s.getStartSleeping(), s.getEndSleeping()).toMinutes())
            .min()
            .orElse(0);
    return new SleepAnalysisResult<>("Минимальная продолжительность сна (минут)", minimum);

}

}
