package com.example.securep2pcomm.helpers;

public class Messages {

    private String id;
    private String roomName;
    private String senderId;
    private String senderName;
    private String message;
    private long sent;

    public Messages(String id, String roomName,String senderID, String senderName, String message, long sent){
        this.id = id;
        this.roomName = roomName;
        this.senderId = senderID;
        this.senderName = senderName;
        this.message = message;
        this.sent = sent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSent() {
        return sent;
    }

    public void setSent(long sent) {
        this.sent = sent;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}

