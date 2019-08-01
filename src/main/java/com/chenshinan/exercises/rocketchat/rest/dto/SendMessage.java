package com.chenshinan.exercises.rocketchat.rest.dto;

public class SendMessage {

    private SendMessageParam message;

    public SendMessage() {
    }

    public SendMessage(SendMessageParam message) {
        this.message = message;
    }

    public SendMessageParam getMessage() {
        return message;
    }

    public void setMessage(SendMessageParam message) {
        this.message = message;
    }
}