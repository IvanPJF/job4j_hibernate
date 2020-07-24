package ru.job4j.exam.model;

import javax.persistence.*;

@Entity
public class Entry {

    @EmbeddedId
    private EntryId id = new EntryId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "meetingId")
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "userId")
    private User user;

    @Column
    private boolean status;

    public Entry() {
    }

    public Entry(Meeting meeting, User user, boolean status) {
        this.meeting = meeting;
        this.user = user;
        this.status = status;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Entry{"
                + "meeting=" + meeting
                + ", user=" + user
                + ", status=" + status
                + '}';
    }
}
