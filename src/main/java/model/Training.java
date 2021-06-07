package model;


import annatations.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Данный класс представяет Тренировку
 */
public class Training {
    private int id;
    private int numberGym;
    private Trainer trainer;
    @Exclude
    private Apprentice apprentice;
    private Date date;

    public Training(int numberGym, Trainer trainer, Apprentice apprentice, Date date) {
        this.numberGym = numberGym;
        this.trainer = trainer;
        this.apprentice = apprentice;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberGym() {
        return numberGym;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public Apprentice getApprentice() {
        return apprentice;
    }

    public void setApprentice(Apprentice apprentice) {
        this.apprentice = apprentice;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return id == training.id &&
                numberGym == training.numberGym &&
                Objects.equals(trainer, training.trainer) &&
                Objects.equals(apprentice, training.apprentice) &&
                Objects.equals(date, training.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberGym, trainer, apprentice, date);
    }

    @Override
    public String toString() {
        return "Дата: " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(this.date) + ", Тренер: " + this.trainer.getSurname() + ", Номер зала: " + this.getNumberGym();
    }
}
