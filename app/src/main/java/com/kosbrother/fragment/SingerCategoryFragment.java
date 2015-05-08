package com.kosbrother.fragment;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.kosbrother.lyric.R;
import com.kosbrother.lyric.api.LyricAPI;
import com.kosbrother.lyric.entity.SingerSearchWayItem;
import com.kosbrother.lyric.entity.Song;
import com.taiwan.imageload.GridViewAdapter;
import com.taiwan.imageload.GridViewSearchWayAdapter;
import com.taiwan.imageload.ListSongAdapter;

public class SingerCategoryFragment extends Fragment {
	
//	private ListSongAdapter mdapter;
	private ArrayList<SingerSearchWayItem> mSearchways;
	private static int singerSearchWayId;
	private GridView mGridView;
	private GridViewSearchWayAdapter mdapter;
	private LinearLayout progressLayout;
	private LinearLayout reloadLayout;
	private Button buttonReload;
	
	

    
    public static SingerCategoryFragment newInstance(int search_way_id) {

    	SingerCategoryFragment fragment = new SingerCategoryFragment();
    	singerSearchWayId = search_way_id;
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.layout_category, container, false);
        progressLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_progress);
    	reloadLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_reload);
        mGridView = (GridView) myFragmentView.findViewById(R.id.grid_search_way);
    	buttonReload = (Button) myFragmentView.findViewById(R.id.button_reload);
        
    	
    	buttonReload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                progressLayout.setVisibility(View.VISIBLE);
                reloadLayout.setVisibility(View.GONE);
                new DownloadChannelsTask().execute();
            }
        });
    	
        if (mdapter != null) {
//            progressLayout.setVisibility(View.GONE);
            mGridView.setAdapter(mdapter);
        } else {
            new DownloadChannelsTask().execute();
        }
        
        return myFragmentView;
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
        	
        	mSearchways = LyricAPI.getSingerSearchWayItems(singerSearchWayId);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
                       
            if(mSearchways !=null && mSearchways.size()!=0){
          	  try{
          		mdapter = new GridViewSearchWayAdapter(getActivity(), mSearchways);
          		mGridView.setAdapter(mdapter);
          	  }catch(Exception e){
          		 
          	  }
            }else{
            	reloadLayout.setVisibility(View.VISIBLE);
            }

        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}