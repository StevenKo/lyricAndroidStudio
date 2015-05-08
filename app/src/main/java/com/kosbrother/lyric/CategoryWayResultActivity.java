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

import com.kosbrother.lyric.api.LyricAPI;
import com.kosbrother.lyric.entity.Singer;
import com.taiwan.imageload.GridViewSingersAdapter;
import com.taiwan.imageload.LoadMoreGridView;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class CategoryWayResultActivity extends Activity{

	private LoadMoreGridView   mGridView;
	private ArrayList<Singer> mSingers;
	private ArrayList<Singer>	   moreSingers;
	private Bundle mBundle;
	private int searchWayId;
	private String searchWayName;
	private GridViewSingersAdapter mdapter;
	private LinearLayout progressLayout;
	private LinearLayout reloadLayout;
	private Button buttonReload;
	private Boolean   checkLoad  = true;
    private int       myPage     = 1;
    private LinearLayout     loadmoreLayout;
    private AlertDialog.Builder aboutUsDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadmore_grid);
        mGridView = (LoadMoreGridView) findViewById(R.id.grid_loadmore);
        progressLayout = (LinearLayout) findViewById(R.id.layout_progress);
    	reloadLayout = (LinearLayout) findViewById(R.id.layout_reload);
    	loadmoreLayout = (LinearLayout) findViewById(R.id.load_more_grid);
    	buttonReload = (Button) findViewById(R.id.button_reload);
        
        mBundle = this.getIntent().getExtras();
        searchWayId = mBundle.getInt("SearchWayId");
        searchWayName = mBundle.getString("SearchWayName");
        
        setTitle(searchWayName);
        int sdkVersion = android.os.Build.VERSION.SDK_INT; 
        if(sdkVersion > 10){
        	getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
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
    
    private class DownloadChannelsTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            
        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub
        	
        	mSingers = LyricAPI.getSingers(searchWayId, myPage);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            	progressLayout.setVisibility(View.GONE);
            	loadmoreLayout.setVisibility(View.GONE);
                       
            if(mSingers !=null && mSingers.size()!=0){
          	  try{        		
                mdapter = new GridViewSingersAdapter(CategoryWayResultActivity.this, mSingers);
                mGridView.setAdapter(mdapter);
          	  }catch(Exception e){
          		 
          	  }
            }else{
            	reloadLayout.setVisibility(View.VISIBLE);
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

        	moreSingers = LyricAPI.getSingers(searchWayId, myPage);
            if (moreSingers != null && moreSingers.size()!=0) {
                for (int i = 0; i < moreSingers.size(); i++) {
                	mSingers.add(moreSingers.get(i));
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            loadmoreLayout.setVisibility(View.GONE);

            if (moreSingers != null && moreSingers.size()!=0) {
            	mdapter.notifyDataSetChanged();
            } else {
                checkLoad = false;
                Toast.makeText(CategoryWayResultActivity.this, "no more data", Toast.LENGTH_SHORT).show();
            }
            mGridView.onLoadMoreComplete();

        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

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