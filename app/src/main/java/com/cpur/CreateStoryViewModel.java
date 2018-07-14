package com.cpur;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;

import com.cpur.data.Paragraph;
import com.cpur.data.Story;
import com.cpur.data.StoryAllParagraph;
import com.cpur.data.StoryDataSource;
import com.cpur.data.StoryRepository;
import com.cpur.data.source.local.StoryLocalDataSource;
import com.cpur.db.AppDatabase;
import com.cpur.utils.AppExecutors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreateStoryViewModel extends ViewModel {
    private static final String TAG = "StoryViewModel";
    private String userId;
    private StoryRepository storyRepo;

    public CreateStoryViewModel(Application mApplication, StoryRepository storyRepository) {
        this.storyRepo = storyRepository;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {
    }

    public void start() {
        userId = FirebaseAuth.getInstance().getUid();
    }

    public void writeNewStory(String title, String imageId, String body, int numParticipantsValue, int numRoundsValue) {
        List<String> participants = new ArrayList<>();
        participants.add(userId);
        List<Paragraph> paragraphList = new ArrayList<>();
        Paragraph paragraph = new Paragraph(UUID.randomUUID().toString(), userId, body);
        paragraphList.add(paragraph);
        Story story = new Story(userId, title, imageId, numParticipantsValue, numRoundsValue, participants);
        StoryAllParagraph storyAllParagraph = new StoryAllParagraph();
        storyAllParagraph.setStory(story);
        storyAllParagraph.setParagraphs(paragraphList);
        storyRepo.createStory(story, paragraph);
    }
}
