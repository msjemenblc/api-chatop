package com.apichatop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.apichatop.dto.requests.AuthRequest;
import com.apichatop.dto.requests.RegisterRequest;
import com.apichatop.dto.responses.UserDTO;
import com.apichatop.service.JwtService;
import com.apichatop.service.UserService;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtService jwtService;
    public UserService userService;

    public LoginController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/api/auth/login")
    public String getToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            return jwtService.generateToken(authentication.getName());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @PostMapping("api/auth/register")
    public String registerUser(@RequestBody RegisterRequest registerRequest) {
        UserDTO registeredUser = userService.registerUser(registerRequest);

        return jwtService.generateToken(registeredUser.getName());
    }

}
