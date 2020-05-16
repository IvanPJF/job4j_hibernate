package ru.job4j.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.job4j.model.HibernateRollbackUtil.executeAndRollback;

public class DriverTest {

    @Test
    public void whenSaveDriverThenDriver() {
        Driver driver = new Driver();
        Driver result = executeAndRollback(session -> {
            session.save(driver);
            return session.get(Driver.class, driver.getId());
        });
        Driver expected = new Driver();
        expected.setId(driver.getId());
        assertThat(result, is(expected));
    }

    @Test
    public void whenEditDriverThenDriverEdited() {
        Driver driver = new Driver("First");
        Driver result = executeAndRollback(session -> {
            session.save(driver);
            driver.setName("Second");
            return session.get(Driver.class, driver.getId());
        });
        Driver expected = new Driver("Second");
        expected.setId(driver.getId());
        assertThat(result, is(expected));
    }

    @Test
    public void whenDeleteDriverThenNotDriver() {
        Driver driver = new Driver();
        Driver result = executeAndRollback(session -> {
            session.save(driver);
            session.delete(driver);
            return session.get(Driver.class, driver.getId());
        });
        assertThat(result, is((Driver) null));
    }
}