package com.example.securep2pcomm.helpers;

public class Room {

    private String room_id;
    private String room_name;
    private Boolean full;

    public Room(){

    }

    public Room(String room_id, String room_name, Boolean full){
        this.room_id = room_id;
        this.room_name = room_name;
        this.full = full;
    }

    public String getID() {
        return room_id;
    }

    public String getName() {
        return room_name;
    }

    public void setID(String room_id) {
        this.room_id = room_id;
    }

    public void setName(String room_name) {
        this.room_name = room_name;
    }

}
