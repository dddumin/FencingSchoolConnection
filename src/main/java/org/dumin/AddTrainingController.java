package org.dumin;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Apprentice;
import model.Trainer;
import model.TrainerSchedule;
import model.Training;
import repository.TrainerRepository;
import repository.TrainingRepository;

import java.io.IOException;
import java.sql.Time;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Контроллер для формы "addTraining.fxml"
 */
public class AddTrainingController {
    public ComboBox<Trainer> comboBoxTrainer;
    public DatePicker datePicker;
    public Label labelTime;
    public TextField textFieldNumber;
    public Button btnAdd;
    public ComboBox<Time> comboBoxTime;
    private Apprentice apprentice;

    /**
     * инициализация ученика
     *
     * @param apprentice
     */
    public void setApprentice(Apprentice apprentice) {
        this.apprentice = apprentice;
        try {
            this.comboBoxTrainer.setItems(FXCollections.observableList(new TrainerRepository().getTrainers().stream()
                    .filter(o -> o.getTrainerSchedule() != null).collect(Collectors.toList())));
            this.comboBoxTrainer.setOnAction(action -> {
                if (this.comboBoxTrainer.getSelectionModel().getSelectedItem() != null) {
                    this.datePicker.setValue(null);
                    this.datePicker.setVisible(true);
                    this.datePicker.setDayCellFactory(getDayCellFactory());
                    this.comboBoxTime.getSelectionModel().clearSelection();
                    setDefaultElementSetting();
                }
            });
            this.comboBoxTime.setOnAction(action -> {
                if (this.comboBoxTime.getSelectionModel().getSelectedItem() != null) {
                    LocalDateTime localDateTime = LocalDateTime.of(this.datePicker.getValue(), this.comboBoxTime.getSelectionModel().getSelectedItem().toLocalTime());
                    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    Integer numberGymByDateTime = this.comboBoxTrainer.getSelectionModel().getSelectedItem()
                            .getNumberGymByDateTime(this.datePicker.getValue(), this.comboBoxTime.getSelectionModel().getSelectedItem().toLocalTime());
                    if (numberGymByDateTime != null) {
                        this.textFieldNumber.setText(String.valueOf(numberGymByDateTime));
                        this.textFieldNumber.setEditable(false);
                    } else
                        this.setDefaultElementSetting();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Установка элементов формы по умолчанию
     */
    private void setDefaultElementSetting() {
        this.textFieldNumber.setEditable(true);
        this.comboBoxTime.setVisible(true);
        this.btnAdd.setDisable(false);
        this.textFieldNumber.setText("");
    }

    /**
     * Сделать даты недоступными
     *
     * @return
     */
    private Callback<DatePicker, DateCell> getDayCellFactory() {
        Trainer trainer = this.comboBoxTrainer.getSelectionModel().getSelectedItem();
        TrainerSchedule trainerSchedule = trainer.getTrainerSchedule();
        HashMap<DayOfWeek, Time[]> workDay = trainerSchedule.getSchedule();
        return new Callback<>() {
            @Override
            public DateCell call(final DatePicker datePicker1) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                        if (!workDay.containsKey(item.getDayOfWeek())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                        if (apprentice.getTrainings() != null) {
                            for (Training training : apprentice.getTrainings()) {
                                if (training.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(item)) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        }
                    }
                };
            }
        };
    }

    /**
     * Обработчик выбора даты
     *
     * @param actionEvent
     */
    public void setTime(ActionEvent actionEvent) {
        this.setDefaultElementSetting();
        Trainer trainer = this.comboBoxTrainer.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = this.datePicker.getValue();
        if (selectedDate != null) {

            this.labelTime.setVisible(true);
            this.comboBoxTime.setVisible(true);

            this.comboBoxTime.setItems(FXCollections.observableList(trainer.getFreeTrainingTimeForDay(selectedDate)));
        }
    }

    /**
     * Обработка кнопки "Добавить тренировку"
     *
     * @param actionEvent
     */
    public void addTraining(ActionEvent actionEvent) {
        String numberGym = this.textFieldNumber.getText().trim();
        if (numberGym.isEmpty()) {
            App.showAlert("Ошибка", "Необходимо заполнить все поля!!!");
            return;
        }
        Trainer trainer = this.comboBoxTrainer.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = this.datePicker.getValue();
        LocalTime selectedTime = this.comboBoxTime.getSelectionModel().getSelectedItem().toLocalTime();
        LocalDateTime localDateTime = LocalDateTime.of(selectedDate, selectedTime);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Training training = new Training(Integer.parseInt(numberGym), trainer, this.apprentice, date);
        try {
            new TrainingRepository(training);
            App.showAlert("Информация", "Тренировка добавлена!!!");
            this.apprentice.getTrainings().add(training);
            Stage stage = (Stage) this.textFieldNumber.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            App.showAlert("Ошибка", e.getMessage());
        }
    }
}
