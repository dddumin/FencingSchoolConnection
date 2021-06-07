package org.dumin;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Apprentice;
import model.Trainer;
import model.User;
import repository.ApprenticeRepository;
import repository.TrainerRepository;
import repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Контроллер для формы "main.fxml"
 */
public class MainController {
    public ListView listViewApprentice;
    public Label labelHello;
    public ListView listViewTrainer;
    private User user;

    /**
     * инициализация авторизованного пользователя
     *
     * @param user
     */
    public void setUser(User user) {
        this.listViewTrainer.getScene().getWindow().setWidth(1100);
        this.user = user;
        this.labelHello.setText("Добро пожаловать, " + this.user.getName());
        try {
            ArrayList<Apprentice> apprentices = new ApprenticeRepository().getApprentices();
            this.listViewApprentice.setItems(FXCollections.observableList(new ApprenticeRepository().getApprentices()));
            this.listViewTrainer.setItems(FXCollections.observableList(new TrainerRepository().getTrainers()));
            this.listViewApprentice.setOnMouseClicked(mouseEvent -> {
                if (this.listViewApprentice.getSelectionModel().getSelectedItem() != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("apprenticeInfo.fxml"));
                    try {
                        Stage stage = new Stage();
                        stage.setScene(new Scene(loader.load()));
                        ApprenticeInfoController apprenticeInfoController = loader.getController();
                        apprenticeInfoController.setApprentice(((Apprentice) this.listViewApprentice.getSelectionModel().getSelectedItem()).getId());
                        this.listViewApprentice.getSelectionModel().clearSelection();
                        stage.showAndWait();
                        this.listViewApprentice.setItems(FXCollections.observableList(new ApprenticeRepository().getApprentices()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            this.listViewTrainer.setOnMouseClicked(mouseEvent -> {
                if (this.listViewTrainer.getSelectionModel().getSelectedItem() != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("trainerInfo.fxml"));
                    try {
                        Stage stage = new Stage();
                        stage.setScene(new Scene(loader.load()));
                        TrainerInfoController trainerInfoController = loader.getController();
                        trainerInfoController.setTrainer((Trainer) this.listViewTrainer.getSelectionModel().getSelectedItem());
                        this.listViewTrainer.getSelectionModel().clearSelection();
                        stage.showAndWait();
                        this.listViewTrainer.setItems(FXCollections.observableList(new TrainerRepository().getTrainers()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обработка кнопки "Удалить пользователя"
     *
     * @param actionEvent
     */
    public void deleteUser(ActionEvent actionEvent) {
        try {
            new UserRepository(this.user.getId(), "DELETE");
            App.showAlert("Информация", "Пользователь успешно удален!");
            App.setRoot("authorization");
        } catch (IOException e) {
            App.showAlert("Ошибка", e.getMessage());
        }
    }

    /**
     * Обработка кнопки "Добавить"
     *
     * @param actionEvent
     */
    public void addApprenticeOrTrainer(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addApprenticeOrTrainer.fxml"));
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
            this.listViewApprentice.setItems(FXCollections.observableList(new ApprenticeRepository().getApprentices()));
            this.listViewTrainer.setItems(FXCollections.observableList(new TrainerRepository().getTrainers()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit(ActionEvent actionEvent) {
        try {
            App.setRoot("authorization");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
