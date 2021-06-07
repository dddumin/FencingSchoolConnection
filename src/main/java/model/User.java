package model;

import java.sql.Date;
import java.util.Objects;

/**
 * Данный класс представляет Пользователя
 */
public class User {
    private int id;
    private String login;
    private String password;
    private String name;
    private Date regDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(regDate, user.regDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, name, regDate);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", regDate=" + regDate +
                '}';
    }
}
