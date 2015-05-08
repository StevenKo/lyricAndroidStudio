package com.kosbrother.lyric;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.kosbrother.lyric.entity.Album;
import com.taiwan.imageload.ListAlbumAdapter;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class NewAlbumActivity extends Activity {

	public  int myPage = 1;
	private Boolean checkLoad = true;
	private LoadMoreListView myList;
	private ArrayList<Album> mAlbums;
	private ArrayList<Album> moreAlbums = new ArrayList<Album>();
	private ListAlbumAdapter mdapter;
	private LinearLayout progressLayout;
	private LinearLayout reloadLayout;
	private AlertDialog.Builder aboutUsDialog;
	private Button buttonReload;
	private final String  adWhirlKey = Setting.adwhirlKey;
	
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadmore);
        setTitle("最新熱門專輯");
        int sdkVersion = android.os.Build.VERSION.SDK_INT; 
        if(sdkVersion > 10){
        	getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        
        myList = (LoadMoreListView) findViewById(R.id.news_list);
        progressLayout = (LinearLayout) findViewById(R.id.layout_progress);
    	reloadLayout = (LinearLayout) findViewById(R.id.layout_reload);
    	buttonReload = (Button) findViewById(R.id.button_reload);
       
    	buttonReload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                progressLayout.setVisibility(View.VISIBLE);
                reloadLayout.setVisibility(View.GONE);
                new DownloadChannelsTask().execute();
            }
        });
    	
    	myList.setOnLoadMoreListener(new OnLoadMoreListener() {
            public void onLoadMore() {
            	if(checkLoad){
					myPage = myPage +1;
					new LoadMoreTask().execute();
				}else{
					myList.onLoadMoreComplete();
				}
            }
        });
        new DownloadChannelsTask().execute();
        
        setAboutUsDialog();
        
        AdViewUtil.setAdView((LinearLayout) findViewById(R.id.adonView), this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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
        	
        	mAlbums = LyricAPI.getNewAlbums(myPage);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
                       
            if(mAlbums !=null && mAlbums.size()!=0){
          	  try{
          		mdapter = new ListAlbumAdapter(NewAlbumActivity.this, mAlbums);
          		myList.setAdapter(mdapter);
          	  }catch(Exception e){
          		 
          	  }
            }else{
            	reloadLayout.setVisibility(View.VISIBLE);
            }

        }
    }
    
    private class LoadMoreTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub
        	
        	moreAlbums.clear();
        	moreAlbums = LyricAPI.getNewAlbums(myPage);
        	if(moreAlbums!= null){
	        	for(int i=0; i<moreAlbums.size();i++){	        		
	        		mAlbums.add(moreAlbums.get(i));
	            }
        	}
        	
        	
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            
            if(moreAlbums!= null && moreAlbums.size()!=0){
            	mdapter.notifyDataSetChanged();	                
            }else{
                checkLoad= false;
                Toast.makeText(NewAlbumActivity.this, "no more data", Toast.LENGTH_SHORT).show();            	
            }       
          	myList.onLoadMoreComplete();
          	
          	
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

    
    @Override
    public void onStart() {
      super.onStart();
    }

    @Override
    public void onStop() {
      super.onStop();
    }
}
