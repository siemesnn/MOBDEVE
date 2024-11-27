package com.mobdeve.cherie;

public class UserData {

    private String name;
    private String bio;
    private String hobby;
    private int age;
    private String userId;
    private String location;
    private double height;
    private String gender;
    private String genderPreference;
    private boolean intentionCasualDating;
    private boolean intentionLongTerm;
    private boolean intentionMarriage;
    private String funFact;
    private String revealInfo;
    private String favoriteThings;
    private String imageUrl;

    public UserData(){
        // Empty constructor needed for Firestore
    }
    UserData(String name){
        this.name = name;
    }

    //When fetching data from Firestore
    UserData(String name, String USER_ID){
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

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getHeight() {
        return this.height;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGenderPreference(String genderPreference) {
        this.genderPreference = genderPreference;
    }

    public String getGenderPreference() {
        return this.genderPreference;
    }

    public void setIntentionCasualDating(boolean intentionCasualDating) {
        this.intentionCasualDating = intentionCasualDating;
    }

    public boolean isIntentionCasualDating() {
        return this.intentionCasualDating;
    }

    public void setIntentionLongTerm(boolean intentionLongTerm) {
        this.intentionLongTerm = intentionLongTerm;
    }

    public boolean isIntentionLongTerm() {
        return this.intentionLongTerm;
    }

    public void setIntentionMarriage(boolean intentionMarriage) {
        this.intentionMarriage = intentionMarriage;
    }

    public boolean isIntentionMarriage() {
        return this.intentionMarriage;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }

    public String getFunFact() {
        return this.funFact;
    }

    public void setRevealInfo(String revealInfo) {
        this.revealInfo = revealInfo;
    }

    public String getRevealInfo() {
        return this.revealInfo;
    }

    public void setFavoriteThings(String favoriteThings) {
        this.favoriteThings = favoriteThings;
    }

    public String getFavoriteThings() {
        return this.favoriteThings;
    }

    public String getUserId(){
        return this.userId;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

}
