package util;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Constants {
    public static final String SERVER = "http://localhost:8080/FencingSchool";
    public static final List<DayOfWeek> DAY_OF_WEEKS = Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    public static final Map<String, DayOfWeek> MAP_DAY_NAMES = Map.of("Понедельник", DayOfWeek.MONDAY, "Вторник", DayOfWeek.TUESDAY, "Среда", DayOfWeek.WEDNESDAY,
            "Четверг", DayOfWeek.THURSDAY, "Пятница", DayOfWeek.FRIDAY, "Суббота", DayOfWeek.SATURDAY, "Воскресение", DayOfWeek.SUNDAY);
    public static final Map<DayOfWeek, String> MAP_DAY = Map.of(DayOfWeek.MONDAY, "Понедельник", DayOfWeek.TUESDAY, "Вторник",
            DayOfWeek.WEDNESDAY, "Среда", DayOfWeek.THURSDAY, "Четверг", DayOfWeek.FRIDAY, "Пятница", DayOfWeek.SATURDAY, "Суббота", DayOfWeek.SUNDAY, "Воскресение");


    public static ArrayList<Time> getTimesTraining() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = LocalTime.parse("08:00", dateTimeFormatter);
        LocalTime endTime = LocalTime.parse("23:00", dateTimeFormatter);
        ArrayList<Time> times = new ArrayList<>();
        while (!time.equals(endTime)) {
            times.add(Time.valueOf(time));
            time = time.plusMinutes(90);
        }
        return times;
    }

    public static List<Time> getTimesTraining(Time timeStart, Time timeFinish) {
        LocalTime time = timeStart.toLocalTime();
        LocalTime endTime = timeFinish.toLocalTime();
        ArrayList<Time> times = new ArrayList<>();
        while (!time.equals(endTime)) {
            times.add(Time.valueOf(time));
            time = time.plusMinutes(90);
        }
        return times;
    }
}
