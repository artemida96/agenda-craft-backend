package com.agendaCraft.agendaCraft.domain;


import com.agendaCraft.agendaCraft.enums.EnumStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String name;

    @NotNull
    @Column(name="due_date")
    private Date dueDate;


    @JsonIgnore
    @Column(name="date_created")
    private Date dateCreated;

    @JsonIgnore
    @Column(name = "user_id")
    private Long userId;

    @Column (name= "status")
    private EnumStatus status;

    @Column(name="isFavorite")
    private Boolean isFavorite;


}