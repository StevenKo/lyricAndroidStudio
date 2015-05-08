package com.kosbrother.lyric.entity;

public class Singer {

    int    id;
    String name;
    String description;
    int    singer_category_id;

    public Singer() {
        this(1, "", "");
    }

    public Singer(int i, String string, String string2) {
        this.id = i;
        this.name = string;
        this.description = string2;
    }

    public Singer(int i, String string, String string2, int category_id) {
        this.id = i;
        this.name = string;
        this.description = string2;
        this.singer_category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSingerCategoryId() {
        return singer_category_id;
    }

}
