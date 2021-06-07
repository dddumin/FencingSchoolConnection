package model;

import annatations.Exclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Данный класс представляет Ученика
 */
public class Apprentice {
    private int id;
    private String surname;
    private String name;
    private String patronymic;
    private String phoneNumber;
    private ArrayList<Training> trainings;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<Training> getTrainings() {
        return trainings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apprentice that = (Apprentice) o;
        return id == that.id &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(name, that.name) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(trainings, that.trainings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, name, patronymic, phoneNumber, trainings);
    }

    @Override
    public String toString() {
        return "Фамилия: " + surname
                + ", Имя: " + name
                + ", Отчество: " + patronymic
                + ", Номер телефона: " + phoneNumber;
    }
}
