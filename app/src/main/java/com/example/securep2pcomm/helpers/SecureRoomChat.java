package com.example.securep2pcomm.helpers;

public class SecureRoomChat {
    private String room_id;
    private String room_name;


    public SecureRoomChat(){}

    public SecureRoomChat(String room_id, String room_name){
        this.room_id = room_id;
        this.room_name = room_name;
    }

    public String getRoom_id(){
        return room_id;
    }

    public void setRoom_id(String room_id){
        this.room_id = room_id;
    }

    public String getRoom_name(){
        return room_name;
    }

    public void setRoom_name(String room_name){
        this.room_name = room_name;
    }
}
