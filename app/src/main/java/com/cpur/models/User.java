package com.cpur.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class User extends BaseModel{
    private String username;
    private String email;
    private String uid;
    private HashMap<String,Boolean> notificationToken;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String token) {
        this.username = username;
        this.email = email;
        notificationToken = new HashMap<>();
        this.notificationToken.put(token,true);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public HashMap<String, Boolean> getNotificationToken() {
        return notificationToken;
    }

    public void setNotificationToken(HashMap<String, Boolean> notificationToken) {
        this.notificationToken = notificationToken;
    }
}
