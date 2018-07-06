package com.cpur.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Story extends BaseModel{

    private String title = "My Story";
    public String author;
    private List<Paragraph> content;
    private String thumbnail;
    private int numParticipants = 3;
    private int numRounds = 10;
    private List<User> participants;
    private Long creationTime;
    private Long lastModified;
    private Long expirationTime;
    public HashMap stars;
    public String uid;
    public int starCount;
    public int cuurentRound;

    public Story() {
    }

    public Story(String uid, String author, String title, List<Paragraph> content, int numParticipants, int numRounds) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.numParticipants = numParticipants;
        this.numRounds = numRounds;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Paragraph> getContent() {
        return content;
    }

    public void setContent(List<Paragraph> content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public int getNumParticipants() {
        return numParticipants;
    }

    public void setNumParticipants(int numParticipants) {
        this.numParticipants = numParticipants;
    }

    public int getNumRounds() {
        return numRounds;
    }

    public void setNumRounds(int numRounds) {
        this.numRounds = numRounds;
    }


    @Override
    public String toString() {
        return "Story{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content=" + content +
                ", thumbnail='" + thumbnail + '\'' +
                ", numParticipants=" + numParticipants +
                ", numRounds=" + numRounds +
                ", participants=" + participants +
                ", creationTime=" + creationTime +
                ", lastModified=" + lastModified +
                ", expirationTime=" + expirationTime +
                ", stars=" + stars +
                ", uid='" + uid + '\'' +
                ", starCount=" + starCount +
                ", cuurentRound=" + cuurentRound +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("content", content);
        result.put("participants",numParticipants);
        result.put("rounds",numRounds);
        result.put("currentRound",cuurentRound);
        result.put("starCount", starCount);
        result.put("stars", stars);
        return result;
    }
}
