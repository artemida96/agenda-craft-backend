package com.agendaCraft.agendaCraft.service;

import com.agendaCraft.agendaCraft.converter.UserConverter;
import com.agendaCraft.agendaCraft.converter.UserDTOConverter;
import com.agendaCraft.agendaCraft.domain.User;
import com.agendaCraft.agendaCraft.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.agendaCraft.agendaCraft.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private UserDTOConverter userDTOConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public boolean userExists(String email) {
        return userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public User createUser(UserDTO userDTO) {
            User user = userConverter.converter(userDTO);
            if(userExists(user.getEmail())){
                return null;
            }
            userRepository.save(user);
            return user;
    }

    @Override
    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


}