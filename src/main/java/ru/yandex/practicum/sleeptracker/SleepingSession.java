package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;

public class SleepingSession {

    private final LocalDateTime startSleeping;
    private final LocalDateTime endSleeping;
    private final SleepQuality sleepQuality;

    public SleepingSession(LocalDateTime startSleeping, LocalDateTime endSleeping, SleepQuality sleepQuality) {
        this.startSleeping = startSleeping;
        this.endSleeping = endSleeping;
        this.sleepQuality = sleepQuality;
    }

    public LocalDateTime getStartSleeping() {
        return startSleeping;
    }

    public LocalDateTime getEndSleeping() {
        return endSleeping;
    }

    public SleepQuality getSleepQuality() {
        return sleepQuality;
    }

}
