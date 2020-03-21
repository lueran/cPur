package com.cpur.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

public class StoryAllParagraph {
    @Embedded
    private Story story = null;
    @Relation(parentColumn = "uid", entityColumn = "storyId", entity =   Paragraph.class)
    private List<Paragraph> paragraphs = new ArrayList<>();

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }
}
