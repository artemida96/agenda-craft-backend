package com.agendaCraft.agendaCraft.dto;

import com.agendaCraft.agendaCraft.domain.Role;
import com.agendaCraft.agendaCraft.domain.Task;

import java.util.List;
import java.util.Set;

public class UserDTO {

    public String firstName;
    public String email;
    public String password;

    public String username;

    public String lastName;

    public  String role;

    public List<Task> tasks;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getLastName() {
        return lastName;
    }


    public String getUsername() {
        return username;
    }

    public List<Task> getTasks(){
        return tasks;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
