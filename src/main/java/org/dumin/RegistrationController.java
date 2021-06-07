package org.dumin;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import repository.UserRepository;

import java.io.IOException;

/**
 * Контроллер для формы "registration.fxml"
 */
public class RegistrationController {
    public TextField textFieldLogin;
    public TextField textFieldPassword;
    public TextField textFieldName;

    /**
     * Обработка кнопки "Зарегистрироваться"
     * @param actionEvent
     */
    public void signUp(ActionEvent actionEvent) {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        String name = textFieldName.getText();

        if (login.isEmpty() || password.isEmpty() || name.isEmpty()){
            App.showAlert("Ошибка", "Необходимо заполнить все поля");
            return;
        }

        try {
            UserRepository userRepository = new UserRepository(login, password, name);
            App.showAlert("Информация", "Пользователь успешно зарегистрирован!");
            App.setRoot("authorization");
        } catch (IOException e) {
            App.showAlert("Ошибка", e.getMessage());
        }
    }

    /**
     * Обработка нажатия на текст "Нажмите, чтобы войти"
     * @param mouseEvent
     */
    public void goToSignIn(MouseEvent mouseEvent) {
        try {
            App.setRoot("authorization");
        } catch (IOException e) {
            App.showAlert("Ошибка", e.getMessage());
        }
    }
}