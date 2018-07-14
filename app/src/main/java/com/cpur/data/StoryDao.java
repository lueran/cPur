package com.cpur.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StoryDao {
    @Query("SELECT * FROM Story")
    LiveData<List<Story>> getAll();

    @Transaction
    @Query("SELECT * FROM Story WHERE participants LIKE :search")
    List<StoryAllParagraph> getUserStories(String search);

    @Query("SELECT * FROM Story WHERE uid = (:storyId)")
    LiveData<Story> getStoryById(String storyId);

    @Transaction
    @Query("SELECT * FROM Story")
    List<StoryAllParagraph> getStories();

    @Transaction
    @Query("SELECT * FROM Story WHERE uid = (:storyId)")
    StoryAllParagraph loadStoryAllParagraphById(String storyId);

    @Transaction
    @Insert(onConflict = REPLACE)
    void insertAllStoriesParagraphs(Story story, List<Paragraph> paragraphs);

    @Delete
    void delete(Story story);
}