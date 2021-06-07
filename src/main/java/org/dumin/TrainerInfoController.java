package org.dumin;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Trainer;
import model.TrainerSchedule;
import repository.TrainerRepository;
import repository.TrainerScheduleRepository;
import util.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контроллер для формы "trainerInfo.fxml"
 */
public class TrainerInfoController {
    public TextField textFieldSurName;
    public TextField textFieldName;
    public TextField textFieldPatronymic;
    public TextField textFieldExperience;
    public TableView<TrainerSchedule.TrainerScheduleItem> tableViewSchedule;
    private Trainer trainer;
    private TrainerSchedule trainerSchedule;

    /**
     * инициализация выбранного тренера
     *
     * @param trainer
     */
    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
        this.textFieldSurName.setText(trainer.getSurname());
        this.textFieldName.setText(trainer.getName());
        this.textFieldPatronymic.setText(trainer.getPatronymic());
        this.textFieldExperience.setText(String.valueOf(trainer.getExperience()));
        this.trainerSchedule = this.trainer.getTrainerSchedule() != null ? this.trainer.getTrainerSchedule() : new TrainerSchedule();
        this.createTable();
    }

    private void createTable() {
        TableColumn<TrainerSchedule.TrainerScheduleItem, String> dayColumn = new TableColumn<>("День недели:");
        TableColumn<TrainerSchedule.TrainerScheduleItem, String> timeStartColumn = new TableColumn<>("Время начала работы:");
        TableColumn<TrainerSchedule.TrainerScheduleItem, String> timeEndColumn = new TableColumn<>("Время окончания работы:");
        this.tableViewSchedule.setItems(FXCollections.observableList(this.getTrainerScheduleItems()));
        this.tableViewSchedule.getColumns().addAll(dayColumn, timeStartColumn, timeEndColumn);
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("dayOfWeek"));
        timeStartColumn.setCellValueFactory(new PropertyValueFactory<>("timeStart"));
        timeEndColumn.setCellValueFactory(new PropertyValueFactory<>("timeFinish"));
    }

    private List<TrainerSchedule.TrainerScheduleItem> getTrainerScheduleItems() {
        return this.trainerSchedule.getSchedule().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(o -> new TrainerSchedule.TrainerScheduleItem(Constants.MAP_DAY.get(o.getKey()), o.getValue()[0].toString(), o.getValue()[1].toString()))
                .collect(Collectors.toList());
    }

    /**
     * Обработка кнопки "Обновить данные тренера"
     *
     * @param actionEvent
     */
    public void updateTrainer(ActionEvent actionEvent) {
        String experienceStr = this.textFieldExperience.getText().trim();
        if (experienceStr.isEmpty()) {
            App.showAlert("Ошибка", "Необходимо заполнить поле Опыт!!!");
            return;
        }
        try {
            int experience = Integer.parseInt(experienceStr);
            if (experience < 0)
                throw new NumberFormatException();
            this.trainer.setExperience(experience);
            TrainerRepository trainerRepository = new TrainerRepository(this.trainer);
            App.showAlert("Информация", "Данные тренера обновлены!!!");
        } catch (NumberFormatException e) {
            App.showAlert("Ошибка", "Опыт должен быть неотрицательным числом!!!");
        } catch (IOException e) {
            App.showAlert("Ошибка", e.getMessage());
        }
    }


    /**
     * Обработка кнопки "Удалить тренера"
     *
     * @param actionEvent
     */
    public void delete(ActionEvent actionEvent) {
        try {
            TrainerRepository trainerRepository = new TrainerRepository(this.trainer.getId(), "DELETE");
            App.showAlert("Информация", "Тренер удален!!!");
            Stage stage = (Stage) this.textFieldExperience.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            App.showAlert("Ошибка", e.getMessage());
        }
    }


    /**
     * Добавить запись в расписание
     *
     * @param actionEvent
     */
    public void addItemSchedule(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addTrainerScheduleItem.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            AddTrainerScheduleItemController addTrainerScheduleItemController = loader.getController();
            if (this.trainerSchedule.getTrainer() == null)
                this.trainerSchedule.setTrainer(this.trainer);
            addTrainerScheduleItemController.setTrainerSchedule(this.trainerSchedule);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
            this.tableViewSchedule.setItems(FXCollections.observableList(this.getTrainerScheduleItems()));
        } catch (IOException e) {
            App.showAlert("Ошибка", e.getMessage());
        }

    }

    /**
     * Удалить запись из расписания
     *
     * @param actionEvent
     */
    public void deleteItemSchedule(ActionEvent actionEvent) {
        if (this.tableViewSchedule.getSelectionModel().getSelectedItem().getDayOfWeek() != null) {
            try {
                if (this.trainerSchedule.getSchedule().size() == 1) {
                    new TrainerScheduleRepository(this.trainerSchedule, "DELETE");
                    this.trainerSchedule.deleteWorkDay(Constants.MAP_DAY_NAMES.get(this.tableViewSchedule.getSelectionModel().getSelectedItem().getDayOfWeek()));
                } else {
                    this.trainerSchedule.deleteWorkDay(Constants.MAP_DAY_NAMES.get(this.tableViewSchedule.getSelectionModel().getSelectedItem().getDayOfWeek()));
                    new TrainerScheduleRepository(this.trainerSchedule, "PUT");
                }
                App.showAlert("Информация", "Запись удалена!");
                this.tableViewSchedule.setItems(FXCollections.observableList(this.getTrainerScheduleItems()));

            } catch (IOException e) {
                App.showAlert("Ошибка", e.getMessage());
            }
        }
    }
}
