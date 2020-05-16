package ru.job4j.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.function.Function;

public class HibernateRollbackUtil {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateRollbackUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            return new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }

    public static <T> T executeAndRollback(Function<Session, T> function) {
        try (final Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            T result = function.apply(session);
            session.getTransaction().rollback();
            return result;
        }
    }
}
