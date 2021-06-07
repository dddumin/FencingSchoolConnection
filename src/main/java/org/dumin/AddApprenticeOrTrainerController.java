package org.dumin;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.ApprenticeRepository;
import repository.TrainerRepository;

import java.io.IOException;

/**
 * Контроллер для формы "addApprenticeOrTrainer.fxml"
 */
public class AddApprenticeOrTrainerController {
    public TextField textFieldSurName;
    public TextField textFieldName;
    public TextField textFieldPatronymic;
    public TextField textFieldVariableParameter;
    public RadioButton radioBtnTrainer;
    public RadioButton radioBtnApprentice;
    public Label labelVariableParameter;

    /**
     * Обработка кнопки создать
     *
     * @param actionEvent
     */
    public void add(ActionEvent actionEvent) {
        if (!this.radioBtnApprentice.isSelected() && !this.radioBtnTrainer.isSelected()) {
            App.showAlert("Ошибка", "Выберите сущность, которую вы хотите создать!!!");
        } else if (radioBtnApprentice.isSelected()) {
            String surname = this.textFieldSurName.getText().trim();
            String name = this.textFieldName.getText().trim();
            String patronymic = this.textFieldPatronymic.getText().trim();
            String phoneNumber = this.textFieldVariableParameter.getText().trim();

            if (surname.isEmpty() || name.isEmpty() || phoneNumber.isEmpty()) {
                App.showAlert("Ошибка", "Поля Фамилия, Имя, Номер телефона обязательны для заполнения!!!");
                return;
            }
            if (patronymic.isEmpty())
                patronymic = "-";

            try {
                ApprenticeRepository apprenticeRepository = new ApprenticeRepository(surname, name, patronymic, phoneNumber);
                App.showAlert("Информация", "Пользователь создан!!!");
                Stage stage = (Stage) this.textFieldVariableParameter.getScene().getWindow();
                stage.close();
            } catch (IOException e) {
                App.showAlert("Ошибка", e.getMessage());
            }
        } else if (this.radioBtnTrainer.isSelected()) {
            String surname = this.textFieldSurName.getText().trim();
            String name = this.textFieldName.getText().trim();
            String patronymic = this.textFieldPatronymic.getText().trim();
            String experienceString = this.textFieldVariableParameter.getText().trim();

            if (surname.isEmpty() || name.isEmpty() || experienceString.isEmpty()) {
                App.showAlert("Ошибка", "Поля Фамилия, Имя, Опыт работы обязательны для заполнения!!!");
                return;
            }
            try {
                int experience = Integer.parseInt(experienceString);
                if (experience < 0)
                    throw new NumberFormatException();
                if (patronymic.isEmpty())
                    patronymic = "-";
                TrainerRepository trainerRepository = new TrainerRepository(surname, name, patronymic, experience);
                App.showAlert("Информация", "Пользователь создан!!!");
                Stage stage = (Stage) this.textFieldVariableParameter.getScene().getWindow();
                stage.close();
            } catch (NumberFormatException e) {
                App.showAlert("Ошибка", "Опыт должен быть не отрицательным числом!!!");
            } catch (IOException e) {
                App.showAlert("Ошибка", e.getMessage());
            }
        }
    }

    /**
     * обработка выбора Radio Button "Тренер"
     *
     * @param actionEvent
     */
    public void setFormAddTrainer(ActionEvent actionEvent) {
        this.radioBtnApprentice.setSelected(false);
        this.labelVariableParameter.setText("Опыт работы");
    }

    /**
     * обработка выбора Radio Button "Ученик"
     *
     * @param actionEvent
     */
    public void setFormAddApprentice(ActionEvent actionEvent) {
        this.radioBtnTrainer.setSelected(false);
        this.labelVariableParameter.setText("Номер телефона");
    }
}
