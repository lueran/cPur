package com.cpur.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

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