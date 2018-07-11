package com.cpur.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
@Entity
public class User {
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "email")
    private String email;
    @PrimaryKey
    @NonNull
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
