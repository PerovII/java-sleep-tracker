package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.functions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Ошибка: не указан путь к файлу.");
            return;
        }

        String filePath = args[0];

        try {
            List<SleepingSession> sleepingSessions = new SleepLogReader().getSleepSessions(filePath);

            List<Function<List<SleepingSession>, ? extends SleepAnalysisResult<?>>> functions = new ArrayList<>();
            functions.add(new NumberOfSleepSessions());
            functions.add(new MinimumDurationSession());
            functions.add(new MaximumDurationSession());
            functions.add(new AverageDurationSession());
            functions.add(new NumberOfBadSessions());
            functions.add(new NumberOfSleeplessNights());
            functions.add(new UserChronotypeClassification());

            functions.stream()
                    .map(f -> f.apply(sleepingSessions))
                    .forEach(r -> {
                                Object value = r.getValue();
                                if (value instanceof UserChronotype chronotype) {
                                    if (chronotype == UserChronotype.OWL) value = "сова";
                                    else if (chronotype == UserChronotype.LARK) value = "жаворонок";
                                    else if (chronotype == UserChronotype.DOVE) value = "голубь";
                                }
                            System.out.println(r.getDescription() + ": " + value);
                    });

        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + filePath);
            System.out.println(e.getMessage());
        }
    }
}
