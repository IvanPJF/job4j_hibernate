package ru.job4j.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.exam.model.Entry;
import ru.job4j.exam.model.Meeting;
import ru.job4j.exam.model.User;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MeetingTest {

    private static final SessionFactory SESSION_FACTORY = buildFactory();

    @BeforeClass
    public static void fillDB() throws Exception {
        Transaction tx = null;
        try (final Session session = SESSION_FACTORY.openSession()) {
            tx = session.beginTransaction();
            User uJohn = new User("John");
            User uSarah = new User("Sarah");
            User uArny = new User("Arny");
            Meeting mJug = new Meeting("JUG");
            Meeting mJPoint = new Meeting("JPoint");
            Meeting mDotNet = new Meeting("DotNet");
            session.save(uJohn);
            session.save(uSarah);
            session.save(uArny);
            session.save(mJug);
            session.save(mJPoint);
            session.save(mDotNet);
            Entry eMJugJohn = new Entry(mJug, uJohn, true);
            Entry eMJPointJohn = new Entry(mJPoint, uJohn, true);
            Entry eMJPointSarah = new Entry(mJPoint, uSarah, false);
            Entry eMJugSarah = new Entry(mJug, uSarah, false);
            Entry eMJPointArny = new Entry(mJPoint, uArny, false);
            session.save(eMJugJohn);
            session.save(eMJPointJohn);
            session.save(eMJPointSarah);
            session.save(eMJugSarah);
            session.save(eMJPointArny);
            tx.commit();
        } catch (Exception e) {
            if (Objects.nonNull(tx) && tx.isActive()) {
                tx.rollback();
            }
            throw new Exception(e);
        }
    }

    @Test
    public void whenGetAllEntriesThen5() throws Exception {
        List<Entry> entries = execute(session ->
                session.createQuery(
                        "select e from Entry e join fetch e.meeting join fetch e.user",
                        Entry.class
                ).getResultList());
        assertThat(entries.size(), is(5));
    }

    @Test
    public void whenGetNumberConfirmedUsersThen2() throws Exception {
        long numberTrue = execute(session ->
                session.createQuery(
                        "select count(*) from Entry e group by e.status having e.status = true",
                        Long.class
                ).getSingleResult());
        assertThat(numberTrue, is(2L));
    }

    @Test
    public void whenGetMeetingsWithoutEntriesThen1() throws Exception {
        List<Meeting> meetings = execute(session ->
                session.createQuery(
                        "select m from Meeting m left join fetch m.entries e "
                                + "where e.meeting is null",
                        Meeting.class
                ).getResultList());
        assertThat(meetings.size(), is(1));
    }

    private <T> T execute(Function<Session, T> function) throws Exception {
        Transaction tx = null;
        try (final Session session = SESSION_FACTORY.openSession()) {
            tx = session.beginTransaction();
            T result = function.apply(session);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (Objects.nonNull(tx) && tx.isActive()) {
                tx.rollback();
            }
            throw new Exception(e);
        }
    }

    private static SessionFactory buildFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.test.cfg.xml").build();
        try {
            return new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new ExceptionInInitializerError(e);
        }
    }
}
