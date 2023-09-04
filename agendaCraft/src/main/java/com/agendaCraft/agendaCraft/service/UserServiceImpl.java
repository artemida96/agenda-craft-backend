package com.agendaCraft.agendaCraft.service;

import com.agendaCraft.agendaCraft.converter.UserConverter;
import com.agendaCraft.agendaCraft.converter.UserDTOConverter;
import com.agendaCraft.agendaCraft.enums.EnumRole;
import com.agendaCraft.agendaCraft.domain.Role;
import com.agendaCraft.agendaCraft.domain.User;
import com.agendaCraft.agendaCraft.dto.UserDTO;
import com.agendaCraft.agendaCraft.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.agendaCraft.agendaCraft.repository.UserRepository;
import org.webjars.NotFoundException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private UserDTOConverter userDTOConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    public User updateUser(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(encoder.encode(userDTO.getPassword()));
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setRole(userDTO.getRole());
            user.setTasks(userDTO.getTasks());
            return userRepository.save(user);
        } else {
          return null;
        }
    }

    @Override
    public User createUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername()) || userRepository.existsByEmail(userDTO.getEmail()) ) {
            return null;
            //return "Error: Email is already in use!"; //dont give for email that may exist
        }

        User user = new User(userDTO.getUsername(),
                userDTO.getEmail(),
                encoder.encode(userDTO.getPassword()), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getRole(), null);

        String strRole = userDTO.getRole();
        Set<Role> roles = new HashSet<>();

        /*if (strRole == null) {
            Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            switch (strRole) {
                case "admin":
                    Role adminRole = roleRepository.findByName(EnumRole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);

                    break;
                case "mod":
                    Role modRole = roleRepository.findByName(EnumRole.ROLE_MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(modRole);

                    break;
                default:
                    Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
            }
        }*/
        user.setRole(strRole);
        userRepository.save(user);
        return user;
    }
}