package com.gif.ping.giflover.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @created by PingYuan at 11/4/18
 * @email: husteryp@gmail.com
 * @description:
 */

@Entity(tableName = "gif")
public class GifEntity {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    @ColumnInfo
    private String tag;
    @ColumnInfo
    private int page;
    @ColumnInfo
    private String data;

    public GifEntity() {
    }

    public GifEntity(String tag, int page, String data) {
        this.tag = tag;
        this.page = page;
        this.data = data;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
