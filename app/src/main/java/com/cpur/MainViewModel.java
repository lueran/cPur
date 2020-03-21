package com.cpur;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cpur.data.StoryAllParagraph;
import com.cpur.data.StoryRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final StoryRepository storyRepo;

    public MainViewModel(Application mApplication, StoryRepository storyRepository) {
        this.storyRepo = storyRepository;
    }

    public LiveData<List<StoryAllParagraph>> getDiscoverStories() {
        return storyRepo.getDiscoverStoriesLiveData();
    }

    public LiveData<List<StoryAllParagraph>> getUserStories() {
        return storyRepo.getUserStoriesLiveData();
    }

    public LiveData<List<StoryAllParagraph>> getStories(int type) {
        if (type == 0){
            return getDiscoverStories();
        }else{
            return getUserStories();
        }
    }
}
