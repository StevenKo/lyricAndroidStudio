package com.kosbrother.lyric;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.kosbrother.lyric.api.LyricAPI;
import com.kosbrother.lyric.db.SQLiteLyric;
import com.kosbrother.lyric.entity.Album;
import com.kosbrother.lyric.entity.Song;
import com.taiwan.imageload.ListSongAdapter;

import java.util.ArrayList;

public class AlbumActivity extends Activity {

    private LinearLayout        progressLayout;
    private LinearLayout        reloadLayout;
    private LoadMoreListView    myList;
    private Button              buttonReload;
    private ListSongAdapter     mdapter;
    private ArrayList<Song>     mSongs;

    private Bundle              mBundle;
    private int                 albumId;
    private String              albumName;
    private Album               mAlbum;

    private final int           ID_INTRODUCE   = 666666;
    private final int           ID_COLLECT     = 777777;
    private AlertDialog.Builder aboutUsDialog;
    private AlertDialog.Builder introduceDialog;
    private ProgressDialog      progressDialog = null;
    private String              singerNmae;
    
    private int sdkVersion;
    private SQLiteLyric db;
	private MenuItem itemCollect;
	
	private final String  adWhirlKey = Setting.adwhirlKey;

    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadmore);
        db = new SQLiteLyric(AlbumActivity.this);
        
        mBundle = this.getIntent().getExtras();
        albumId = mBundle.getInt("AlbumId");
        albumName = mBundle.getString("AlbumName");
        singerNmae = mBundle.getString("SingerNmae");

        mAlbum = new Album(albumId, albumName, null, "null", singerNmae);

        if (albumName != null && !albumName.equals("null") && !albumName.equals("")) {
            setTitle(albumName);
        }
        
        sdkVersion = android.os.Build.VERSION.SDK_INT; 
        if(sdkVersion > 10){
        	getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressLayout = (LinearLayout) findViewById(R.id.layout_progress);
        reloadLayout = (LinearLayout) findViewById(R.id.layout_reload);
        buttonReload = (Button) findViewById(R.id.button_reload);
        myList = (LoadMoreListView) findViewById(R.id.news_list);
        myList.setOnLoadMoreListener(new OnLoadMoreListener() {
            public void onLoadMore() {
                // Do the work to load more items at the end of list
                myList.onLoadMoreComplete();
            }
        });

        buttonReload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                progressLayout.setVisibility(View.VISIBLE);
                reloadLayout.setVisibility(View.GONE);
                new DownloadChannelsTask().execute();
            }
        });

        new DownloadChannelsTask().execute();
        new UpdateServerCollectTask().execute();

        setIntroduceDialog();
        setAboutUsDialog();
        
        AdViewUtil.setAdView((LinearLayout) findViewById(R.id.adonView), this);
    }
    
    private class UpdateServerCollectTask extends AsyncTask {

		@Override
		protected Object doInBackground(Object... params) {
			
			LyricAPI.sendAlbum(mAlbum.getId(), Settings.Secure.getString(AlbumActivity.this.getContentResolver(),Settings.Secure.ANDROID_ID));
			return null;
		}
    	
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        
        if (sdkVersion > 10){ 
        	menu.add(0, ID_INTRODUCE, 0, getResources().getString(R.string.menu_introduce)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);       	
        	if(db.isAlbumCollected(mAlbum.getId())){
    			itemCollect = menu.add(0, ID_COLLECT, 1, getResources().getString(R.string.menu_collect_cancel));
    			itemCollect.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    	}else{
	    		itemCollect = menu.add(0, ID_COLLECT, 1, getResources().getString(R.string.menu_collect));
	    		itemCollect.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    	}
        }else{
        	menu.add(0, ID_INTRODUCE, 0, getResources().getString(R.string.menu_introduce));
        	menu.add(0, ID_COLLECT, 1, getResources().getString(R.string.menu_collect));
        	if(db.isAlbumCollected(mAlbum.getId())){
    			itemCollect = menu.add(0, ID_COLLECT, 1, getResources().getString(R.string.menu_collect_cancel));
	    	}else{
	    		itemCollect = menu.add(0, ID_COLLECT, 1, getResources().getString(R.string.menu_collect));
	    	}
        }
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
        case android.R.id.home:
            finish();
            break;
//        case R.id.action_settings:
//
//            break;
        case R.id.action_about:
            aboutUsDialog.show();
            break;
        case R.id.action_contact:
            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "service@kosbrother.com" });
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "聯絡我們 from 歌曲王國");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            break;
        case R.id.action_grade:
             Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.recommend_url)));
             startActivity(browserIntent);
            break;
        case ID_INTRODUCE:
            if (mAlbum.getDescription().equals("null")) {
                new DownloadAlbumTask().execute();
            } else {
                introduceDialog.setMessage(mAlbum.getDescription());
                introduceDialog.show();
            }
            break;
        case ID_COLLECT:            
            if (db.isAlbumCollected(mAlbum.getId())) {
                db.deleteAlbum(mAlbum);
                itemCollect.setTitle(getResources().getString(R.string.menu_collect));
                Toast.makeText(AlbumActivity.this, "已取消此專輯收藏", Toast.LENGTH_SHORT).show();
            } else {
                db.insertAlbum(mAlbum);
                itemCollect.setTitle(getResources().getString(R.string.menu_collect_cancel));
                Toast.makeText(AlbumActivity.this, "已加入此專輯收藏", Toast.LENGTH_SHORT).show();
            }
            break;
        }
        return true;
    }

    private class DownloadChannelsTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub

            mSongs = LyricAPI.getAlbumSongs(mAlbum.getId());
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);

            if (mSongs != null && mSongs.size() != 0) {
                try {
                    mdapter = new ListSongAdapter(AlbumActivity.this, mSongs);
                    myList.setAdapter(mdapter);
                } catch (Exception e) {

                }
            } else {
                reloadLayout.setVisibility(View.VISIBLE);
            }

        }
    }

    private void setAboutUsDialog() {
        // TODO Auto-generated method stub
        aboutUsDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.about_us_string)).setIcon(R.drawable.app_icon_72)
                .setMessage(getResources().getString(R.string.about_us))
                .setPositiveButton(getResources().getString(R.string.yes_string), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    private void setIntroduceDialog() {
        introduceDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.album_introduce_title)).setMessage(mAlbum.getDescription())
                .setPositiveButton(getResources().getString(R.string.yes_string), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
    }

    private class DownloadAlbumTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(AlbumActivity.this, "資料讀取中...", null);
            progressDialog.setCancelable(true);
        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub
            // get DB Song data
            Album aAlbum = LyricAPI.getAlbum(mAlbum.getId());
            if (aAlbum != null) {
                mAlbum = aAlbum;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (!mAlbum.getDescription().equals("null")) {
                introduceDialog.setMessage(mAlbum.getDescription());
                introduceDialog.show();
            } else {
                Toast.makeText(AlbumActivity.this, "無簡介", Toast.LENGTH_SHORT).show();
            }

        }
    }


    
    @Override
    public void onStart() {
      super.onStart();
    }

    @Override
    public void onStop() {
      super.onStop();
    }
}
