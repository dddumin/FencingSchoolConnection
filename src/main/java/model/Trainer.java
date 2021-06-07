package model;

import repository.TrainingRepository;
import util.Constants;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Данный класс представляет Тренера
 */
public class Trainer {
    private int id;
    private String surname;
    private String name;
    private String patronymic;
    private int experience;
    private TrainerSchedule trainerSchedule;

    public TrainerSchedule getTrainerSchedule() {
        return trainerSchedule;
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

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * Сформировать список времени доступных тренеровок для тренера
     *
     * @return - список тренировок
     */
    private ArrayList<Training> getTrainingsByTrainer() {
        try {
            TrainingRepository trainingRepository = new TrainingRepository(this.id, EntityEnum.TRAINER, "GET");
            return trainingRepository.getTrainings();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Свободное время для даты
     *
     * @param date - дата
     * @return список объектов Time, в которые тренер может принять ученика
     */
    public List<Time> getFreeTrainingTimeForDay(LocalDate date) {
        Time[] times = this.trainerSchedule.getSchedule().get(date.getDayOfWeek());
        List<Time> timesTraining = Constants.getTimesTraining(times[0], times[1]);
        List<Time> notFreeTimes = this.getTrainingsByTrainer().stream()
                .filter(o -> o.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date))
                .collect(Collectors.groupingBy(o -> Time.valueOf(o.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()), Collectors.counting()))
                .entrySet()
                .stream().filter(o -> o.getValue() == 3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return timesTraining.stream().filter(o -> !notFreeTimes.contains(o)).collect(Collectors.toList());
    }


    /**
     * Возвращает номер зала если у тренера уже есть тренировка в назначенное время
     *
     * @param date - дата тренировки
     * @param time - время тренировки
     * @return - номер зала или null
     */
    public Integer getNumberGymByDateTime(LocalDate date, LocalTime time) {
        return this.getTrainingsByTrainer().stream()
                .filter(o -> o.getDate().equals(Date.from(LocalDateTime.of(date, time).atZone(ZoneId.systemDefault()).toInstant())))
                .map(Training::getNumberGym)
                .findFirst().orElse(null);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return id == trainer.id &&
                experience == trainer.experience &&
                Objects.equals(surname, trainer.surname) &&
                Objects.equals(name, trainer.name) &&
                Objects.equals(patronymic, trainer.patronymic) &&
                Objects.equals(trainerSchedule, trainer.trainerSchedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, name, patronymic, experience, trainerSchedule);
    }

    @Override
    public String toString() {
        return "Фамилия: " + surname
                + ", Имя: " + name
                + ", Отчество: " + patronymic
                + ", Опыт в годах: " + experience;
    }
}
