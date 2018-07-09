package com.cpur.models;

public class BuzzNotification {
    private String body;
    private String fromUID;
    private String toUID;
    private String title;

    public BuzzNotification() {
    }

    public BuzzNotification(String fromUID, String toUID, String body, String title) {
        this.fromUID = fromUID;
        this.toUID = toUID;
        this.body = body;
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFromUID() {
        return fromUID;
    }

    public void setFromUID(String fromUID) {
        this.fromUID = fromUID;
    }

    public String getToUID() {
        return toUID;
    }

    public void setToUID(String toUID) {
        this.toUID = toUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
