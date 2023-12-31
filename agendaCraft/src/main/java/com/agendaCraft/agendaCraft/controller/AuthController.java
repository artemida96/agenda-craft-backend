package com.agendaCraft.agendaCraft.controller;

import com.agendaCraft.agendaCraft.domain.User;

import com.agendaCraft.agendaCraft.dto.UserDTO;
import com.agendaCraft.agendaCraft.repository.RoleRepository;
import com.agendaCraft.agendaCraft.repository.UserRepository;
import com.agendaCraft.agendaCraft.response.JwtResponse;
import com.agendaCraft.agendaCraft.security.jwt.JwtUtils;
import com.agendaCraft.agendaCraft.security.services.UserDetailsImpl;
import com.agendaCraft.agendaCraft.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    private Set<String> invalidatedTokens = new HashSet<>();



    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserDTO  userDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        /*List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();*/

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),null
                ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody  UserDTO userDTO) {
        User user = userServiceImpl.createUser(userDTO);

        if(user!=null){
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        }

        return ResponseEntity.badRequest().body("User already exists");
    }


    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authorizationHeader, HttpServletRequest request, HttpServletResponse response) {
        String token = extractTokenFromAuthorizationHeader(authorizationHeader);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Check if the token has already been invalidated
        if (invalidatedTokens.contains(token)) {
            return "Token already invalidated. Logout not allowed.";
        }
        // Invalidate the token and mark it as invalidated
        invalidatedTokens.add(token);
        // Perform the logout action
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "Logged out successfully";
    }
    private String extractTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }
}
