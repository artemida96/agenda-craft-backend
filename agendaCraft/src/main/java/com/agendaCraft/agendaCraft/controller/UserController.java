package com.agendaCraft.agendaCraft.controller;

import com.agendaCraft.agendaCraft.domain.User;
import com.agendaCraft.agendaCraft.dto.UserDTO;
import com.agendaCraft.agendaCraft.repository.UserRepository;
import com.agendaCraft.agendaCraft.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userServiceImpl;

    @GetMapping("/me")
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return  userRepository.findByUsername(username);
    }

    @PutMapping("/me")
    public  ResponseEntity<Object> updateCurrentUser(@RequestBody UserDTO userToUpdate) {
        User updatedUser = userServiceImpl.updateUser(userToUpdate);
        if (Objects.nonNull(updatedUser)){
            return  ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.badRequest().body("Task could not be created");
    }
}