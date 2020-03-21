package com.cpur.data.source.remote;

import androidx.annotation.NonNull;

import com.cpur.data.Paragraph;
import com.cpur.data.Story;
import com.cpur.data.StoryAllParagraph;
import com.cpur.data.StoryDataSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryRemoteDataSource implements StoryDataSource {

    private DatabaseReference storyDatabaseReference;

    public StoryRemoteDataSource(DatabaseReference storyDatabaseReference) {
        this.storyDatabaseReference = storyDatabaseReference;
    }

    @Override
    public void getStory(@NonNull String storyId, @NonNull final GetStoryCallback<StoryAllParagraph> callback) {
        storyDatabaseReference.child("new-stories").child(storyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onComplete(dataSnapshot.getValue(StoryAllParagraph.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void saveStory(@NonNull StoryAllParagraph storyAllParagraph) {
        String key = storyAllParagraph.getStory().getUid();
        if (key == null) {
            key = storyDatabaseReference.child("new-stories").push().getKey();
        }
        storyAllParagraph.getStory().setUid(key);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/new-stories/" + key, storyAllParagraph);
        for (String uid : storyAllParagraph.getStory().getParticipants()) {
            childUpdates.put("/new-user-stories/" + uid + "/" + key, storyAllParagraph);
        }
        storyDatabaseReference.updateChildren(childUpdates);
    }

    @Override
    public void getUserStories(@NonNull final GetStoryCallback<List<StoryAllParagraph>> callback) {
        Query recentStoriesQuery = storyDatabaseReference.child("new-user-stories").child(getUID()).limitToLast(100);
        recentStoriesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, StoryAllParagraph>> genericTypeIndicator = new GenericTypeIndicator<Map<String, StoryAllParagraph>>() {
                };
                Map<String, StoryAllParagraph> stringStoryAllParagraphMap = dataSnapshot.getValue(genericTypeIndicator);
                if (stringStoryAllParagraphMap != null) {
                    Collection<StoryAllParagraph> values = stringStoryAllParagraphMap.values();
                    callback.onComplete(new ArrayList<>(values));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getAllStories(@NonNull final GetStoryCallback<List<StoryAllParagraph>> callback) {
        Query recentStoriesQuery = storyDatabaseReference.child("new-stories").limitToLast(100);
        recentStoriesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, StoryAllParagraph>> genericTypeIndicator = new GenericTypeIndicator<Map<String, StoryAllParagraph>>() {
                };
                Map<String, StoryAllParagraph> stringStoryAllParagraphMap = dataSnapshot.getValue(genericTypeIndicator);
                if (stringStoryAllParagraphMap != null) {
                    Collection<StoryAllParagraph> values = stringStoryAllParagraphMap.values();
                    callback.onComplete(new ArrayList<>(values));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void saveStories(@NonNull List<StoryAllParagraph> stories) {

    }

    @Override
    public void createStory(Story story, Paragraph paragraph) {
        String key = storyDatabaseReference.child("new-stories").push().getKey();
        story.setUid(key);
        paragraph.setStoryId(key);
        StoryAllParagraph storyAllParagraph = new StoryAllParagraph();
        storyAllParagraph.setStory(story);
        List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(paragraph);
        storyAllParagraph.setParagraphs(paragraphs);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/new-stories/" + key, storyAllParagraph);
        childUpdates.put("/new-user-stories/" + getUID() + "/" + key, storyAllParagraph);
        storyDatabaseReference.updateChildren(childUpdates);
    }


}
