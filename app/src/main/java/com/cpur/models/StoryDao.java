package com.cpur.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StoryDao {
    @Query("SELECT * FROM story")
    List<Story> getAll();

    @Query("SELECT * FROM story WHERE uid IN (:storyIds)")
    List<User> loadAllByIds(int[] storyIds);

    /*@Query("SELECT * FROM user WHERE username LIKE :username")
    User findByName(String username);*/

    @Insert
    void insertAll(Story... stories);

    @Delete
    void delete(Story story);
}