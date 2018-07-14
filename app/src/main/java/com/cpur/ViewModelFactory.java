/*
 *  Copyright 2017 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cpur;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.cpur.data.StoryRepository;

/**
 * A creator is used to inject the product ID into the ViewModel
 * <p>
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final Application mApplication;

    private StoryRepository storyRepository;

    public static void init(Application application, StoryRepository storyRepository) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application, storyRepository);
                }
            }
        }
    }

    public static ViewModelFactory getInstance() {
        if (INSTANCE == null) {
//            throw new Exception("must call init first");
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private ViewModelFactory(Application application, StoryRepository repository) {
        mApplication = application;
        storyRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StoryViewModel.class)) {
            //noinspection unchecked
            return (T) new StoryViewModel(mApplication, storyRepository);
        } else if (modelClass.isAssignableFrom(CreateStoryViewModel.class)) {
            //noinspection unchecked
            return (T) new CreateStoryViewModel(mApplication, storyRepository);
        } else if (modelClass.isAssignableFrom(StoryListViewModel.class)){
            //noinspection unchecked
            return (T) new StoryListViewModel(mApplication, storyRepository);

        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
