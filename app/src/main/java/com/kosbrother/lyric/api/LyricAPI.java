package com.kosbrother.lyric.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.kosbrother.lyric.entity.Album;
import com.kosbrother.lyric.entity.Singer;
import com.kosbrother.lyric.entity.SingerCategory;
import com.kosbrother.lyric.entity.SingerNews;
import com.kosbrother.lyric.entity.SingerSearchWay;
import com.kosbrother.lyric.entity.SingerSearchWayItem;
import com.kosbrother.lyric.entity.Song;
import com.kosbrother.lyric.entity.Video;
import com.kosbrother.lyric.entity.YoutubeVideo;

public class LyricAPI {
    final static String         HOST  = "http://106.187.102.167";
    public static final String  TAG   = "LyricAPI";
    public static final boolean DEBUG = true;

    // all the page start from 1 except google youtube and news api from 0

    public static ArrayList<Singer> searchSinger(String query, int page) {
        try {
            query = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }
        String message = getMessageFromServer("GET", "/api/v1/singers/search.json?keyword=" + query + "&page=" + page, null, null);
        ArrayList<Singer> singers = new ArrayList<Singer>();
        if (message == null) {
            return null;
        } else {
            return parseSingers(message, singers);
        }
    }

    public static ArrayList<Album> searchAlbum(String query, int page) {
        try {
            query = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }
        ArrayList<Album> albums = new ArrayList<Album>();
        String message = getMessageFromServer("GET", "/api/v1/albums/search.json?keyword=" + query + "&page=" + page, null, null);
        if (message == null) {
            return null;
        } else {
            return parseAlbums(message, albums);
        }
    }

    public static ArrayList<Song> searchSongLyric(String query, int page) {
        try {
            query = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }
        String message = getMessageFromServer("GET", "/api/v1/songs/search_lyric.json?keyword=" + query + "&page=" + page, null, null);
        ArrayList<Song> songs = new ArrayList<Song>();
        if (message == null) {
            return null;
        } else {
            return parseSongs(message, songs);
        }
    }

    public static ArrayList<Song> searchSongName(String query, int page) {
        if (query.length() == 1) {
            query = query + "*";
        }
        try {
            query = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }
        String message = getMessageFromServer("GET", "/api/v1/songs/search_name.json?keyword=" + query + "&page=" + page, null, null);
        ArrayList<Song> songs = new ArrayList<Song>();
        if (message == null) {
            return null;
        } else {
            return parseSongs(message, songs);
        }
    }

    public static ArrayList<Video> getHotVideos() {
        String message = getMessageFromServer("GET", "/api/v1/videos.json", null, null);
        ArrayList<Video> videos = new ArrayList<Video>();
        if (message == null) {
            return null;
        } else {
            return parseVideos(message, videos);
        }
    }

    public static ArrayList<YoutubeVideo> getYoutubeVideos(String query, int page) {
        ArrayList<YoutubeVideo> videos = new ArrayList<YoutubeVideo>();
        if (query.indexOf("(") != -1) {
            String name2 = query.substring(0, query.indexOf("("));
            query = name2;
        }
        try {
            query = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }

        String url = "http://gdata.youtube.com/feeds/api/videos?q=" + query + "&start-index=" + (page * 12 + 1)
                + "&max-results=12&v=2&alt=json&fields=entry[link/@rel='http://gdata.youtube.com/schemas/2007%23mobile']";
        String message = getMessageFromServer("GET", null, null, url);

        if (message == null) {
            return null;
        } else {
            try {
                JSONObject object = new JSONObject(message);
                JSONObject feedObject = object.getJSONObject("feed");
                JSONArray videoArray = feedObject.getJSONArray("entry");
                for (int i = 0; i < videoArray.length(); i++) {

                    String title = "null";
                    String link = "null";
                    String thumbnail = "null";

                    try {
                        title = videoArray.getJSONObject(i).getJSONObject("title").getString("$t");
                        link = videoArray.getJSONObject(i).getJSONArray("link").getJSONObject(0).getString("href");
                        thumbnail = videoArray.getJSONObject(i).getJSONObject("media$group").getJSONArray("media$thumbnail").getJSONObject(0).getString("url");
                    } catch (Exception e) {

                    }

                    int duration = 0;
                    int viewCount = 0;

                    try {
                        duration = videoArray.getJSONObject(i).getJSONObject("media$group").getJSONObject("yt$duration").getInt("seconds");
                        viewCount = videoArray.getJSONObject(i).getJSONObject("yt$statistics").getInt("viewCount");
                    } catch (Exception e) {

                    }

                    int likes = -1;

                    try {
                        likes = videoArray.getJSONObject(i).getJSONObject("yt$rating").getInt("numLikes");
                    } catch (Exception e) {
                    }

                    // int dislikes = videoArray.getJSONObject(i).getJSONObject("yt$rating").getInt("numDislikes");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    Date uploadTime = null;
                    try {
                        uploadTime = sdf.parse(videoArray.getJSONObject(i).getJSONObject("published").getString("$t"));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    YoutubeVideo video = new YoutubeVideo(title, link, thumbnail, uploadTime, viewCount, duration, likes);
                    videos.add(video);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return videos;

    }

    // perpage has 8 results
    // this page start from 0
    public static ArrayList<SingerNews> getNews(String query, int page) {
        ArrayList<SingerNews> newses = new ArrayList<SingerNews>();

        if (query.indexOf("(") != -1) {
            String name2 = query.substring(0, query.indexOf("("));
            query = name2;
        }

        try {
            query = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }

        String url = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=" + query + "&start=" + page * 8 + "&rsz=8&ned=tw";
        String message = getMessageFromServer("GET", null, null, url);

        if (message == null) {
            return null;
        } else {
            try {
                JSONObject object = new JSONObject(message);
                JSONObject feedObject = object.getJSONObject("responseData");
                JSONArray newsArray = feedObject.getJSONArray("results");
                for (int i = 0; i < newsArray.length(); i++) {

                    String title = newsArray.getJSONObject(i).getString("title");
                    String source = newsArray.getJSONObject(i).getString("publisher");

                    String pic_link = "null";
                    try {
                        pic_link = newsArray.getJSONObject(i).getJSONObject("image").getString("tbUrl");
                    } catch (Exception e) {
                    }

                    String link = newsArray.getJSONObject(i).getString("unescapedUrl");
                    SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss Z", Locale.ENGLISH);
                    Date releaseTime = null;
                    try {
                        String parseString = newsArray.getJSONObject(i).getString("publishedDate");
                        releaseTime = format.parse(parseString);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    SingerNews news = new SingerNews(title, source, pic_link, link, releaseTime);
                    newses.add(news);
                }
                return newses;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    public static Song getSong(int song_id) {
        String message = getMessageFromServer("GET", "/api/v1/songs/" + song_id + ".json", null, null);
        if (message == null) {
            return null;
        } else {
            try {
                JSONObject nObject;
                nObject = new JSONObject(message.toString());
                int id = nObject.getInt("id");
                String name = nObject.getString("name");
                String lyric = nObject.getString("lyric");
                int album_id = 0;

                if (!nObject.isNull("album_id"))
                    album_id = nObject.getInt("album_id");

                String singer = "null";
                if (nObject.has("singer_name")) {
                    singer = nObject.getString("singer_name");
                }

                return new Song(id, name, lyric, album_id, singer);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static ArrayList<Song> getAlbumSongs(int album_id) {
        String message = getMessageFromServer("GET", "/api/v1/songs/album_songs.json?album_id=" + album_id, null, null);
        ArrayList<Song> songs = new ArrayList<Song>();
        if (message == null) {
            return null;
        } else {
            return parseSongs(message, songs);
        }
    }

    public static ArrayList<Song> getSingerSongs(int singer_id, int page) {
        String message = getMessageFromServer("GET", "/api/v1/songs.json?singer_id=" + singer_id + "&page=" + page, null, null);
        ArrayList<Song> songs = new ArrayList<Song>();
        if (message == null) {
            return null;
        } else {
            return parseSongs(message, songs);
        }
    }
    
    public static ArrayList<Song> getTopListSongs(int top_list_id) {
        String message = getMessageFromServer("GET", "/api/v1/songs/top_list_songs.json?list_id=" + top_list_id , null, null);
        ArrayList<Song> songs = new ArrayList<Song>();
        if (message == null) {
            return null;
        } else {
            return parseSongs(message, songs);
        }
    }
    
    public static ArrayList<Song> getRecommendSongs(int recommend_id) {
        String message = getMessageFromServer("GET", "/api/v1/songs/recommend_songs.json?recommend_id=" + recommend_id , null, null);
        ArrayList<Song> songs = new ArrayList<Song>();
        if (message == null) {
            return null;
        } else {
            return parseSongs(message, songs);
        }
    }

    public static ArrayList<Song> getCategoryHotSongs(int singer_category_id, int page) {
        String message = getMessageFromServer("GET", "/api/v1/songs/hot_songs.json?category_id=" + singer_category_id + "&page=" + page, null, null);
        ArrayList<Song> songs = new ArrayList<Song>();
        if (message == null) {
            return null;
        } else {
            return parseSongs(message, songs);
        }
    }

    public static Singer getSinger(int singer_id) {
        String message = getMessageFromServer("GET", "/api/v1/singers/" + singer_id + ".json", null, null);
        if (message == null) {
            return null;
        } else {
            try {
                JSONObject nObject;
                nObject = new JSONObject(message.toString());
                int id = nObject.getInt("id");
                String name = nObject.getString("name");
                String description = nObject.getString("description");

                return new Singer(id, name, description);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    // public static ArrayList<Singer> getHotSingers() {
    // String message = getMessageFromServer("GET", "/api/v1/singers/all_hot_singers.json", null, null);
    // ArrayList<Singer> singers = new ArrayList<Singer>();
    // if (message == null) {
    // return null;
    // } else {
    // return parseSingersWithCategoryID(message, singers);
    // }
    // }

    public static ArrayList<Singer> getCategoryHotSingers(int singer_category_id, int page) {
        String message = getMessageFromServer("GET", "/api/v1/singers/hot_singers.json?singer_category_id=" + singer_category_id + "&page=" + page, null, null);
        ArrayList<Singer> singers = new ArrayList<Singer>();
        if (message == null) {
            return null;
        } else {
            return parseSingers(message, singers);
        }
    }

    public static ArrayList<Singer> getSingers(int singerSearchWayItemId, int page) {
        String message = getMessageFromServer("GET", "/api/v1/singers.json?serch_item_id=" + singerSearchWayItemId + "&page=" + page, null, null);
        ArrayList<Singer> singers = new ArrayList<Singer>();
        if (message == null) {
            return null;
        } else {
            return parseSingers(message, singers);
        }
    }

    public static Album getAlbum(int album_id) {
        String message = getMessageFromServer("GET", "/api/v1/albums/" + album_id + ".json", null, null);
        if (message == null) {
            return null;
        } else {

            try {
                JSONObject nObject;
                nObject = new JSONObject(message.toString());
                int id = nObject.getInt("id");
                String name = nObject.getString("name");
                String description = nObject.getString("description");
                String release = nObject.getString("release_time");
                Date release_time = null;
                if (!release.equals("null")) {
                    SimpleDateFormat createFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                    release_time = createFormatter.parse(release);
                }

                String singer = "null";
                if (nObject.has("singer_name")) {
                    singer = nObject.getString("singer_name");
                }

                return new Album(id, name, release_time, description, singer);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static ArrayList<Album> getCategoryHotAlbums(int category_id) {
        ArrayList<Album> albums = new ArrayList<Album>();
        String message = getMessageFromServer("GET", "/api/v1/albums/hot_albums.json?category_id=" + category_id, null, null);
        if (message == null) {
            return null;
        } else {
            return parseAlbums(message, albums);
        }
    }

    public static ArrayList<Album> getSingerAlbums(int singer_id) {
        ArrayList<Album> albums = new ArrayList<Album>();
        String message = getMessageFromServer("GET", "/api/v1/albums.json?singer_id=" + singer_id, null, null);
        if (message == null) {
            return null;
        } else {
            return parseAlbums(message, albums);
        }
    }

    public static ArrayList<Album> getNewAlbums(int page) {
        ArrayList<Album> albums = new ArrayList<Album>();
        String message = getMessageFromServer("GET", "/api/v1/albums/new_albums.json?page=" + page, null, null);
        if (message == null) {
            return null;
        } else {
            return parseAlbums(message, albums);
        }
    }

    public static ArrayList<SingerCategory> getSingerCategories() {
        return SingerCategory.getCategories();
    }

    public static ArrayList<SingerSearchWay> getSingerCategoryWays(int singerCategoryId) {
        return SingerSearchWay.getSingerCategoryWays(singerCategoryId);
    }

    public static ArrayList<SingerSearchWayItem> getSingerSearchWayItems(int singerSearchWayId) {
        return SingerSearchWayItem.getSingerSearchWayItems(singerSearchWayId);
    }

    private static ArrayList<Video> parseVideos(String message, ArrayList<Video> videos) {
        try {
            JSONArray jArray;
            jArray = new JSONArray(message.toString());
            for (int i = 0; i < jArray.length(); i++) {

                String title = jArray.getJSONObject(i).getString("title");
                String youtube_id = jArray.getJSONObject(i).getString("youtube_id");
                Video video = new Video(title, youtube_id);
                videos.add(video);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return videos;
    }

    private static ArrayList<Song> parseSongs(String message, ArrayList<Song> songs) {
        try {
            JSONArray jArray;
            jArray = new JSONArray(message.toString());
            for (int i = 0; i < jArray.length(); i++) {

                int id = jArray.getJSONObject(i).getInt("id");
                String name = jArray.getJSONObject(i).getString("name");
                int album_id = 0;

                if (!jArray.getJSONObject(i).isNull("album_id"))
                    album_id = jArray.getJSONObject(i).getInt("album_id");

                String singer = "null";
                if (jArray.getJSONObject(i).has("singer_name")) {
                    singer = jArray.getJSONObject(i).getString("singer_name");
                }

                songs.add(new Song(id, name, "null", album_id, singer));

            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return songs;
    }

    private static ArrayList<Singer> parseSingersWithCategoryID(String message, ArrayList<Singer> singers) {
        try {
            JSONArray jArray;
            jArray = new JSONArray(message.toString());
            for (int i = 0; i < jArray.length(); i++) {

                int id = jArray.getJSONObject(i).getInt("id");
                String name = jArray.getJSONObject(i).getString("name");
                int categor_id = jArray.getJSONObject(i).getInt("singer_category_id");
                singers.add(new Singer(id, name, "null", categor_id));

            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return singers;
    }

    private static ArrayList<Singer> parseSingers(String message, ArrayList<Singer> singers) {
        try {
            JSONArray jArray;
            jArray = new JSONArray(message.toString());
            for (int i = 0; i < jArray.length(); i++) {

                int id = jArray.getJSONObject(i).getInt("id");
                String name = jArray.getJSONObject(i).getString("name");
                singers.add(new Singer(id, name, "null"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return singers;
    }

    private static ArrayList<Album> parseAlbums(String message, ArrayList<Album> albums) {
        try {
            JSONArray jArray;
            jArray = new JSONArray(message.toString());
            for (int i = 0; i < jArray.length(); i++) {

                int id = jArray.getJSONObject(i).getInt("id");
                String name = jArray.getJSONObject(i).getString("name");
                String release = jArray.getJSONObject(i).getString("release_time");

                String singer = "null";
                if (jArray.getJSONObject(i).has("singer_name")) {
                    singer = jArray.getJSONObject(i).getString("singer_name");
                }

                if (!release.equals("null")) {
                    SimpleDateFormat createFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                    Date release_time = createFormatter.parse(release);
                    Album album = new Album(id, name, release_time, "null", singer);
                    albums.add(album);
                } else {
                    Album album = new Album(id, name, null, "null", singer);
                    albums.add(album);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return albums;
    }

    public static String getMessageFromServer(String requestMethod, String apiPath, JSONObject json, String apiUrl) {
        URL url;
        try {
            if (apiUrl != null)
                url = new URL(apiUrl);
            else
                url = new URL(HOST + apiPath);

            if (DEBUG)
                Log.d(TAG, "URL: " + url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);

            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            if (requestMethod.equalsIgnoreCase("POST"))
                connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();

            if (requestMethod.equalsIgnoreCase("POST")) {
                OutputStream outputStream;

                outputStream = connection.getOutputStream();
                if (DEBUG)
                    Log.d("post message", json.toString());

                outputStream.write(json.toString().getBytes());
                outputStream.flush();
                outputStream.close();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder lines = new StringBuilder();
            ;
            String tempStr;

            while ((tempStr = reader.readLine()) != null) {
                lines = lines.append(tempStr);
            }
            if (DEBUG)
                Log.d("MOVIE_API", lines.toString());

            reader.close();
            connection.disconnect();

            return lines.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

	public static boolean sendRegistrationId(String regid,String deviceId) {
		try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			String url = HOST + "/api/v1/users.json?regid="+regid+"&device_id="+deviceId;					
			if(DEBUG)
				Log.d(TAG, "URL : " + url);

			HttpPost httpPost = new HttpPost(url);
			HttpResponse response = httpClient.execute(httpPost);

			StatusLine statusLine =  response.getStatusLine();
			if (statusLine.getStatusCode() == 200){
				return true;
			}else{
				return false;
			}
		} 
	    catch (Exception e) {
			return false;
		} 
	}
	
	public static boolean sendCollectSongs(String songs,String deviceId){
    	try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			String url = HOST + "/api/v1/users/update_collected_songs.json?songs="+songs+"&device_id="+deviceId;						
			if(DEBUG)
				Log.d(TAG, "URL : " + url);

			HttpPut httpPut = new HttpPut(url);
			HttpResponse response = httpClient.execute(httpPut);

			StatusLine statusLine =  response.getStatusLine();
			if (statusLine.getStatusCode() == 200){
				return true;
			}else{
				return false;
			}
		} 
	    catch (Exception e) {
			return false;
		} 

    }
	
	public static boolean sendCollectSingers(String singers,String deviceId){
    	try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			String url = HOST + "/api/v1/users/update_collected_singers.json?singers="+singers+"&device_id="+deviceId;						
			if(DEBUG)
				Log.d(TAG, "URL : " + url);

			HttpPut httpPut = new HttpPut(url);
			HttpResponse response = httpClient.execute(httpPut);

			StatusLine statusLine =  response.getStatusLine();
			if (statusLine.getStatusCode() == 200){
				return true;
			}else{
				return false;
			}
		} 
	    catch (Exception e) {
			return false;
		} 

    }
	
	public static boolean sendCollectAlbums(String albums,String deviceId){
    	try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			String url = HOST + "/api/v1/users/update_collected_albums.json?albums="+albums+"&device_id="+deviceId;						
			if(DEBUG)
				Log.d(TAG, "URL : " + url);

			HttpPut httpPut = new HttpPut(url);
			HttpResponse response = httpClient.execute(httpPut);

			StatusLine statusLine =  response.getStatusLine();
			if (statusLine.getStatusCode() == 200){
				return true;
			}else{
				return false;
			}
		} 
	    catch (Exception e) {
			return false;
		} 

    }
	public static boolean sendSong(int song,String deviceId){
    	try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			String url = HOST + "/api/v1/users/update_looked_songs.json?device_id="+deviceId+"&song="+song;						
			if(DEBUG)
				Log.d(TAG, "URL : " + url);

			HttpPut httpPut = new HttpPut(url);
			HttpResponse response = httpClient.execute(httpPut);

			StatusLine statusLine =  response.getStatusLine();
			if (statusLine.getStatusCode() == 200){
				return true;
			}else{
				return false;
			}
		} 
	    catch (Exception e) {
			return false;
		} 

    }
	
	public static boolean sendAlbum(int album,String deviceId){
    	try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			String url = HOST + "/api/v1/users/update_looked_albums.json?device_id="+deviceId+"&album="+album;						
			if(DEBUG)
				Log.d(TAG, "URL : " + url);

			HttpPut httpPut = new HttpPut(url);
			HttpResponse response = httpClient.execute(httpPut);

			StatusLine statusLine =  response.getStatusLine();
			if (statusLine.getStatusCode() == 200){
				return true;
			}else{
				return false;
			}
		} 
	    catch (Exception e) {
			return false;
		} 

    }
	
	public static boolean sendSinger(int singer,String deviceId){
    	try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			String url = HOST + "/api/v1/users/update_looked_singers.json?device_id="+deviceId+"&singer="+singer;						
			if(DEBUG)
				Log.d(TAG, "URL : " + url);

			HttpPut httpPut = new HttpPut(url);
			HttpResponse response = httpClient.execute(httpPut);

			StatusLine statusLine =  response.getStatusLine();
			if (statusLine.getStatusCode() == 200){
				return true;
			}else{
				return false;
			}
		} 
	    catch (Exception e) {
			return false;
		} 

    }

}
