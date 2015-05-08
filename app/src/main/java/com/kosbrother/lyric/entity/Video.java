package com.kosbrother.lyric.entity;

public class Video {
    String title;
    String youtube_id;

    public Video() {
        this("::首播::白安Ann[是什麼讓我遇見這樣的你] MV官方完整版", "aVmZpcrQBU4");
    }

    public Video(String title, String youtube_id) {
        this.title = title;
        this.youtube_id = youtube_id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return "http://img.youtube.com/vi/" + youtube_id + "/1.jpg";
    }

    public String getYoutubeLink() {
        return "http://www.youtube.com/watch?v=" + youtube_id;
    }
}
