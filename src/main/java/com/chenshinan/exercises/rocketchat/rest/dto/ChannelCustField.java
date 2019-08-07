package com.chenshinan.exercises.rocketchat.rest.dto;

import java.util.Map;

/**
 * @author shinan.chen
 * @since 2019/8/2
 */
public class ChannelCustField {
    private String roomId;
    private String roomName;
    private Map<String, String> customFields;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Map<String, String> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, String> customFields) {
        this.customFields = customFields;
    }
}
