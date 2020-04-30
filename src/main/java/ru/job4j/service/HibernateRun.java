package ru.job4j.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.User;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class HibernateRun {

    private static SessionFactory getSessionFactory() throws Exception {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            return new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new Exception("Trouble building the SessionFactory");
        }
    }

    public static void main(String[] args) {
        try (final SessionFactory factory = getSessionFactory();
             final Session session = factory.openSession()) {
            session.beginTransaction();
            User user = new User("Name", new GregorianCalendar());
            session.save(user);
            User findUser = session.get(User.class, user.getId());
            System.out.printf("Find create user: %s%n", findUser);
            user.setName("New name");
            user.setExpired(new GregorianCalendar(3020, Calendar.JANUARY, Calendar.SUNDAY));
            session.update(user);
            findUser = session.get(User.class, user.getId());
            System.out.printf("Find update user: %s%n", findUser);
            session.delete(user);
            List<User> users = session.createQuery("from User", User.class).list();
            users.forEach(System.out::println);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
