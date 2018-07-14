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

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;


/**
 * Concrete implementation to load Storys from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 * <p>
 * //TODO: Implement this class using LiveData.
 */
public class StoryRepository {

    private volatile static StoryRepository INSTANCE = null;

    private final StoryDataSource mStoryRemoteDataSource;

    private final StoryDataSource mStoryLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Story> mCachedStories;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private StoryRepository(@NonNull StoryDataSource tasksRemoteDataSource,
                            @NonNull StoryDataSource tasksLocalDataSource) {
        mStoryRemoteDataSource = tasksRemoteDataSource;
        mStoryLocalDataSource = tasksLocalDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param tasksRemoteDataSource the backend data source
     * @param tasksLocalDataSource  the device storage data source
     * @return the {@link StoryRepository} instance
     */
    public static StoryRepository getInstance(StoryDataSource tasksRemoteDataSource,
                                              StoryDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            synchronized (StoryRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StoryRepository(tasksRemoteDataSource, tasksLocalDataSource);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(StoryDataSource, StoryDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }


    public void saveStory(@NonNull StoryAllParagraph story) {
        mStoryRemoteDataSource.saveStory(story);

        // Do in memory cache update to keep the app UI up to date
    }


   /* @Override
    public void getStory(@NonNull final String storyId, @NonNull final GetStoryCallback callback) {

        Story cachedTask = getStoryWithId(storyId);

        // Respond immediately with cache if available
        if (cachedTask != null) {
            callback.onStoryLoaded(cachedTask);
            return;
        }

        // Is the task in the local data source? If not, query the network.
        mStoryLocalDataSource.getStory(storyId, new GetStoryCallback() {
            @Override
            public void onStoryLoaded(Story story) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedStories == null) {
                    mCachedStories = new LinkedHashMap<>();
                }
                mCachedStories.put(story.getUid(), story);

                callback.onStoryLoaded(story);
            }

            @Override
            public void onDataNotAvailable() {
                mStoryRemoteDataSource.getStory(storyId, new GetStoryCallback() {
                    @Override
                    public void onStoryLoaded(Story story) {
                        if (story == null) {
                            onDataNotAvailable();
                            return;
                        }
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedStories == null) {
                            mCachedStories = new LinkedHashMap<>();
                        }
                        mCachedStories.put(story.getUid(), story);

                        callback.onStoryLoaded(story);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }*/

    public LiveData<StoryAllParagraph> getStory(@NonNull final String storyId) {

        return new MutableLiveData<StoryAllParagraph>() {
            @Override
            protected void onActive() {
                super.onActive();

                mStoryLocalDataSource.getStory(storyId, new StoryDataSource.GetStoryCallback<StoryAllParagraph>() {

                    @Override
                    public void onComplete(StoryAllParagraph story) {
                        setValue(story);

                        mStoryRemoteDataSource.getStory(storyId, new StoryDataSource.GetStoryCallback<StoryAllParagraph>() {
                            @Override
                            public void onComplete(StoryAllParagraph story) {
                                setValue(story);
                                mStoryLocalDataSource.saveStory(story);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }

                    @Override
                    public void onError() {

                        mStoryRemoteDataSource.getStory(storyId, new StoryDataSource.GetStoryCallback<StoryAllParagraph>() {
                            @Override
                            public void onComplete(StoryAllParagraph story) {
                                setValue(story);
                                mStoryLocalDataSource.saveStory(story);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                });
            }

            @Override
            protected void onInactive() {
                super.onInactive();
            }
        };
    }

    public MutableLiveData<List<StoryAllParagraph>> getDiscoverStoriesLiveData() {

        return new MutableLiveData<List<StoryAllParagraph>>() {
            @Override
            protected void onActive() {
                super.onActive();

                mStoryLocalDataSource.getAllStories(new StoryDataSource.GetStoryCallback<List<StoryAllParagraph>>() {

                    @Override
                    public void onComplete(List<StoryAllParagraph> storyAllParagraphs) {
                        setValue(storyAllParagraphs);

                        mStoryRemoteDataSource.getAllStories(new StoryDataSource.GetStoryCallback<List<StoryAllParagraph>>() {
                            @Override
                            public void onComplete(List<StoryAllParagraph> stories) {
                                setValue(stories);
                                mStoryLocalDataSource.saveStories(stories);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }

                    @Override
                    public void onError() {

                        mStoryRemoteDataSource.getAllStories(new StoryDataSource.GetStoryCallback<List<StoryAllParagraph>>() {
                            @Override
                            public void onComplete(List<StoryAllParagraph> stories) {
                                setValue(stories);
                                mStoryLocalDataSource.saveStories(stories);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                });
            }

            @Override
            protected void onInactive() {
                super.onInactive();
            }
        };
    }

    public MutableLiveData<List<StoryAllParagraph>> getUserStoriesLiveData() {

        return new MutableLiveData<List<StoryAllParagraph>>() {
            @Override
            protected void onActive() {
                super.onActive();

                mStoryLocalDataSource.getUserStories(new StoryDataSource.GetStoryCallback<List<StoryAllParagraph>>() {

                    @Override
                    public void onComplete(List<StoryAllParagraph> storyAllParagraphs) {
                        setValue(storyAllParagraphs);

                        mStoryRemoteDataSource.getUserStories(new StoryDataSource.GetStoryCallback<List<StoryAllParagraph>>() {
                            @Override
                            public void onComplete(List<StoryAllParagraph> stories) {
                                setValue(stories);
                                mStoryLocalDataSource.saveStories(stories);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }

                    @Override
                    public void onError() {

                        mStoryRemoteDataSource.getUserStories(new StoryDataSource.GetStoryCallback<List<StoryAllParagraph>>() {
                            @Override
                            public void onComplete(List<StoryAllParagraph> stories) {
                                setValue(stories);
                                mStoryLocalDataSource.saveStories(stories);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                });
            }

            @Override
            protected void onInactive() {
                super.onInactive();
            }
        };
    }


    private Story getStoryWithId(@NonNull String id) {
        if (mCachedStories == null || mCachedStories.isEmpty()) {
            return null;
        } else {
            return mCachedStories.get(id);
        }
    }

    public void createStory(Story story, Paragraph paragraph) {
        mStoryRemoteDataSource.createStory(story, paragraph);
    }
}
