package com.cpur.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.cpur.data.Paragraph;
import com.cpur.data.ParagraphDao;
import com.cpur.data.Story;
import com.cpur.data.StoryDao;

@Database(entities = {Story.class, Paragraph.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
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
