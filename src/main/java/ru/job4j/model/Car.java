package ru.job4j.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(
            name = "engine_id",
            foreignKey = @ForeignKey(name = "ENGINE_ID_FK"),
            nullable = false,
            updatable = false,
            unique = true
    )
    private Engine engine;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "history_owner",
            joinColumns = @JoinColumn(name = "car_id", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "driver_id", nullable = false, updatable = false)
    )
    private Set<Driver> drivers;

    public Set<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        return Objects.equals(id, car.id)
                && Objects.equals(engine, car.engine)
                && Objects.equals(drivers, car.drivers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, engine, drivers);
    }

    @Override
    public String toString() {
        return "Car{"
                + "id=" + id
                + ", engine=" + engine
                + ", drivers=" + drivers
                + '}';
    }
}
