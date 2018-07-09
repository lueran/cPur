package com.cpur;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.cpur.models.Story;

public class StoryViewModel extends ViewModel {

    private LiveData<Story> storyLiveData;

    public Story getStory(String storyId) {
        return new Story();
    }
}
