package com.cpur;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;

import com.cpur.models.Paragraph;
import com.cpur.models.Story;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateStoryViewModel extends ViewModel {
    private static final String TAG = "StoryViewModel";
    private String userId;
    // Initialize Database
    private DatabaseReference databaseReference;


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {
    }

    public void start(){
        userId = FirebaseAuth.getInstance().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void writeNewStory(String title, String imageId, String body, int numParticipantsValue, int numRoundsValue) {
        String key = databaseReference.child("stories").push().getKey();
        List<String> participants = new ArrayList<>();
        participants.add(userId);
        List<Paragraph> paragraphList = new ArrayList<>();
        paragraphList.add(new Paragraph(userId, body));
        Story story = new Story(userId, title, paragraphList, imageId, numParticipantsValue, numRoundsValue, participants);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/stories/" + key, story);
        childUpdates.put("/user-stories/" + userId + "/" + key, story);
        databaseReference.updateChildren(childUpdates);
    }
}
