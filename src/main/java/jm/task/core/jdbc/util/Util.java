package jm.task.core.jdbc.util;
import java.util.Properties;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.*;
import java.util.logging.Logger;

public class Util {
    private Util() {

    }

    private static final String URL = "jdbc:postgresql://localhost:5432/max";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "databasetestforpp";
    private static SessionFactory sessionFactory;
    private static final Logger log = Logger.getLogger(Util.class.getName());

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = getConfiguration();

                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                log.warning("I can`t get SessionFactory." + e);
            }
        }
        return sessionFactory;
    }

    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        Properties settings = new Properties();
        settings.put(AvailableSettings.DRIVER, "org.postgresql.Driver");
        settings.put(AvailableSettings.URL, URL);
        settings.put(AvailableSettings.USER, USERNAME);
        settings.put(AvailableSettings.PASS, PASSWORD);
        settings.put(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");

        settings.put(AvailableSettings.SHOW_SQL, "true");

        settings.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");

        settings.put(AvailableSettings.HBM2DDL_AUTO, "");

        configuration.setProperties(settings);
        return configuration;
    }

    public static void close() {
        sessionFactory.close();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
