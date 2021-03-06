package com.cpur.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;


import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Story.class,
        parentColumns = "uid",
        childColumns = "storyId",
        onDelete = CASCADE))
public class Paragraph {
    @PrimaryKey
    @NonNull
    private String uid;
    private String authorId;
    private String body;
    private String storyId;

    public Paragraph() {
    }

    public Paragraph(String uid, String authorId, String body) {
        this.uid = uid;
        this.authorId = authorId;
        this.body = body;
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

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    @Exclude
    @Ignore
    public String getSuffixBody() {
        StringBuilder suffixBuilder = new StringBuilder();

        if (this.getBody() != null) {
            String[] words = this.getBody().split(" ");
            int half = (int) Math.round(words.length / 2.0);

            for (int wordAt = half; wordAt < words.length; wordAt++) {
                suffixBuilder.append(" ").append(words[wordAt]);
            }
        }

        return suffixBuilder.toString().trim();
    }
}
