package com.cpur.data.source.remote;

import android.support.annotation.NonNull;

import com.cpur.data.Story;
import com.cpur.data.StoryDataSource;
import com.google.firebase.database.DatabaseReference;

public class StoryRemoteDataSource implements StoryDataSource {

    private DatabaseReference storyDatabaseReference;

    public StoryRemoteDataSource(DatabaseReference storyDatabaseReference) {
        this.storyDatabaseReference = storyDatabaseReference;
    }

    @Override
    public void getStory(@NonNull String storyId, @NonNull GetStoryCallback callback) {


    }

    @Override
    public void saveStory(@NonNull Story story) {

    }
}
