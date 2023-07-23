package com.agendaCraft.agendaCraft.converter;


import com.agendaCraft.agendaCraft.domain.User;
import com.agendaCraft.agendaCraft.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserDTOConverter {

    public UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setLastName(user.getLastName());
        return userDTO;
    }

}