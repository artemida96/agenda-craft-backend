package com.agendaCraft.agendaCraft.service;

import com.agendaCraft.agendaCraft.domain.User;
import com.agendaCraft.agendaCraft.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    User findById(int id);
    User createUser(UserDTO userDto);

    User updateUser(UserDTO userDto);

    boolean userExists(String email);

    Optional<User> getCurrentUser();
}