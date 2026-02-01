package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.functions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {

    public static void main(String[] args) {

        try {
            List<SleepingSession> sleepingSessions = new SleepLogReader().getSleepSessions("src/main/resources/sleep_log.txt");

            List<Function<List<SleepingSession>, ? extends SleepAnalysisResult<?>>> functions = new ArrayList<>();
            functions.add(new NumberOfSleepSessions());
            functions.add(new MinimumDurationSession());
            functions.add(new MaximumDurationSession());
            functions.add(new AverageDurationSession());
            functions.add(new NumberOfBadSassions());
            functions.add(new NumberOfSleeplessNights());
            functions.add(new UserChronotypeClassification());

            functions.stream()
                    .map(f ->f.apply(sleepingSessions))
                    .forEach(r -> System.out.println(r.getDescription() + ": " + r.getValue()));



        } catch (IOException e) {
            throw new RuntimeException(e);
        }






    }
}