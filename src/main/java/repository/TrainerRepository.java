package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.ServerError;
import model.Trainer;
import util.Constants;
import util.GsonExclusionStrategy;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerException;
import java.util.ArrayList;

public class TrainerRepository {
    private Trainer trainer;
    private ArrayList<Trainer> trainers;

    public TrainerRepository() throws IOException {
        this.connect(Constants.SERVER + "/trainer", "GET");
    }

    public TrainerRepository(int id, String method) throws IOException {
        this.connect(Constants.SERVER + "/trainer?id=" + id, method);
    }

    public TrainerRepository(String surname, String name, String patronymic, int experience) throws IOException {
        this.connect(Constants.SERVER + "/trainer?surname=" + URLEncoder.encode(surname, StandardCharsets.UTF_8)
                + "&name=" + URLEncoder.encode(name, StandardCharsets.UTF_8)
                + "&patronymic=" + URLEncoder.encode(patronymic, StandardCharsets.UTF_8)
                + "&experience=" + experience, "POST");
    }

    public TrainerRepository(Trainer trainer) throws IOException {
        this.connect(Constants.SERVER + "/trainer?id=" + trainer.getId()
                + "&surname=" + URLEncoder.encode(trainer.getSurname(), StandardCharsets.UTF_8)
                + "&name=" + URLEncoder.encode(trainer.getName(), StandardCharsets.UTF_8)
                + "&patronymic=" + URLEncoder.encode(trainer.getPatronymic(), StandardCharsets.UTF_8)
                + "&experience=" + trainer.getExperience(), "PUT");
    }


    private void connect(String urlS, String method) throws IOException {
        URL url = new URL(urlS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        if (connection.getResponseCode() == 400 || connection.getResponseCode() == 500) {
            try (InputStreamReader errorStream = new InputStreamReader(connection.getErrorStream())) {
                ServerError serverError = new Gson().fromJson(errorStream, ServerError.class);
                throw new ServerException(serverError.getMessage());
            }
        } else {
            if (urlS.endsWith("/trainer")) {
                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    this.trainers = new GsonBuilder().addDeserializationExclusionStrategy(new GsonExclusionStrategy()).create()
                            .fromJson(reader, new TypeToken<ArrayList<Trainer>>() {
                            }.getType());
                    this.trainers.forEach(o -> {
                        if (o.getTrainerSchedule() != null)
                            o.getTrainerSchedule().setTrainer(o);
                    });
                }
            } else {
                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    this.trainer = new GsonBuilder().addDeserializationExclusionStrategy(new GsonExclusionStrategy()).create().fromJson(reader, Trainer.class);
                }
            }
        }
    }

    public ArrayList<Trainer> getTrainers() {
        return trainers;
    }
}
