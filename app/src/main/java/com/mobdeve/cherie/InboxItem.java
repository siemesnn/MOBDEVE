package com.mobdeve.cherie;

public class InboxItem {
    private final String name;
    private final String messagePreview;

    public InboxItem(String name, String messagePreview) {
        this.name = name;
        this.messagePreview = messagePreview;
    }

    public String getName() {
        return name;
    }

    public String getMessagePreview() {
        return messagePreview;
    }
}
