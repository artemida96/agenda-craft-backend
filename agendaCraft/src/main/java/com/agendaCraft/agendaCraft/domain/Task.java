package com.agendaCraft.agendaCraft.domain;


import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name="task")
public class Task {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="due_date")
    private Date dueDate;

    @Column(name="date_created")
    private Date dateCreated;

    @Column(name = "user_id")
    private Integer userId;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getPerson_id() {
        return userId;
    }

    public void setUserId (Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Task: " + name + " due at: " + dueDate.toString() +
                " created at: " + dateCreated.toString();
    }
}