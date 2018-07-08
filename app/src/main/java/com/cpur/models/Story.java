package com.cpur.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Story extends BaseModel{

    enum Status
    {
        PENDING, IN_PROGRESS, COMPLETED
    }
    private String title = "My Story";
    public String author;
    private List<Paragraph> content;
    private String thumbnail;
    private int minParticipants = 3;
    private int maxParticipants = 50;
    private int numRounds = 10;
    private List<User> participants;
    private  HashMap<String, Object> dateCreated;
    private HashMap<String, Object> lastModified;
    private Long expirationTime;
    public HashMap claps;
    public String uid;
    public int clapsCount;
    public int currentRound = 0 ;
    public Status currentStatus;

    public Story() {
    }

    public Story(String uid, String author, String title, List<Paragraph> content, int numParticipants, int numRounds) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.numRounds = numRounds;
        this.uid = uid;
        this.currentStatus = Status.PENDING;
        //Date last changed will always be set to ServerValue.TIMESTAMP
        HashMap<String, Object> dateLastChangedObj = new HashMap<>();
        dateLastChangedObj.put("date", ServerValue.TIMESTAMP);
        this.dateCreated = dateLastChangedObj;
        this.lastModified = dateLastChangedObj;
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

    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public int getNumRounds() {
        return numRounds;
    }

    public void setNumRounds(int numRounds) {
        this.numRounds = numRounds;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public HashMap getClaps() {
        return claps;
    }

    public void setClaps(HashMap claps) {
        this.claps = claps;
    }

    public int getClapsCount() {
        return clapsCount;
    }

    public void setClapsCount(int clapsCount) {
        this.clapsCount = clapsCount;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Status currentStatus) {
        this.currentStatus = currentStatus;
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

        return (long)lastModified.get("date");
    }

    @Exclude
    public long getDateCreatedLong() {
        return (long)dateCreated.get("date");
    }

    @Override
    public String toString() {
        return "Story{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content=" + content +
                ", thumbnail='" + thumbnail + '\'' +
                ", numRounds=" + numRounds +
                ", participants=" + participants +
                ", creationTime=" + dateCreated +
                ", lastModified=" + lastModified +
                ", expirationTime=" + expirationTime +
                ", claps=" + claps +
                ", uid='" + uid + '\'' +
                ", clapsCount=" + clapsCount +
                ", currentRound=" + currentRound +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("content", content);
        result.put("rounds",numRounds);
        result.put("currentRound", currentRound);
        result.put("clapsCount", clapsCount);
        result.put("claps", claps);
        return result;
    }
}
