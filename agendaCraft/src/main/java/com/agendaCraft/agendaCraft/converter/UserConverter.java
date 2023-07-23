package com.agendaCraft.agendaCraft.converter;

import com.agendaCraft.agendaCraft.domain.User;
import com.agendaCraft.agendaCraft.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {


    public User converter(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        return user;
    }
}