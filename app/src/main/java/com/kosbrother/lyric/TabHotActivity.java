package com.kosbrother.lyric;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kosbrother.circlegallery.CircleGalleryAdapter;
import com.kosbrother.lyric.api.LyricAPI;
import com.kosbrother.lyric.entity.Video;
import com.taiwan.imageload.ListAdapter;

import java.util.ArrayList;

public class TabHotActivity extends Activity {

    private GridView             mGridView;
    private Gallery              mGallery;
    private LinearLayout         linearDownLoading;
    private LinearLayout         linearNetwork;
    private ArrayList<Video>     mHotVideos;
    private CircleGalleryAdapter mCircleAdapter;
    private Button btnReload;

    private final Integer[]      mImageIds = { R.drawable.icon_list_new, R.drawable.icon_list_hot, R.drawable.icon_list_song, R.drawable.hot_singer, R.drawable.list_num, R.drawable.thumbs_up};
    private final String[]       mStrings  = { "最新熱門", "熱門專輯", "熱門歌曲", "熱門歌手","歌曲排行","推薦歌曲" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth(); // deprecated
        int height = display.getHeight(); // deprecated

        if (width > 480) {
        	   setContentView(R.layout.layout_hot);
        } else {
        	   setContentView(R.layout.layout_hot_small);
        }
          
        linearDownLoading = (LinearLayout) findViewById(R.id.linear_downloading);
        linearNetwork = (LinearLayout) findViewById(R.id.linear_network);
        mGallery = (Gallery) findViewById(R.id.gallery);

        mGridView = (GridView) findViewById(R.id.grid_tab_hot);
        ListAdapter mdapter = new ListAdapter(TabHotActivity.this, mStrings, mImageIds);
        mGridView.setAdapter(mdapter);
        
        btnReload = (Button) findViewById(R.id.btn_promotion_reload);
        
        btnReload.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {
            	if(isOnline()){
	            	linearNetwork.setVisibility(View.GONE);
	            	new DownloadChannelsTask().execute();
            	}else{
            		Toast.makeText(getApplicationContext(), "無網路連線!", Toast.LENGTH_SHORT).show();
            	}
            }		
        });
        
        mGallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				// TODO Auto-generated method stub
				position= getPosition(position);
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mHotVideos.get(position).getYoutubeLink()));
                startActivity(browserIntent);
			}

        });
        
        new DownloadChannelsTask().execute();

    }

    private class DownloadChannelsTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub

            mHotVideos = LyricAPI.getHotVideos();
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            linearDownLoading.setVisibility(View.GONE);

            if (mHotVideos != null && mHotVideos.size() != 0) {
                try {
                    mCircleAdapter = new CircleGalleryAdapter(TabHotActivity.this, mHotVideos);
                    mGallery.setAdapter(mCircleAdapter);
                    // To select the xSelected one -> 0 is the first.
                    int xSelected = 0;
                    // To make the view go to the middle of our 'endless' array
                    mGallery.setSelection(Integer.MAX_VALUE / 2 + (Integer.MAX_VALUE / 2) % 5 - 1 + xSelected);
                } catch (Exception e) {

                }
            } else {
                linearNetwork.setVisibility(View.VISIBLE);
            }

        }
    }
    
    int getPosition(int position)
    {
    	 if (position >= mHotVideos.size()) { 
	            position = position % mHotVideos.size(); 
	        } 
	     return position; 
    }
    
    public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
		this.getParent().onMenuItemSelected(featureId, item);
        return true;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
    
    @Override
    public void onBackPressed() {
    	this.getParent().onBackPressed(); 
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
