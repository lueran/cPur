package com.cpur.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Story extends BaseModel{

    private String title = "My Story";
    private String ownerId;
    private List<Paragraph> content;
    private String thumbnail;
    private int numParticipants = 3;
    private int numRounds = 10;
    private List<User> participants;
    private Long creationTime;
    private Long lastModified;
    private Long expirationTime;

    public Story() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Story(String uid, String title, String ownerId, List<Paragraph> content, String thumbnail, int maxParticipants, int minParticipants, int maxRounds, int numParticipants, int numRounds, List<User> participants, Long creationTime, Long lastModified, Long expirationTime) {
        this.title = title;
        this.ownerId = ownerId;
        this.content = content;
        this.thumbnail = thumbnail;
        this.numParticipants = numParticipants;
        this.numRounds = numRounds;
        this.participants = participants;
        this.creationTime = creationTime;
        this.lastModified = lastModified;
        this.expirationTime = expirationTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
                ", title='" + title + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", content=" + content +
                ", thumbnail='" + thumbnail + '\'' +
                ", numParticipants=" + numParticipants +
                ", numRounds=" + numRounds +
                ", participants=" + participants +
                ", creationTime=" + creationTime +
                ", lastModified=" + lastModified +
                ", expirationTime=" + expirationTime +
                '}';
    }
}
