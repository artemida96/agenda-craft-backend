package com.agendaCraft.agendaCraft.domain;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(name="username")
    private String username;

    @Column(name="first-name")
    private String firstName;

    @Column(name="last-name")
    private String lastName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    private String password;
    private String role;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Task> tasks;

    public User() {
    }

    public User(String username, String email, String password, String firstName, String lastName, String role, List<Task> tasks) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName  = firstName;
        this.lastName = lastName;
        this.role = role;
        this.tasks = tasks;
    }

}