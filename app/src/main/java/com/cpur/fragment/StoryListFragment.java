package com.cpur.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpur.R;
import com.cpur.StoryActivity;
import com.cpur.models.Story;
import com.cpur.viewholder.StoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;


public abstract class StoryListFragment extends Fragment {

    private static final String TAG = "StoryListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Story, StoryViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private GridLayoutManager mManager;

    public StoryListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_stories, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new GridLayoutManager(getActivity(), 2);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Story>()
                .setQuery(postsQuery, Story.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Story, StoryViewHolder>(options) {

            @Override
            public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new StoryViewHolder(inflater.inflate(R.layout.item_story, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(StoryViewHolder viewHolder, int position, final Story model) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole story view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), StoryActivity.class);
                        intent.putExtra(StoryActivity.EXTRA_STORY_ID_KEY, postKey);
                        startActivity(intent);
                    }
                });

                // Determine if the current user has liked this story and set UI accordingly
                // Bind Story to ViewHolder
                viewHolder.bindToStory(model);
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

}
