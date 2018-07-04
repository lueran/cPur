package com.cpur.models;

public class Paragraph {
    private String uid;
    private String authorId;
    private String s1;
    private String s2;
    private Long creationTime;

    public Paragraph() {
    }

    public Paragraph(String uid, String authorId, String s1, String s2, Long creationTime) {
        this.uid = uid;
        this.authorId = authorId;
        this.s1 = s1;
        this.s2 = s2;
        this.creationTime = creationTime;
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

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }
}
