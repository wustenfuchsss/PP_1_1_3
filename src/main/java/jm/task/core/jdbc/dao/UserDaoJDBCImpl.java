package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public void createUsersTable() {
        String sql = """
                create table public.users
                (
                    id       integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    name     varchar not null,
                    lastname varchar not null,
                    age      integer not null
                );""";
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sql);
            logger.log(Level.INFO, "Таблица создана");
        } catch (SQLException e) {
            logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute("drop table public.users;");
            logger.log(Level.INFO, "Таблица удалена");
        } catch (SQLException e) {
            logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?);";
        try (Connection connection = Util.getConnection(); PreparedStatement prStatement = connection.prepareStatement(sql)) {
            prStatement.setString(1, name);
            prStatement.setString(2, lastName);
            prStatement.setInt(3, age);
            prStatement.execute();
            logger.log(Level.INFO, () -> "User с именем -  " + name + " добавлен в базу данных.");
        } catch (SQLException e) {
            logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection(); PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?;")) {
            statement.setLong(1, id);
            statement.execute();
            logger.log(Level.INFO, "Пользователь удален.");
        } catch (SQLException e) {
            logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                User user = new User(resultSet.getString(2), resultSet.getString(3), resultSet.getByte(4));
                user.setId(resultSet.getLong(1));
                users.add(user);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
        return users;
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE users;";
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sql);
            logger.log(Level.INFO, "Таблица очищена.");
        } catch (SQLException e) {
            logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }
}
