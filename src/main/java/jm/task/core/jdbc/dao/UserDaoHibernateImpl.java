package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private static final Logger log = Logger.getLogger(UserDaoHibernateImpl.class.getName());

    @Override
    public void createUsersTable() {
        String sql = """
                create table public.users
                (
                    id       serial
                        constraint users_pk
                            primary key,
                    name     varchar not null,
                    lastname varchar not null,
                    age      integer not null
                );""";
        try(Session session = Util.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.createSQLQuery(sql).executeUpdate();
            session.getTransaction().commit();
            log.info("Таблица создана.");
        }catch (Exception e) {
            log.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "drop table public.users;";
        try(Session session = Util.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.createSQLQuery(sql).executeUpdate();
            session.getTransaction().commit();
            log.info("Таблица удалена");
        }catch (Exception e) {
            log.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try(Session session = Util.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
            log.log(Level.INFO, () -> "User с именем -  " + name + " добавлен в базу данных.");
        }catch (Exception e) {
            log.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void removeUserById(long id) {
        try(Session session = Util.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
            log.info("User с именем -  " + user.getName() + " удален из базы даных.");
        }catch (Exception e) {
            log.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public List<User> getAllUsers() {
        try(Session session = Util.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }catch (Exception e) {
            log.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
        return new ArrayList<>();
    }

    @Override
    public void cleanUsersTable() {
        try(Session session = Util.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        }catch (Exception e) {
            log.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }
}
