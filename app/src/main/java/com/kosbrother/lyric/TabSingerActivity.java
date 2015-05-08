package com.kosbrother.lyric;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.kosbrother.lyric.api.LyricAPI;
import com.kosbrother.lyric.entity.SingerCategory;
import com.taiwan.imageload.GridViewAdapter;

import java.util.ArrayList;

public class TabSingerActivity extends Activity {

	private GridView mGridView;
	private ArrayList<SingerCategory> mCategory;
	private LinearLayout progressLayout;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_singer);
       
        
        mCategory = LyricAPI.getSingerCategories();
       
        mGridView = (GridView) findViewById(R.id.grid_singers);
        GridViewAdapter mdapter = new GridViewAdapter(TabSingerActivity.this, mCategory);
        mGridView.setAdapter(mdapter);
        
        progressLayout = (LinearLayout) findViewById(R.id.layout_progress);
        progressLayout.setVisibility(View.GONE);
        
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
		this.getParent().onMenuItemSelected(featureId, item);
        return true;
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