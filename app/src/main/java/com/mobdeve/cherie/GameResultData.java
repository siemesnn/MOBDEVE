package com.mobdeve.cherie;

public class GameResultData {
    private String userId;
    private int percentage;

    public GameResultData(){

    }

    public GameResultData(String currentUserId, int percentage) {
        this.userId = currentUserId;
        this.percentage = percentage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String currentUserId) {
        this.userId = currentUserId;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

}
