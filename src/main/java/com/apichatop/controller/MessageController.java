package com.apichatop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apichatop.dto.requests.MessageRequest;
import com.apichatop.model.Message;
import com.apichatop.model.Rental;
import com.apichatop.model.User;
import com.apichatop.service.MessageService;
import com.apichatop.service.RentalService;
import com.apichatop.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private RentalService rentalService;

    @PostMapping
    public ResponseEntity<Map <String, String>> createMessage(@RequestBody MessageRequest messageRequest) {

        if (messageRequest.getUserId() == null || messageRequest.getRentalId() == null) {
            throw new IllegalArgumentException("User ID or Rental ID must not be null");
        }

        User user = userService.findById(messageRequest.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + messageRequest.getUserId()));

        Rental rental = rentalService.getRentalEntity(messageRequest.getRentalId())
            .orElseThrow(() -> new RuntimeException("Rental not found with id: " + messageRequest.getRentalId()));

        Message newMessage = new Message();
        newMessage.setMessage(messageRequest.getMessage());
        newMessage.setUser(user);
        newMessage.setRental(rental);

        messageService.createMessage(newMessage);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Message send with success");

        return ResponseEntity.ok(response);
    }
    
}
