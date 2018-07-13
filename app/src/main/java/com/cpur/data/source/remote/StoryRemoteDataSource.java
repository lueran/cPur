package com.cpur.data.source.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.cpur.data.Paragraph;
import com.cpur.data.Story;
import com.cpur.data.StoryAllParagraph;
import com.cpur.data.StoryDataSource;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
        String key = storyDatabaseReference.child("new-stories").push().getKey();
        storyAllParagraph.getStory().setUid(key);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/new-stories/" + key, storyAllParagraph);
        String uid = FirebaseAuth.getInstance().getUid();
        if (storyAllParagraph.getStory().getParticipants().contains(uid)) {
            childUpdates.put("/new-user-stories/" + uid + "/" + key, storyAllParagraph);
        }
        storyDatabaseReference.updateChildren(childUpdates);
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
        String uid = FirebaseAuth.getInstance().getUid();
        childUpdates.put("/new-user-stories/" + uid + "/" + key, storyAllParagraph);
        storyDatabaseReference.updateChildren(childUpdates);
    }

    //    public void removeListener() {
//        storyDatabaseReference.removeEventListener(valueChange);
//    }
}
