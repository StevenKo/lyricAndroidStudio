package com.kosbrother.lyric.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kosbrother.lyric.entity.Album;
import com.kosbrother.lyric.entity.Singer;
import com.kosbrother.lyric.entity.Song;

public class SQLiteLyric extends SQLiteOpenHelper {

    private static final String DB_NAME          = "koslyric.sqlite"; // 資料庫名稱
    private static final int    DATABASE_VERSION = 1;                // 資料庫版本
    private SQLiteDatabase      db;
    private final Context       ctx;

    // Define database schema
    public interface AlbumSchema {
        String TABLE_NAME   = "albums";
        String ID           = "id";
        String NAME         = "name";
        String RELEASE_TIME = "release_time";
        String DESCRIPTION  = "description";
        String SINGER_NAME  = "singer_name";
    }

    public interface SongSchema {
        String TABLE_NAME  = "songs";
        String ID          = "id";
        String NAME        = "name";
        String LYRIC       = "lyric";
        String ALBUM_ID    = "album_id";
        String SINGER_NAME = "singer_name";
    }

    public interface SingerSchema {
        String TABLE_NAME  = "singers";
        String ID          = "id";
        String NAME        = "name";
        String DESCRIPTION = "description";
    }

    public SQLiteLyric(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        ctx = context;

        if (db == null)
            db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + AlbumSchema.TABLE_NAME + " (" + AlbumSchema.ID + " INTEGER PRIMARY KEY" + "," + AlbumSchema.NAME
                + " TEXT NOT NULL" + "," + AlbumSchema.RELEASE_TIME + " TEXT NOT NULL" + "," + AlbumSchema.DESCRIPTION + " TEXT NOT NULL" + ","
                + AlbumSchema.SINGER_NAME + " TEXT NOT NULL" + ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SongSchema.TABLE_NAME + " (" + SongSchema.ID + " INTEGER PRIMARY KEY" + "," + SongSchema.NAME
                + " TEXT NOT NULL" + "," + SongSchema.LYRIC + " TEXT NOT NULL" + "," + SongSchema.ALBUM_ID + " INTEGER NOT NULL" + "," + SongSchema.SINGER_NAME
                + " TEXT NOT NULL" + ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SingerSchema.TABLE_NAME + " (" + SingerSchema.ID + " INTEGER PRIMARY KEY" + "," + SingerSchema.NAME
                + " TEXT NOT NULL" + "," + SingerSchema.DESCRIPTION + " TEXT NOT NULL" + ");");

    }

    public boolean deleteSong(Song song) {
        Cursor cursor = db.rawQuery("DELETE FROM songs WHERE `songs`.`id` = ?", new String[] { song.getId() + "" });
        cursor.moveToFirst();
        cursor.close();
        return true;
    }

    public boolean deleteAlbum(Album album) {
        Cursor cursor = db.rawQuery("DELETE FROM albums WHERE `albums`.`id` = ?", new String[] { album.getId() + "" });
        cursor.moveToFirst();
        cursor.close();
        return true;
    }

    public boolean deleteSinger(Singer singer) {
        Cursor cursor = db.rawQuery("DELETE FROM singers WHERE `singers`.`id` = ?", new String[] { singer.getId() + "" });
        cursor.moveToFirst();
        cursor.close();
        return true;
    }

    public long insertSong(Song song) {
        ContentValues args = new ContentValues();
        args.put(SongSchema.ID, song.getId());
        args.put(SongSchema.ALBUM_ID, song.getAlbumId());
        args.put(SongSchema.LYRIC, song.getLyric());
        args.put(SongSchema.NAME, song.getName());
        args.put(SongSchema.SINGER_NAME, song.getSingerName());
        return db.insert(SongSchema.TABLE_NAME, null, args);
    }

    public long insertAlbum(Album album) {
        ContentValues args = new ContentValues();
        args.put(AlbumSchema.ID, album.getId());
        args.put(AlbumSchema.DESCRIPTION, album.getDescription());
        args.put(AlbumSchema.NAME, album.getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (album.getDate() != null)
            args.put(AlbumSchema.RELEASE_TIME, dateFormat.format(album.getDate()));
        else
            args.put(AlbumSchema.RELEASE_TIME, "null");
        args.put(AlbumSchema.SINGER_NAME, album.getSingerName());

        return db.insert(AlbumSchema.TABLE_NAME, null, args);
    }

    public long insertSinger(Singer singer) {
        ContentValues args = new ContentValues();
        args.put(SingerSchema.ID, singer.getId());
        args.put(SingerSchema.DESCRIPTION, singer.getDescription());
        args.put(SingerSchema.NAME, singer.getName());
        return db.insert(SingerSchema.TABLE_NAME, null, args);
    }

    public ArrayList<Song> getAllSongs() {
        Cursor cursor = null;
        ArrayList<Song> songs = new ArrayList<Song>();
        cursor = db.rawQuery("SELECT * FROM " + SongSchema.TABLE_NAME + " ORDER BY id DESC", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String lyric = cursor.getString(2);
            int album_id = cursor.getInt(3);
            String singer_name = cursor.getString(4);

            Song song = new Song(id, name, lyric, album_id, singer_name);
            songs.add(song);
        }
        return songs;
    }

    public ArrayList<Album> getAllAlbums() {
        Cursor cursor = null;
        ArrayList<Album> albums = new ArrayList<Album>();
        cursor = db.rawQuery("SELECT * FROM " + AlbumSchema.TABLE_NAME + " ORDER BY id DESC", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String release_time = cursor.getString(2);
            Date release = null;
            if (!release_time.equals("null")) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    release = dateFormat.parse(release_time);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            String description = cursor.getString(3);
            String singer_name = cursor.getString(4);

            Album album = new Album(id, name, release, description, singer_name);
            albums.add(album);
        }
        return albums;
    }

    public ArrayList<Singer> getAllSingers() {
        Cursor cursor = null;
        ArrayList<Singer> singers = new ArrayList<Singer>();
        cursor = db.rawQuery("SELECT * FROM " + SingerSchema.TABLE_NAME + " ORDER BY id DESC", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            singers.add(new Singer(id, name, description));
        }
        return singers;
    }

    public Boolean isSingerCollected(int singer_id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + SingerSchema.TABLE_NAME + " where id = " + singer_id, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public Boolean isSongCollected(int song_id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + SongSchema.TABLE_NAME + " where id = " + song_id, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public Boolean isAlbumCollected(int album_id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + AlbumSchema.TABLE_NAME + " where id = " + album_id, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
