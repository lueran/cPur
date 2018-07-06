package com.cpur.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class StoryFragment extends StoryListFragment {

    public StoryFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my stories
        return databaseReference.child("user-stories")
                .child(getUid());
    }
}
