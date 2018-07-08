package com.cpur.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Story extends BaseModel {

    public static enum Status {
        PENDING, IN_PROGRESS, COMPLETED
    }

    private String title;
    private String author;
    private List<Paragraph> content;
    private String coverImage;
    private int minParticipants = 3;
    private int maxParticipants;
    private int numRounds;
    private List<String> participants;
    private HashMap<String, Object> dateCreated;
    private HashMap<String, Object> lastModified;
    private int claps;
    private String uid;
    private int turn = 0;
    private Status currentStatus = Status.PENDING;

    public Story() {
    }

    public Story(String title, String author, List<Paragraph> content, String coverImage, int maxParticipants, int numRounds, List<String> participants, String uid) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.coverImage = coverImage;
        this.maxParticipants = maxParticipants;
        this.numRounds = numRounds;
        this.participants = participants;
        //Date last changed will always be set to ServerValue.TIMESTAMP
        HashMap<String, Object> dateLastChangedObj = new HashMap<>();
        dateLastChangedObj.put("date", ServerValue.TIMESTAMP);
        this.dateCreated = dateLastChangedObj;
        this.lastModified = dateLastChangedObj;
        this.uid = uid;
    }

    public HashMap<String, Object> getLastModified() {
        return lastModified;
    }

    public HashMap<String, Object> getDateCreated() {
        //If there is a dateCreated object already, then return that
        if (dateCreated != null) {
            return dateCreated;
        }
        //Otherwise make a new object set to ServerValue.TIMESTAMP
        HashMap<String, Object> dateCreatedObj = new HashMap<String, Object>();
        dateCreatedObj.put("date", ServerValue.TIMESTAMP);
        return dateCreatedObj;
    }

    // Use the method described in https://stackoverflow.com/questions/25500138/android-chat-crashes-on-datasnapshot-getvalue-for-timestamp/25512747#25512747
    // to get the long values from the date object.
    @Exclude
    public long getDateLastChangedLong() {

        return (long) lastModified.get("date");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Paragraph> getContent() {
        return content;
    }

    public void setContent(List<Paragraph> content) {
        this.content = content;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getNumRounds() {
        return numRounds;
    }

    public void setNumRounds(int numRounds) {
        this.numRounds = numRounds;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public void setDateCreated(HashMap<String, Object> dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLastModified(HashMap<String, Object> lastModified) {
        this.lastModified = lastModified;
    }

    public int getClaps() {
        return claps;
    }

    public void setClaps(int claps) {
        this.claps = claps;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Status currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Exclude
    public long getDateCreatedLong() {
        return (long) dateCreated.get("date");
    }
}
