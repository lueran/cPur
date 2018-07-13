package com;

import android.app.Application;

import com.cpur.ViewModelFactory;
import com.cpur.data.StoryRepository;
import com.cpur.data.source.local.StoryLocalDataSource;
import com.cpur.data.source.remote.StoryRemoteDataSource;
import com.cpur.db.AppDatabase;
import com.cpur.utils.AppExecutors;
import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ViewModelFactory.init(this,
                StoryRepository.getInstance(
                        new StoryRemoteDataSource(FirebaseDatabase.getInstance().getReference()),
                StoryLocalDataSource.getInstance(new AppExecutors(), AppDatabase.getInstance(this).storyDao())));
    }
}
