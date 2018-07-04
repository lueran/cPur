package com.cpur.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User extends BaseModel{
    private String uid;
    private String username;
    private String email;
    private String nickname;
    private String avatar;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String uid, String nickname, String avatar) {
        this.username = username;
        this.email = email;
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
}
