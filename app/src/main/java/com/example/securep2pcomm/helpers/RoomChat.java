package com.example.securep2pcomm.helpers;

public class RoomChat {

    private String room_id;
    private String room_name;
    private String person_name;
    //private String other;

    public RoomChat(){

    }

    public RoomChat(String room_id, String room_name, String person_name){
        this.room_id = room_id;
        this.room_name = room_name;
        this.person_name = person_name;
    }

    public String getPerson_name(){
        return person_name;
    }

    public void setPerson_name(String person_name){
        this.person_name = person_name;
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

}
