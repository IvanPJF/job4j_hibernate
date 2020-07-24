package ru.job4j.exam.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "meeting")
    private Set<Entry> entries = new HashSet<>();

    public Meeting() {
    }

    public Meeting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Entry> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        return "Meeting{"
                + "name='" + name + '\''
                + '}';
    }
}
