package org.dumin;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Apprentice;
import model.EntityEnum;
import model.Training;
import repository.ApprenticeRepository;
import repository.TrainingRepository;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Контроллер для формы "apprentice.fxml"
 */
public class ApprenticeInfoController {
    public TextField textFieldSurName;
    public TextField textFieldName;
    public TextField textFieldPatronymic;
    public TextField textFieldPhoneNumber;
    public ListView listViewTraining;
    private Apprentice apprentice;
    private ArrayList<Training> trainings;

    /**
     * инициализация выбранного ученика
     *
     * @param id
     */
    public void setApprentice(int id) {
        try {
            this.apprentice = new ApprenticeRepository(id, "GET").getApprentice();
            this.textFieldSurName.setText(this.apprentice.getSurname());
            this.textFieldName.setText(this.apprentice.getName());
            this.textFieldPatronymic.setText(this.apprentice.getPatronymic());
            this.textFieldPhoneNumber.setText(this.apprentice.getPhoneNumber());
            setTrainings();
            if (this.trainings != null)
                this.listViewTraining.setItems(FXCollections.observableList(this.trainings));
        } catch (IOException e) {
            App.showAlert("Ошибка", e.getMessage());
        }
    }

    /**
     * Инициализация тренировок для выбранного ученика
     */
    private void setTrainings() {
        try {
            this.trainings = new TrainingRepository(this.apprentice.getId(), EntityEnum.APPRENTICE, "GET").getTrainings();
            this.apprentice = new ApprenticeRepository(this.apprentice.getId(), "GET").getApprentice();
        } catch (IOException e) {
            this.trainings = null;
        }
    }

    /**
     * Обработка кнопки "Обновить ученика"
     *
     * @param actionEvent
     */
    public void update(ActionEvent actionEvent) {
        String phoneNumber = this.textFieldPhoneNumber.getText().trim();
        if (phoneNumber.isEmpty()) {
            App.showAlert("Ошибка", "Необходимо заполнить поле Номер телефона!!!");
            return;
        }
        this.apprentice.setPhoneNumber(phoneNumber);
        try {
            new ApprenticeRepository(this.apprentice);
            App.showAlert("Информация", "Ученик обновлен!");
        } catch (IOException e) {
            App.showAlert("Ошибка", e.getMessage());
        }

    }

    /**
     * Обработка кнопки "Удалить ученика"
     *
     * @param actionEvent
     */
    public void delete(ActionEvent actionEvent) {
        try {
            new ApprenticeRepository(this.apprentice.getId(), "DELETE");
            App.showAlert("Информация", "Ученик удалён!");
            Stage stage = (Stage) this.textFieldName.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            App.showAlert("Ошибка", e.getMessage());
        }
    }

    /**
     * Обработка кнопки "Добавить тренировку"
     *
     * @param actionEvent
     */
    public void addTraining(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addTraining.fxml"));
        try {
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load(), 600, 200);
            stage.setScene(scene);
            AddTrainingController addTrainingController = loader.getController();
            addTrainingController.setApprentice(this.apprentice);
            stage.showAndWait();
            setTrainings();
            this.listViewTraining.setItems(FXCollections.observableList(new TrainingRepository(this.apprentice.getId(), EntityEnum.APPRENTICE, "GET").getTrainings()));
        } catch (IOException e) {
            App.showAlert("Ошибка", e.getMessage());
        }
    }

    /**
     * Обработка кнопки "Удалить тренировку"
     *
     * @param actionEvent
     */
    public void deleteTraining(ActionEvent actionEvent) {
        Training training = (Training) this.listViewTraining.getSelectionModel().getSelectedItem();
        if (training != null) {
            try {
                new TrainingRepository(training.getId(), EntityEnum.TRAINING, "DELETE");
                App.showAlert("Информация", "Тренировка удалена!");
                setTrainings();
                this.listViewTraining.setItems(FXCollections.observableList(this.trainings));
            } catch (IOException e) {
                App.showAlert("Ошибка", e.getMessage());
            }
        }
    }
}
