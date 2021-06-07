package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Apprentice;
import model.ServerError;
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

public class ApprenticeRepository {
    private Apprentice apprentice;
    private ArrayList<Apprentice> apprentices;

    public ApprenticeRepository() throws IOException {
        this.connect(Constants.SERVER + "/apprentice", "GET");
    }

    public ApprenticeRepository(int id, String method) throws IOException {
        this.connect(Constants.SERVER + "/apprentice?id=" + id, method);
    }

    public ApprenticeRepository(String surname, String name, String patronymic, String phoneNumber) throws IOException {
        this.connect(Constants.SERVER + "/apprentice?surname=" + URLEncoder.encode(surname, StandardCharsets.UTF_8) + "&name="
                + URLEncoder.encode(name, StandardCharsets.UTF_8) + "&patronymic=" + URLEncoder.encode(patronymic, StandardCharsets.UTF_8)
                + "&phone_number=" + URLEncoder.encode(phoneNumber, StandardCharsets.UTF_8), "POST");
    }

    public ApprenticeRepository(Apprentice apprentice) throws IOException {
        this.connect(Constants.SERVER + "/apprentice?id=" + apprentice.getId()
                + "&surname=" + URLEncoder.encode(apprentice.getSurname(), StandardCharsets.UTF_8)
                + "&name=" + URLEncoder.encode(apprentice.getName(), StandardCharsets.UTF_8)
                + "&patronymic=" + URLEncoder.encode(apprentice.getPatronymic(), StandardCharsets.UTF_8)
                + "&phone_number=" + URLEncoder.encode(apprentice.getPhoneNumber(), StandardCharsets.UTF_8), "PUT");
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
            if (urlS.endsWith("/apprentice")) {
                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    this.apprentices = new GsonBuilder().addDeserializationExclusionStrategy(new GsonExclusionStrategy()).create().fromJson(reader, new TypeToken<ArrayList<Apprentice>>() {
                    }.getType());
                }
            } else {
                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    this.apprentice = new GsonBuilder().addDeserializationExclusionStrategy(new GsonExclusionStrategy()).create().fromJson(reader, Apprentice.class);
                }
            }
        }
    }

    public ArrayList<Apprentice> getApprentices() {
        return apprentices;
    }

    public Apprentice getApprentice() {
        return apprentice;
    }
}
