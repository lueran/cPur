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

package com.cpur.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.cpur.data.Story;
import com.cpur.data.StoryDao;
import com.cpur.data.StoryDataSource;
import com.cpur.utils.AppExecutors;



/**
 * Concrete implementation of a data source as a db.
 */
public class StoryLocalDataSource implements StoryDataSource {

    private static volatile StoryLocalDataSource INSTANCE;

    private StoryDao mStoryDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private StoryLocalDataSource(@NonNull AppExecutors appExecutors,
                                 @NonNull StoryDao storyDao) {
        mAppExecutors = appExecutors;
        mStoryDao = storyDao;
    }

    public static StoryLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
            @NonNull StoryDao storyDao) {
        if (INSTANCE == null) {
            synchronized (StoryLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StoryLocalDataSource(appExecutors, storyDao);
                }
            }
        }
        return INSTANCE;
    }



    @Override
    public void getStory(@NonNull final String storyId, @NonNull final GetStoryCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Story story = mStoryDao.loadStoryById(storyId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (story != null) {
                            callback.onStoryLoaded(story);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveStory(@NonNull final Story story) {
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mStoryDao.insertAll(story);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
    }
}
