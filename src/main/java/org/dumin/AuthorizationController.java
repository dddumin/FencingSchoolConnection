package org.dumin;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import repository.UserRepository;

import java.io.IOException;

/**
 * Контроллер для формы "authorization.fxml"
 */
public class AuthorizationController {
    public TextField textFieldLogin;
    public TextField textFieldPassword;

    /**
     * Обработка нажатия на текст "Нажмите, чтобы зарегистрироваться"
     * @param mouseEvent
     */
    public void goToSignUpForm(MouseEvent mouseEvent) {
        try {
            App.setRoot("registration");
        } catch (IOException e) {
            App.showAlert("Ошибка", e.getMessage());
        }
    }

    /**
     * Обработка кнопки "Войти"
     * @param actionEvent
     */
    public void signIn(ActionEvent actionEvent) {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();

        if (login.isEmpty() || password.isEmpty()){
            App.showAlert("Ошибка", "Необходимо заполнить все поля");
            return;
        }

        try {
            UserRepository userRepository = new UserRepository(login, password);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            App.setRoot(loader);
            MainController mainController = loader.getController();
            mainController.setUser(userRepository.getUser());
        } catch (IOException e){
            App.showAlert("Ошибка", e.getMessage());
        }
    }
}
