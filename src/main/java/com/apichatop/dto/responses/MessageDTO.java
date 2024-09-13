package com.apichatop.dto.responses;

public class MessageDTO {
    
    private Long id;
    private String message;
    private Long userId;
    private Long rentalId;

    public MessageDTO() {}


    public MessageDTO(Long id, String message, Long userId, Long rentalId) {
        this.id = id;
        this.message = message;
        this.userId = userId;
        this.rentalId = rentalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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