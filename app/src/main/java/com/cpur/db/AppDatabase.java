package com.cpur.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.cpur.data.Paragraph;
import com.cpur.data.ParagraphDao;
import com.cpur.data.Story;
import com.cpur.data.StoryDao;
import com.cpur.data.User;
import com.cpur.data.UserDao;

@Database(entities = {User.class, Story.class, Paragraph.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract StoryDao storyDao();
    public abstract ParagraphDao paragraphDao();

    private static AppDatabase INSTANCE;


    private static final Object sLock = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "cPur.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}
