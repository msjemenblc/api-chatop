package com.apichatop.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apichatop.dto.requests.RegisterRequest;
import com.apichatop.dto.responses.UserDTO;
import com.apichatop.exception.EmailAlreadyExistsException;
import com.apichatop.model.User;
import com.apichatop.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByEmailOrUsername(String emailOrUsername) {
        Optional<User> user = userRepository.findByEmail(emailOrUsername);
        if (user.isEmpty()) {
            user = userRepository.findByName(emailOrUsername);
        }
        return user;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserDTO registerUser(RegisterRequest registerRequest) {
        boolean emailExists = userRepository.existsByEmail(registerRequest.getEmail());
        boolean usernameExists = userRepository.existsByName(registerRequest.getName());

        if (emailExists || usernameExists) {
            throw new EmailAlreadyExistsException("Email or username already exist: " + registerRequest.getEmail());
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }

}
