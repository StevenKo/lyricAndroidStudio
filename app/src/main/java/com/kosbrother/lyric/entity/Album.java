package com.kosbrother.lyric.entity;

import java.util.Date;

public class Album {

    int    id;
    String name;
    Date   release_time;
    String description;
    String singer_name;

    public Album() {
        this(1, "", new Date(), "", "");
    }

    public Album(int id, String name, Date date, String description, String singer_name) {
        this.id = id;
        this.name = name;
        this.release_time = date;
        this.description = description;
        this.singer_name = singer_name;
    }

    public String getSingerName() {
        return singer_name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return release_time;
    }

    public String getDescription() {
        return description;
    }

}
