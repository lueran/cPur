package com.cpur.models;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class Paragraph {
    private String uid;
    private String authorId;
    private String body;
    private HashMap<String, Object> dateCreated;

    public Paragraph() {
    }

    public Paragraph(String authorId, String body) {
        this.authorId = authorId;
        this.body = body;
        HashMap<String, Object> dateLastChangedObj = new HashMap<>();
        dateCreated.put("date", ServerValue.TIMESTAMP);
        this.dateCreated = dateLastChangedObj;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
