package com.gif.ping.giflover.db;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * @created by PingYuan at 11/4/18
 * @email: husteryp@gmail.com
 * @description:
 */
public class GifBaseHolder {
    private static GifDataBase INSTANCE;
    private static final String DATABASE_NAME = "gif_db";

    public static GifDataBase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (GifBaseHolder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), GifDataBase
                                    .class,

                            DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
