package com.apichatop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apichatop.dto.requests.AuthRequest;
import com.apichatop.dto.requests.RegisterRequest;
import com.apichatop.dto.responses.UserDTO;
import com.apichatop.model.User;
import com.apichatop.service.JwtService;
import com.apichatop.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;

@Tag(
    name = "Login Controller", 
    description = "Opérations concernant l'utilisateur, gère les connexion ainsi que la création de token."
)
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtDecoder jwtDecoder;

    public JwtService jwtService;
    public UserService userService;

    public LoginController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Operation(
        summary = "Connecte un utilisateur", 
        description = "Vérifie les identifiants; s'ils correspondent, un token est renvoyé"
    )
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> getToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            String jwtToken = jwtService.generateToken(authentication.getName());

            Map<String, String> response = new HashMap<>();
            response.put("token", jwtToken);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Operation(
        summary = "Enregistre un utilisateur", 
        description = "Vérifie si celui-ci n'est pas déjà existant; si négatif, renvoie un token"
    )
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterRequest registerRequest) {
        UserDTO registeredUser = userService.registerUser(registerRequest);

        String jwtToken = jwtService.generateToken(registeredUser.getName());

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);

        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Renvoie les informations sur l'utilisateur", 
        description = "Récupère le token de la connexion pour le décomposer et renvoyer les informations correspondantes"
    )
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String jwtToken = token.substring(7);

        Jwt decodedJwt = jwtDecoder.decode(jwtToken);
        String emailOrUsername = decodedJwt.getSubject();

        User user = userService.findByEmailOrUsername(emailOrUsername)
                               .orElse(null);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());

        return ResponseEntity.ok(userDTO);
    }

}
