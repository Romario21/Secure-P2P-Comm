package com.example.securep2pcomm.helpers;

import java.util.ArrayList;

public class AvailableRoom {

    private String room_id;
    private String room_name;
    private String owner;
    private String owner_name;
    private String guest;
    private Boolean full;

    public AvailableRoom(){

    }

    public AvailableRoom(String room_id, String room_name, String owner, String owner_name, String guest, Boolean full){
        this.room_id = room_id;
        this.room_name = room_name;
        this.owner = owner;
        this.owner_name = owner_name;
        this.guest = guest;
        this.full = full;
    }

    public String getID(){
        return room_id;
    }

    public void setID(String room_id){
        this.room_id = room_id;
    }

    public String getRoom_name(){
        return room_name;
    }

    public void setRoom_name(String room_name){
        this.room_name = room_name;
    }

    public void setFull(boolean full){
        this.full = full;
    }

    public Boolean getFull(){
        return full;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name){
        this.owner_name = owner_name;
    }
}
