package repository;

import com.google.gson.Gson;
import model.ServerError;
import model.User;
import util.Constants;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerException;

public class UserRepository {
    private User user;


    public UserRepository(String login, String password) throws IOException {
        this.connect(Constants.SERVER + "/user?login=" + URLEncoder.encode(login, StandardCharsets.UTF_8)
                + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8), "POST");
    }

    public UserRepository(String login, String password, String name) throws IOException {
        this.connect(Constants.SERVER + "/registration?login=" + URLEncoder.encode(login, StandardCharsets.UTF_8)
                + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8) + "&name=" + URLEncoder.encode(name, StandardCharsets.UTF_8), "POST");
    }

    public UserRepository(int id, String method) throws IOException {
        this.connect(Constants.SERVER + "/user?id=" + id, method);
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
                this.user = new Gson().fromJson(reader, User.class);
            }
        }
    }

    public User getUser() {
        return user;
    }
}
