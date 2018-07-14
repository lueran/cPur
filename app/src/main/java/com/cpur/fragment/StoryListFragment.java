package com.cpur.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpur.R;
import com.cpur.StoryActivity;
import com.cpur.MainViewModel;
import com.cpur.ViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class StoryListFragment extends Fragment {

    private static final String TAG = "StoryListFragment";
    private StoryAdapter mAdapter;
    private RecyclerView mRecycler;
    private MainViewModel mainViewModel;
    int viewPosition;
    GridLayoutManager mManager;
    int type = 0;

    public StoryListFragment() {
    }

    public static StoryListFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);

        StoryListFragment fragment = new StoryListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.fragment_all_stories, container, false);

        mRecycler = rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true); // Don't Change  leave false

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewModelFactory factory = ViewModelFactory.getInstance();
        mainViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        Bundle args = getArguments();
        type = Objects.requireNonNull(args).getInt("type");

        mAdapter = new StoryAdapter();

        mRecycler.setAdapter(mAdapter);
        mRecycler.setItemAnimator(new DefaultItemAnimator());

        mManager = new GridLayoutManager(getActivity(), 2);
        mRecycler.setLayoutManager(mManager);

        mainViewModel.getStories(type).observe(this, (stories) -> mAdapter.setStories(stories));

        mAdapter.setListener(story -> {
            Intent intent = new Intent(getActivity(), StoryActivity.class);
            intent.putExtra(StoryActivity.EXTRA_STORY_ID_KEY, story.getStory().getUid());
            startActivity(intent);
        });
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getUid();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPosition = mAdapter.getCurrentPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        mManager.scrollToPosition(viewPosition);
    }

}
