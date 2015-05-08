package com.kosbrother.lyric.entity;

import java.util.Date;

public class SingerNews {

    String title;
    String source;
    String pic_link;
    String link;
    Date   release_time;

    public SingerNews() {
        this("", "", "", "", new Date());
    }

    public SingerNews(String title, String source, String pic_link, String link, Date release_time) {
        this.title = title;
        this.source = source;
        this.pic_link = pic_link;
        this.link = link;
        this.release_time = release_time;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getPicLink() {
        return pic_link;
    }

    public String getLink() {
        return link;
    }

    public Date getReleateTime() {
        return release_time;
    }

}
