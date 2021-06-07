package repository;

import com.google.gson.Gson;
import model.ServerError;
import model.TrainerSchedule;
import util.Constants;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.ServerException;
import java.util.stream.Collectors;

public class TrainerScheduleRepository {
    private TrainerSchedule trainerSchedule;

    public TrainerScheduleRepository(int id) throws IOException {
        this.connect(Constants.SERVER + "/trainer_schedule?id=" + id, "GET");
    }

    public TrainerScheduleRepository(TrainerSchedule trainerSchedule, String method) throws IOException {
        StringBuilder urlSBuilder = new StringBuilder(Constants.SERVER + "/trainer_schedule?id=" + trainerSchedule.getTrainer().getId());
        this.trainerSchedule = trainerSchedule;
        urlSBuilder.append(this.trainerSchedule.getSchedule().entrySet().stream().map(o -> "&" + o.getKey().toString().toLowerCase() + "_start=" + o.getValue()[0]
                + "&" + o.getKey().toString().toLowerCase() + "_finish=" + o.getValue()[1]).collect(Collectors.joining()));
        this.connect(urlSBuilder.toString(), method);
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
            try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                this.trainerSchedule = new Gson().fromJson(reader, TrainerSchedule.class);
            }
        }
    }

    public TrainerSchedule getTrainerSchedule() {
        return trainerSchedule;
    }
}
