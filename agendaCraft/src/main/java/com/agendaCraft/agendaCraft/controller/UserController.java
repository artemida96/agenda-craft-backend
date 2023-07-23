package com.agendaCraft.agendaCraft.controller;

import com.agendaCraft.agendaCraft.domain.User;

import com.agendaCraft.agendaCraft.dto.UserDTO;
import com.agendaCraft.agendaCraft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RequestMapping("api/user")
//@CrossOrigin(origins = "http://localhost:3000/", maxAge = 3600)
@RestController
public class UserController {

    @Autowired
    private  UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {

        User user = userService.createUser(userDTO);
        if(Objects.nonNull(user)){
            return ResponseEntity.ok("User created successfully");
        }
        return ResponseEntity.ok("User already exists");
    }
}
