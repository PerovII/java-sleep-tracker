package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;
import java.util.List;
import java.util.function.Function;

public class NumberOfBadSassions implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {

@Override
public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
    long count = sessions
            .stream()
            .filter(s -> s.getSleepQuality().equals(SleepQuality.BAD))
            .count();
    return new SleepAnalysisResult<>("Количество сессий с плохим качеством сна", count);

}

}
