package com.apichatop.dto.requests;

import javax.validation.constraints.NotBlank;

public class MessageRequest {

    @NotBlank
    private String message;

    @NotBlank
    private Long userId;

    @NotBlank
    private Long rentalId;

    public MessageRequest() {}

    public MessageRequest(String message, Long userId, Long rentalId) {
        this.message = message;
        this.userId = userId;
        this.rentalId = rentalId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }
    
}
