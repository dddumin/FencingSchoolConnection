package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.EntityEnum;
import model.ServerError;
import model.Training;
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

public class TrainingRepository {
    private Training training;
    private ArrayList<Training> trainings;

    public TrainingRepository(Training training) throws IOException {
        this.training = training;
        this.connect(Constants.SERVER + "/training?date=" + URLEncoder.encode(this.training.getDate().toString(), StandardCharsets.UTF_8)
                + "&number_gym=" + this.training.getNumberGym()
                + "&id_trainer=" + this.training.getTrainer().getId()
                + "&id_apprentice="
                + this.training.getApprentice().getId(), "POST");
    }

    public TrainingRepository(int id, EntityEnum entityEnum, String method) throws IOException {
        String req = "";
        switch (entityEnum) {
            case APPRENTICE:
                req = "id_apprentice=";
                break;
            case TRAINER:
                req = "id_trainer=";
                break;
            case TRAINING:
                req = "id=";
                break;
        }
        this.connect(Constants.SERVER + "/training?" + req + id, method);
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
            if (urlS.endsWith("/training") || ((urlS.contains("id_trainer") || urlS.contains("id_apprentice")) && !urlS.contains("number_gym"))) {
                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    this.trainings = new GsonBuilder().addDeserializationExclusionStrategy(new GsonExclusionStrategy()).create().fromJson(reader, new TypeToken<ArrayList<Training>>() {
                    }.getType());
                }
            } else {
                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    this.training = new GsonBuilder().addDeserializationExclusionStrategy(new GsonExclusionStrategy()).create().fromJson(reader, Training.class);
                }
            }
        }
    }

    public Training getTraining() {
        return training;
    }

    public ArrayList<Training> getTrainings() {
        return trainings;
    }

}
