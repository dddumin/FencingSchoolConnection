package org.dumin;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.TrainerSchedule;
import repository.TrainerScheduleRepository;
import util.Constants;

import java.io.IOException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контроллер для формы "addTrainerScheduleItemController.fxml"
 */
public class AddTrainerScheduleItemController {
    public ComboBox<String> comboBoxDayOfTheWeek;
    public ComboBox<Time> comboBoxStartTime;
    public ComboBox<Time> comboBoxEndTime;
    private TrainerSchedule trainerSchedule;

    /**
     * инициализация TrainerSchedule
     *
     * @param trainerSchedule
     */
    public void setTrainerSchedule(TrainerSchedule trainerSchedule) {
        this.trainerSchedule = trainerSchedule;
        this.comboBoxDayOfTheWeek.setItems(FXCollections.observableList(this.getStringDay()));
        ArrayList<Time> timesTraining = Constants.getTimesTraining();
        this.comboBoxStartTime.setItems(FXCollections.observableList(timesTraining));
        ArrayList<Time> times = new ArrayList<>(timesTraining);
        Time remove = times.remove(0);
        LocalTime localTime = remove.toLocalTime().plusHours(15);
        times.add(Time.valueOf(localTime));
        this.comboBoxEndTime.setItems(FXCollections.observableList(times));
    }

    /**
     * Определяет дни в которых ещё нет записей для ComboBox
     *
     * @return
     */
    private List<String> getStringDay() {
        Map<DayOfWeek, String> mapDay = Constants.MAP_DAY;
        return Constants.DAY_OF_WEEKS.stream().filter(o -> !this.trainerSchedule.getSchedule().containsKey(o)).map(mapDay::get).collect(Collectors.toList());
    }

    /**
     * Обработка кнорки "Добавить"
     *
     * @param actionEvent
     */
    public void addTrainingScheduleItem(ActionEvent actionEvent) {
        DayOfWeek dayOfWeek = Constants.MAP_DAY_NAMES.get(this.comboBoxDayOfTheWeek.getSelectionModel().getSelectedItem());
        Time timeStart = this.comboBoxStartTime.getSelectionModel().getSelectedItem();
        Time timeEnd = this.comboBoxEndTime.getSelectionModel().getSelectedItem();
        if (dayOfWeek == null || timeStart == null || timeEnd == null) {
            App.showAlert("Ошибка", "Необходимо заполнить все поля!");
            return;
        } else if (timeStart.after(timeEnd) || timeStart.equals(timeEnd)) {
            App.showAlert("Ошибка", "Время начала работы должно быть раньше окончания!");
            return;
        }
        try {
            this.trainerSchedule.setWorkDay(dayOfWeek, timeStart, timeEnd);
            if (this.trainerSchedule.getSchedule().size() != 1)
                new TrainerScheduleRepository(this.trainerSchedule, "PUT");
            else
                new TrainerScheduleRepository(this.trainerSchedule, "POST");

            App.showAlert("Информация", "Расписание обновлено");
            Stage stage = (Stage) this.comboBoxDayOfTheWeek.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            App.showAlert("Ошибка", e.getMessage());
            e.printStackTrace();
        }
    }
}
