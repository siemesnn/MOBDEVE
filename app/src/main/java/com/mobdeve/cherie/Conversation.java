package com.mobdeve.cherie;

public class Conversation {
    private String name;
    private String messagePreview;
    private int profileImageResId;

    public Conversation(String name, String messagePreview, int profileImageResId) {
        this.name = name;
        this.messagePreview = messagePreview;
        this.profileImageResId = profileImageResId;
    }

    public String getName() {
        return name;
    }

    public String getMessagePreview() {
        return messagePreview;
    }

    public int getProfileImageResId() {
        return profileImageResId;
    }
}
