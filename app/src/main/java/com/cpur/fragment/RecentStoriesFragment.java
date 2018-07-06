package com.cpur.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class RecentStoriesFragment extends StoryListFragment {

    public RecentStoriesFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 stories, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentStoriesQuery = databaseReference.child("stories")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentStoriesQuery;
    }
}
