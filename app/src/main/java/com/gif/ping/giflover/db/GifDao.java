package com.gif.ping.giflover.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * @created by PingYuan at 11/4/18
 * @email: husteryp@gmail.com
 * @description:
 */

@Dao
public interface GifDao {
    @Query("SELECT data from gif WHERE tag = :t and page = :p")
    String getNextPage(String t, int p);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewPage(GifEntity gifEntity);
}
