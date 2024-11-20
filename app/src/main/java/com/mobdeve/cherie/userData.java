package com.mobdeve.cherie;

public class userData {

    private String name;
    private String bio;
    private String hobby;

    public userData(){
        // Empty constructor needed for Firestore
    }
    userData(String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setBio(String bio){
        this.bio = bio;
    }

    public String getBio(){
        return this.bio;
    }

    public void setHobby(String hobby){
        this.hobby = hobby;
    }

    public String getHobby(){
        return this.hobby;
    }
}
