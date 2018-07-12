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


import java.util.List;

/**
 * Main entry point for accessing tasks data.
 */
public interface StoryDataSource {

    interface LoadStoryCallback {

        void onStoryLoaded(List<Story> tasks);

        void onDataNotAvailable();
    }

    interface GetStoryCallback {

        void onStoryLoaded(Story task);

        void onDataNotAvailable();
    }


    void getStory(@NonNull String storyId, @NonNull GetStoryCallback callback);

    void saveStory(@NonNull Story story);
}
