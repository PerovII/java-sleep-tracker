package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;
import ru.yandex.practicum.sleeptracker.UserChronotype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserChronotypeClassification implements Function<List<SleepingSession>, SleepAnalysisResult<String>> {

    private static final int MORNING_HOUR = 6;
    private static final int OWL_START_SLEEPING_HOUR = 23;
    private static final int OWL_END_SLEEPING_HOUR = 9;
    private static final int LARK_START_SLEEPING_HOUR = 22;
    private static final int LARK_END_SLEEPING_HOUR = 7;

    @Override
    public SleepAnalysisResult<String> apply(List<SleepingSession> sessions) {
        Map<UserChronotype, Integer> chronotypes = (HashMap<UserChronotype, Integer>) sessions
                .stream()
                .filter(s ->
                    (!s.getStartSleeping().toLocalDate().equals(s.getEndSleeping().toLocalDate())) ||
                            (s.getStartSleeping().getHour() < MORNING_HOUR)
                )
                .map(s -> {
                    if ((s.getStartSleeping().getHour() >= OWL_START_SLEEPING_HOUR
                            || s.getStartSleeping().getHour() < MORNING_HOUR)
                            && s.getEndSleeping().getHour() >= OWL_END_SLEEPING_HOUR)
                        return UserChronotype.OWL;
                    if (s.getStartSleeping().getHour() < LARK_START_SLEEPING_HOUR
                            && s.getEndSleeping().getHour() < LARK_END_SLEEPING_HOUR)
                        return UserChronotype.LARK;
                    return UserChronotype.DOVE;
                })
                .collect(Collectors.toMap(
                        c -> c,
                        c -> 1,
                        Integer::sum
                ));

        int owl = chronotypes.getOrDefault(UserChronotype.OWL, 0);
        int lark = chronotypes.getOrDefault(UserChronotype.LARK, 0);
        int dove = chronotypes.getOrDefault(UserChronotype.DOVE, 0);

        String chronotype;
        if (owl > lark && owl > dove) chronotype = "сова";
        else if (lark > owl && lark > dove) chronotype = "жаворонок";
        else chronotype = "голубь";

        return new SleepAnalysisResult<>("Ваш хронотип", chronotype);

    }
}
