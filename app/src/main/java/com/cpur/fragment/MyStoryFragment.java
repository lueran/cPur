package com.cpur.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyStoryFragment extends StoryListFragment {

    public MyStoryFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my stories
        return databaseReference.child("new-user-stories")
                .child(getUid());
    }
}
