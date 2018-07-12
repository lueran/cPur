package com.cpur.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ParagraphDao {
    @Query("SELECT * FROM paragraph")
    List<Paragraph> getAll();

    @Query("SELECT * FROM paragraph WHERE uid IN (:paragraphIds)")
    List<Paragraph> loadAllByIds(int[] paragraphIds);

    /*@Query("SELECT * FROM user WHERE username LIKE :username")
    User findByName(String username);*/

    @Insert
    void insertAll(Paragraph... paragraphs);

    @Delete
    void delete(Paragraph paragraph);
}