package com.agendaCraft.agendaCraft.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;


@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name="name")
    private String name;

    @NotNull
    @Column(name="due_date")
    private Date dueDate;


    @JsonIgnore
    @Column(name="date_created")
    private Date dateCreated;

    @JsonIgnore
    @Column(name = "user_id")
    private Long assignedUserId;

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


    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUseId){
        this.assignedUserId = assignedUseId;
    }

    @Override
    public String toString() {
        return "Task: " + name + " due at: " + dueDate.toString() +
                " created at: " + dateCreated.toString();
    }
}