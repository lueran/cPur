package com.cpur.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
@Entity
public class Story {

    public enum Status {

        PENDING("PENDING"), // before enough participants
        IN_PROGRESS("IN_PROGRESS"), // started play
        COMPLETED("COMPLETED");

        private final String text;

        Status(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private String title;
    private String author;
    @Ignore
    private List<Paragraph> content;
    private String coverImage;
    private int minParticipants = 3;
    private int maxParticipants;
    private int numRounds;
    private List<String> participants;
    private int claps;
    @PrimaryKey
    @NonNull
    private String uid;
    private int turn = 1;
    private Status currentStatus = Status.PENDING;

    public Story() {
    }

    public Story(String author, String title, List<Paragraph> content, String coverImage, int maxParticipants, int numRounds, List<String> participants) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.coverImage = coverImage;
        this.maxParticipants = maxParticipants;
        this.numRounds = numRounds;
        this.participants = participants;
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
    public String getNextTurnUID() {
        int nextTurnIndex = this.turn % this.participants.size();
        return this.participants.get(nextTurnIndex);
    }
}
