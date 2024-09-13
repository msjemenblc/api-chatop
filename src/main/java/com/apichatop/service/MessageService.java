package com.apichatop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apichatop.dto.responses.MessageDTO;
import com.apichatop.model.Message;
import com.apichatop.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public MessageDTO convertToDTO(Message message) {
        return new MessageDTO(
            message.getId(),
            message.getMessage(),
            message.getUser().getId(),
            message.getRental().getId()
        );
    }
}
