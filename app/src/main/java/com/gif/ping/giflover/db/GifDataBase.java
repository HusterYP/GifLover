package com.gif.ping.giflover.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * @created by PingYuan at 11/4/18
 * @email: husteryp@gmail.com
 * @description:
 */

@Database(entities = GifEntity.class, version = 1, exportSchema = false)
public abstract class GifDataBase extends RoomDatabase{
    public abstract GifDao getDao();
}
