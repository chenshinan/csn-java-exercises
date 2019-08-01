package com.chenshinan.exercises.rocketchat.rest.dto;

public class ReportMessage {

    private String messageId;

    private String description;

    public ReportMessage() {
    }

    public ReportMessage(String messageId, String description) {
        this.messageId = messageId;
        this.description = description;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}