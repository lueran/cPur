/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cpur.data;

import android.support.annotation.NonNull;


import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Main entry point for accessing tasks data.
 */
public interface StoryDataSource {

    void createStory(Story story, Paragraph paragraph);

    interface GetStoryCallback<T> {
        void  onComplete(T t);
        void  onError();
    }


    void getStory(@NonNull String storyId, @NonNull GetStoryCallback<StoryAllParagraph> callback);

    void saveStory(@NonNull StoryAllParagraph story);

    void getUserStories(@NonNull GetStoryCallback<List<StoryAllParagraph>> callback);

    void getAllStories(@NonNull GetStoryCallback<List<StoryAllParagraph>> callback);

    void saveStories(@NonNull List<StoryAllParagraph> stories);

    default public String getUID(){
        return FirebaseAuth.getInstance().getUid();
    }
}
