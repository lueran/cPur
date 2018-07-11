package com.cpur;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.cpur.models.Paragraph;
import com.cpur.models.Story;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StoryViewModel extends ViewModel {

    private static final String TAG = "StoryViewModel";
    private MutableLiveData<Story> storyLiveData = new MutableLiveData<Story>(){
        @Override
        protected void onActive() {
            super.onActive();
            if (storyReference != null) {
                storyReference.addValueEventListener(storyListener);
            }
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            if (storyReference != null) {
                storyReference.removeEventListener(storyListener);
            }
        }
    };
    private String uid;

    public MutableLiveData<Story> getStory() {
        return storyLiveData;
    }
    // Initialize Database
    private DatabaseReference storyReference;

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
        // Remove story value event listener

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {
    }

    public void start(String storyId){
        uid = FirebaseAuth.getInstance().getUid();
        storyReference = FirebaseDatabase.getInstance().getReference()
                .child("stories").child(storyId);
    }

    public void sendContent(String content) {
        Story story = storyLiveData.getValue();
        if(story != null) {
            story.getContent().add(new Paragraph(uid, content));
            story.setTurn(story.getTurn() + 1);
            storyReference.setValue(story);
        }
    }

    public void clap() {
        Story story = storyLiveData.getValue();
        if(story != null) {
            story.setClaps(story.getClaps() + 1);
            storyReference.setValue(story);
        }
    }

    public void joinStory() {
        Story story = storyLiveData.getValue();
        if(story != null) {
            story.getParticipants().add(uid);
            storyReference.setValue(story);
        }
    }

    public boolean isMyTurn() {
        Story story = storyLiveData.getValue();
        return story != null && story.getParticipants().get(story.getTurn() % story.getParticipants().size()).equals(uid);

    }

    public boolean isParticipants() {
        Story story = storyLiveData.getValue();
        return story != null && story.getParticipants().contains(uid);
    }

    public boolean isMyContent() {
        Story story = storyLiveData.getValue();
        return story != null && story.getContent().get(story.getContent().size()-1).getAuthorId().equals(uid);
    }

    private ValueEventListener storyListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Story object and use the values to update the UI
            Story story = dataSnapshot.getValue(Story.class);
            storyLiveData.setValue(story);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Story failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    };

    public String getUID() {
        return uid;
    }
}
