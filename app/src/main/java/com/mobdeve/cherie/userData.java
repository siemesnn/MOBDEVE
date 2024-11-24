package com.mobdeve.cherie;

public class userData {

    private String name;
    private String bio;
    private String hobby;
    private int age;
    private String userId;

    public userData(){
        // Empty constructor needed for Firestore
    }
    userData(String name){
        this.name = name;
    }

    //When fetching data from Firestore
    userData(String name, String USER_ID){
        this.name = name;
        this.userId = USER_ID;
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

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }

    public String getUserId(){
        return this.userId;
    }
}
