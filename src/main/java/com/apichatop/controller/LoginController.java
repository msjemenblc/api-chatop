package com.apichatop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.apichatop.model.User;
import com.apichatop.service.JwtService;
import com.apichatop.service.UserService;

@RestController
public class LoginController {

    public JwtService jwtService;
    public UserService userService;

    public LoginController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/api/auth/login")
    public String getToken(Authentication authentication) {
        String token = jwtService.generateToken(authentication.getName());
        return token;
    }

    @PostMapping("api/auth/register")
    public String registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return jwtService.generateToken(registeredUser.getName());
    }

}
