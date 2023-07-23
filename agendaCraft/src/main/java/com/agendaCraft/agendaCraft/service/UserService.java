package com.agendaCraft.agendaCraft.service;

import com.agendaCraft.agendaCraft.domain.User;
import com.agendaCraft.agendaCraft.dto.UserDTO;

public interface UserService {
    User findById(int id);
    User createUser(UserDTO userDto);

    boolean userExists(String email);
}