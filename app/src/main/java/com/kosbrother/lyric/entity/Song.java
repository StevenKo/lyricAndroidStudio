package com.kosbrother.lyric.entity;

public class Song {
    int    id;
    String name;
    String lyric;
    int    album_id;
    String singer_name;

    public Song() {
        this(1, "", "", 1, "");
    }

    public Song(int id, String name, String lyric, int album_id, String singer_name) {
        this.id = id;
        this.name = name;
        this.album_id = album_id;
        this.lyric = lyric;
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

    public String getLyric() {
        return lyric;
    }

    public int getAlbumId() {
        return album_id;
    }
}
