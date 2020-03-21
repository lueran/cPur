package com.cpur;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cpur.data.Paragraph;
import com.cpur.data.StoryAllParagraph;
import com.cpur.data.StoryRepository;
import com.google.firebase.auth.FirebaseAuth;
import java.util.UUID;

public class StoryViewModel extends ViewModel {

    private static final String TAG = "StoryViewModel";
    private final StoryRepository storyRepo;
    private LiveData<StoryAllParagraph> storyLiveData /*= new MutableLiveData<Story>(){
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
    }*/;
    private String uid;

    public StoryViewModel(Application mApplication, StoryRepository storyRepository) {
        this.storyRepo = storyRepository;
    }

    public LiveData<StoryAllParagraph> getStory() {
        return storyLiveData;
    }
    // Initialize Database
//    private DatabaseReference storyReference;
//    private DatabaseReference databaseReference;

    public void start(String storyId) {
        uid = FirebaseAuth.getInstance().getUid();
        storyLiveData = storyRepo.getStory(storyId);
        /*storyReference = FirebaseDatabase.getInstance().getReference()
                .child("stories").child(storyId);
        databaseReference = FirebaseDatabase.getInstance().getReference();*/

//        storyLiveData = StoryRepository.getInstance()
    }

    public void sendContent(String content, String storyId) {
        StoryAllParagraph story = storyLiveData.getValue();
        if (story != null) {
            story.getParagraphs().add(new Paragraph(UUID.randomUUID().toString(), uid, content));
            story.getStory().setTurn(story.getStory().getTurn() + 1);
            saveStory(story, storyId);
        }
    }

    public void clap(String storyId) {
        StoryAllParagraph story = storyLiveData.getValue();
        if (story != null) {
            story.getStory().setClaps(story.getStory().getClaps() + 1);
            saveStory(story, storyId);
        }
    }

    public void joinStory(String storyId) {
        StoryAllParagraph story = storyLiveData.getValue();
        if (story != null) {
            story.getStory().getParticipants().add(uid);
            saveStory(story, storyId);
        }
    }

    private void saveStory(StoryAllParagraph story, String storyId) {
        if (story != null) {
            story.getStory().setUid(storyId);
            storyRepo.saveStory(story);
        }
    }

    public boolean isMyTurn() {
        StoryAllParagraph story = storyLiveData.getValue();
        return story != null && story.getStory().getParticipants().get(story.getStory().getTurn() % story.getStory().getParticipants().size()).equals(uid);

    }

    public boolean isParticipants() {
        StoryAllParagraph story = storyLiveData.getValue();
        return story != null && story.getStory().getParticipants().contains(uid);
    }

    public boolean isMyContent() {
        StoryAllParagraph story = storyLiveData.getValue();
        return story != null && story.getParagraphs().get(story.getParagraphs().size() - 1).getAuthorId().equals(uid);
    }

    public boolean isFull() {
        StoryAllParagraph story = storyLiveData.getValue();
        return story != null && story.getStory().getParticipants().size() == story.getStory().getMaxParticipants();
    }

    public String getUID() {
        return uid;
    }


}
