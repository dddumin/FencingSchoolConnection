package model;

import annatations.Exclude;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.stream.Collectors;

public class TrainerSchedule {

    private HashMap<DayOfWeek, Time[]> schedule;
    @Exclude
    private Trainer trainer;

    /**
     * Внутренний класс служащий для отображения расписания в таблице на форме TrainerInfo.fxml
     */
    public static class TrainerScheduleItem {
        private StringProperty dayOfWeek;
        private StringProperty timeStart;
        private StringProperty timeFinish;

        public TrainerScheduleItem(String dayOfWeek, String timeStart, String timeFinish) {
            this.setDayOfWeek(dayOfWeek);
            this.setTimeStart(timeStart);
            this.setTimeFinish(timeFinish);
        }

        public void setDayOfWeek(String dayOfWeek) {
            dayOfWeekProperty().set(dayOfWeek);
        }

        public String getDayOfWeek() {
            return dayOfWeekProperty().get();
        }

        public StringProperty dayOfWeekProperty() {
            if (this.dayOfWeek == null) this.dayOfWeek = new SimpleStringProperty();
            return this.dayOfWeek;
        }

        public void setTimeStart(String timeStart) {
            timeStartProperty().set(timeStart);
        }

        public String getTimeStart() {
            return timeStart.get();
        }

        public StringProperty timeStartProperty() {
            if (this.timeStart == null) this.timeStart = new SimpleStringProperty();
            return this.timeStart;
        }

        public void setTimeFinish(String timeFinish) {
            timeFinishProperty().set(timeFinish);
        }

        public String getTimeFinish() {
            return timeFinish.get();
        }

        public StringProperty timeFinishProperty() {
            if (this.timeFinish == null) this.timeFinish = new SimpleStringProperty();
            return this.timeFinish;
        }
    }

    public TrainerSchedule() {
        this.schedule = new HashMap<>();
    }

    public void setWorkDay(DayOfWeek dayOfWeek, Time start, Time finish) {
        if (start != null) {
            if (finish == null)
                finish = new Time(759600000);
            this.schedule.put(dayOfWeek, new Time[]{start, finish});
        }
    }

    public void deleteWorkDay(DayOfWeek dayOfWeek) {
        this.schedule.remove(dayOfWeek);
    }

    public HashMap<DayOfWeek, Time[]> getSchedule() {
        return new HashMap<>(this.schedule);
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    @Override
    public String toString() {
        return this.schedule.entrySet().stream().map(o -> o.getKey().toString() + o.getValue()[0] + o.getValue()[1]).collect(Collectors.joining(" "));
    }
}
