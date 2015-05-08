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
import com.kosbrother.lyric.entity.Singer;
import com.kosbrother.lyric.entity.Song;
import com.taiwan.imageload.GridViewSingersAdapter;
import com.taiwan.imageload.ListAlbumAdapter;
import com.taiwan.imageload.ListSongAdapter;
import com.taiwan.imageload.LoadMoreGridView;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class SearchActivity extends Activity {
	
	private Boolean          checkLoad  = true;
    private int       myPage     = 1;
	private LinearLayout progressLayout;
	private LinearLayout reloadLayout;
	private LinearLayout     loadmoreLayout;
	private LoadMoreListView myList;
	private LoadMoreGridView       mGridView;
	private Button buttonReload;
	private ListSongAdapter mSongAdapter;
	private ListAlbumAdapter mAlbumAdapter;
	private GridViewSingersAdapter mSingerAdapter;
	private ArrayList<Song> mSongs;
	private ArrayList<Album> mAlbums;
	private ArrayList<Singer> mSingers;
	private ArrayList<Song> moreSongs = new ArrayList<Song>();
	private ArrayList<Album> moreAlbums = new ArrayList<Album>();
	private ArrayList<Singer> moreSingers = new ArrayList<Singer>();
	
	private Bundle mBundle;
	private int searchTypeId;
	private String searchName;
	private Album mAlbum;

	private AlertDialog.Builder aboutUsDialog;
	private ProgressDialog progressDialog   = null;
	private final String  adWhirlKey = Setting.adwhirlKey;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mBundle = this.getIntent().getExtras();
        searchTypeId = mBundle.getInt("SearchTypeId");
        searchName = mBundle.getString("SearchKeyword");
        
        if(searchTypeId == 0){       
        	setContentView(R.layout.loadmore);
        	setTitle("歌曲搜索 >"+searchName);
        	progressLayout = (LinearLayout) findViewById(R.id.layout_progress);
        	reloadLayout = (LinearLayout) findViewById(R.id.layout_reload);
        	buttonReload = (Button) findViewById(R.id.button_reload);
        	myList = (LoadMoreListView) findViewById(R.id.news_list);
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
        }else if(searchTypeId == 1){
        	setContentView(R.layout.loadmore);
        	setTitle("歌詞搜索 >"+searchName);
        	progressLayout = (LinearLayout) findViewById(R.id.layout_progress);
        	reloadLayout = (LinearLayout) findViewById(R.id.layout_reload);
        	buttonReload = (Button) findViewById(R.id.button_reload);
        	myList = (LoadMoreListView) findViewById(R.id.news_list);
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
        	
        }else if(searchTypeId == 2){
        	setContentView(R.layout.loadmore);
        	setTitle("專輯搜索 >"+searchName);
        	progressLayout = (LinearLayout) findViewById(R.id.layout_progress);
        	reloadLayout = (LinearLayout) findViewById(R.id.layout_reload);
        	buttonReload = (Button) findViewById(R.id.button_reload);
        	myList = (LoadMoreListView) findViewById(R.id.news_list);
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
        	
        }else if(searchTypeId == 3){
        	setContentView(R.layout.loadmore_grid);
        	setTitle("歌手搜索 >"+searchName);
        	// 還沒寫
        	progressLayout = (LinearLayout) findViewById(R.id.layout_progress);
        	reloadLayout = (LinearLayout) findViewById(R.id.layout_reload);
        	loadmoreLayout = (LinearLayout) findViewById(R.id.load_more_grid);
        	buttonReload = (Button) findViewById(R.id.button_reload);
        	mGridView = (LoadMoreGridView) findViewById(R.id.grid_loadmore);       	
        	mGridView.setOnLoadMoreListener(new LoadMoreGridView.OnLoadMoreListener() {
                public void onLoadMore() {
                    // Do the work to load more items at the end of list

                    if (checkLoad) {
                        myPage = myPage + 1;
                        loadmoreLayout.setVisibility(View.VISIBLE);
                        new LoadMoreTask().execute();
                    } else {
                    	mGridView.onLoadMoreComplete();
                    }
                }
            });
        }
        
        int sdkVersion = android.os.Build.VERSION.SDK_INT; 
        if(sdkVersion > 10){
        	getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        buttonReload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                progressLayout.setVisibility(View.VISIBLE);
                reloadLayout.setVisibility(View.GONE);
                new DownloadChannelsTask().execute();
            }
        });
        
      new DownloadChannelsTask().execute();    
        
      setAboutUsDialog();
      
      AdViewUtil.setAdView((LinearLayout) findViewById(R.id.adonView), this);
    }
    
    @SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.main, menu);
    	return true;
    }
    
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	        finish();
	        break;
	    case R.id.action_about:
	    	aboutUsDialog.show();
	        break;
	    case R.id.action_contact:
	    	final Intent emailIntent = new Intent(Intent.ACTION_SEND);
	    	emailIntent.setType("plain/text");
	    	emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"service@kosbrother.com"});
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
        	if(searchTypeId == 0){       
        		mSongs = LyricAPI.searchSongName(searchName, 1);
            }else if(searchTypeId == 1){       
        		mSongs = LyricAPI.searchSongLyric(searchName, 1);
            }else if(searchTypeId == 2){
            	mAlbums = LyricAPI.searchAlbum(searchName, 1);
            }else if(searchTypeId == 3){
            	mSingers = LyricAPI.searchSinger(searchName, 1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
            
            if(searchTypeId == 0){           
	            if(mSongs !=null && mSongs.size()!=0){
	          	  try{
	          		mSongAdapter = new ListSongAdapter(SearchActivity.this, mSongs);
	          		myList.setAdapter(mSongAdapter);
	          	  }catch(Exception e){
	          		 
	          	  }
	            }else{
	            	reloadLayout.setVisibility(View.VISIBLE);
	            }
            }else if(searchTypeId == 1){           
	            if(mSongs !=null && mSongs.size()!=0){
		          	  try{
		          		mSongAdapter = new ListSongAdapter(SearchActivity.this, mSongs);
		          		myList.setAdapter(mSongAdapter);
		          	  }catch(Exception e){
		          		 
		          	  }
		            }else{
		            	reloadLayout.setVisibility(View.VISIBLE);
		            }
	        }else if(searchTypeId == 2){
            	if(mAlbums !=null && mAlbums.size()!=0){
  	          	  try{
  	          		mAlbumAdapter = new ListAlbumAdapter(SearchActivity.this, mAlbums);
  	          		myList.setAdapter(mAlbumAdapter);
  	          	  }catch(Exception e){
  	          		 
  	          	  }
  	            }else{
  	            	reloadLayout.setVisibility(View.VISIBLE);
  	            }
            }else if(searchTypeId == 3){
            	loadmoreLayout.setVisibility(View.GONE);
            	if(mSingers !=null && mSingers.size()!=0){
    	          	try{
    	          		mSingerAdapter = new GridViewSingersAdapter(SearchActivity.this, mSingers);
    	          		mGridView.setAdapter(mSingerAdapter);
    	          	}catch(Exception e){
    	          		 
    	          	}
    	        }else{
    	            reloadLayout.setVisibility(View.VISIBLE);
    	        }
            }

        }
    }
    
    private class LoadMoreTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub
        	
        	
        	if(searchTypeId == 0){       
        		moreSongs = LyricAPI.searchSongName(searchName, myPage);
            	if (moreSongs != null && moreSongs.size()!=0) {
                    for (int i = 0; i < moreSongs.size(); i++) {
                    	mSongs.add(moreSongs.get(i));
                    }
                }
            }else if(searchTypeId == 1){
            	moreSongs = LyricAPI.searchSongLyric(searchName, myPage);
            	if (moreSongs != null && moreSongs.size()!=0) {
                    for (int i = 0; i < moreSongs.size(); i++) {
                    	mSongs.add(moreSongs.get(i));
                    }
                }
            }else if(searchTypeId == 2){
            	moreAlbums = LyricAPI.searchAlbum(searchName, myPage);
            	if (moreAlbums != null && moreAlbums.size()!=0) {
                    for (int i = 0; i < moreAlbums.size(); i++) {
                    	mAlbums.add(moreAlbums.get(i));
                    }
                }
            }else if(searchTypeId == 3){
            	moreSingers = LyricAPI.searchSinger(searchName, myPage);
                if (moreSingers != null && moreSingers.size()!=0) {
                    for (int i = 0; i < moreSingers.size(); i++) {
                    	mSingers.add(moreSingers.get(i));
                    }
                }
            }
        	
        	

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            
            
            if(searchTypeId == 0){       
            	if(moreSongs!= null && moreSongs.size()!=0){
            		mSongAdapter.notifyDataSetChanged();	                
                }else{
                    checkLoad= false;
                    Toast.makeText(SearchActivity.this, "no more data", Toast.LENGTH_SHORT).show();            	
                }       
              	myList.onLoadMoreComplete();
            }else if(searchTypeId == 1){       
            	if(moreSongs!= null && moreSongs.size()!=0){
            		mSongAdapter.notifyDataSetChanged();	                
                }else{
                    checkLoad= false;
                    Toast.makeText(SearchActivity.this, "no more data", Toast.LENGTH_SHORT).show();            	
                }       
              	myList.onLoadMoreComplete();
            }else if(searchTypeId == 2){
           	
            	if(moreAlbums!= null && moreAlbums.size()!=0){
            		mAlbumAdapter.notifyDataSetChanged();	                
                }else{
                    checkLoad= false;
                    Toast.makeText(SearchActivity.this, "no more data", Toast.LENGTH_SHORT).show();            	
                }       
              	myList.onLoadMoreComplete();
            	
            }else if(searchTypeId == 3){
            	loadmoreLayout.setVisibility(View.GONE);

                if (moreSingers != null && moreSingers.size()!=0) {
                	mSingerAdapter.notifyDataSetChanged();
                } else {
                    checkLoad = false;
                    Toast.makeText(SearchActivity.this, "no more data", Toast.LENGTH_SHORT).show();
                }
                mGridView.onLoadMoreComplete();
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
    
    @Override
    public void onStart() {
      super.onStart();
    }

    @Override
    public void onStop() {
      super.onStop();
    }
	
}
