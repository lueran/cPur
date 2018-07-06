package com.cpur.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class TopStoriesFragment extends StoryListFragment {

    public TopStoriesFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START my_top_posts_query]
        // My top stories by number of stars
        String myUserId = getUid();
        Query myTopStoriesQuery = databaseReference.child("user-stories").child(myUserId)
                .orderByChild("starCount");
        // [END my_top_posts_query]

        return myTopStoriesQuery;
    }
}
