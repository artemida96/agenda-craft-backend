package com.agendaCraft.agendaCraft.domain;


import com.agendaCraft.agendaCraft.deserializer.EnumTaskStatusDeserializer;
import com.agendaCraft.agendaCraft.enums.EnumTaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;


@Entity
@Table(name="tasks")
@Data
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name="name")
    private String title;

    @NotNull
    @Column(name="due_date")
    private Date dueDate;

    @Column(name="date_created")
    private Date dateCreated;

    @JsonIgnore
    @Column(name = "user_id")
    private Long userId;

    @JsonDeserialize(using = EnumTaskStatusDeserializer.class)
    @Column (name= "status")
    @Enumerated(EnumType.STRING)
    private EnumTaskStatus status;

    @Column(name="isFavorite")
    private Boolean isFavorite;

    @Column(name="description")
    private String description;

}