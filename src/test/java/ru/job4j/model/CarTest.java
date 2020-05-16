package ru.job4j.model;

import org.junit.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.job4j.model.HibernateRollbackUtil.executeAndRollback;

public class CarTest {

    @Test
    public void whenCarSaveThenCar() {
        Car car = new Car();
        Engine engine = new Engine();
        Set<Driver> drivers = Set.of(new Driver());
        car.setEngine(engine);
        car.setDrivers(drivers);
        Car result = executeAndRollback(session -> {
            session.save(engine);
            session.save(car);
            return session.get(Car.class, car.getId());
        });
        Car expected = new Car();
        expected.setId(car.getId());
        expected.setEngine(engine);
        expected.setDrivers(drivers);
        assertThat(result, is(expected));
    }

    @Test
    public void whenCarEditThenCarEdited() {
        Car car = new Car();
        Engine engine = new Engine();
        car.setEngine(engine);
        Engine engineUpdate = new Engine();
        Car result = executeAndRollback(session -> {
            session.save(engine);
            session.save(engineUpdate);
            session.save(car);
            car.setEngine(engineUpdate);
            return session.get(Car.class, car.getId());
        });
        Car expected = new Car();
        expected.setId(car.getId());
        expected.setEngine(engineUpdate);
        assertThat(result, is(expected));
    }

    @Test
    public void whenCarDeleteThenNotCar() {
        Car car = new Car();
        Engine engine = new Engine();
        car.setEngine(engine);
        Car result = executeAndRollback(session -> {
            session.save(engine);
            session.save(car);
            session.delete(car);
            return session.get(Car.class, car.getId());
        });
        assertThat(result, is((Car) null));
    }
}