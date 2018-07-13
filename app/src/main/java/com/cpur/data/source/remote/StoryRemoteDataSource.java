package com.cpur.data.source.remote;

import android.support.annotation.NonNull;

import com.cpur.data.Story;
import com.cpur.data.StoryDataSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class StoryRemoteDataSource implements StoryDataSource {

    private DatabaseReference storyDatabaseReference;
    private StoryValueEventListener valueChange;

    public StoryRemoteDataSource(DatabaseReference storyDatabaseReference) {
        this.storyDatabaseReference = storyDatabaseReference;
    }

    @Override
    public void getStory(@NonNull String storyId, @NonNull final GetStoryCallback callback) {
        valueChange = new StoryValueEventListener(callback);
        storyDatabaseReference.child("stories").child(storyId).addValueEventListener(valueChange);
    }

    @Override
    public void saveStory(@NonNull Story story) {

    }

    public void removeListener() {
        storyDatabaseReference.removeEventListener(valueChange);
    }

    class StoryValueEventListener implements ValueEventListener {
        private GetStoryCallback callback;

        public StoryValueEventListener(GetStoryCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Story story = dataSnapshot.getValue(Story.class);
            callback.onStoryLoaded(story);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
