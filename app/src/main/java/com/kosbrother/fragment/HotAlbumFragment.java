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
import android.widget.LinearLayout;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.kosbrother.lyric.R;
import com.kosbrother.lyric.api.LyricAPI;
import com.kosbrother.lyric.entity.Album;
import com.taiwan.imageload.ListAlbumAdapter;

public class HotAlbumFragment extends Fragment {
	
	private LinearLayout progressLayout;
	private LinearLayout reloadLayout;
	private LoadMoreListView myList;
	private Button buttonReload;
	private ListAlbumAdapter mdapter;
	private ArrayList<Album> mAlbums;
	private int categoryId;
	
    public HotAlbumFragment () {

    }

    public static final HotAlbumFragment newInstance(int category_id)
    {
    	HotAlbumFragment f = new HotAlbumFragment();
        Bundle bdl = new Bundle();
        bdl.putInt("id", category_id);
        f.setArguments(bdl);
        return f;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	categoryId = getArguments().getInt("id");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.loadmore, container, false);
        progressLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_progress);
    	reloadLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_reload);
    	buttonReload = (Button) myFragmentView.findViewById(R.id.button_reload);
    	myList = (LoadMoreListView) myFragmentView.findViewById(R.id.news_list);
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
        
        if (mdapter != null) {
            progressLayout.setVisibility(View.GONE);
            myList.setAdapter(mdapter);
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
        	
        	mAlbums = LyricAPI.getCategoryHotAlbums(categoryId);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
                       
            if(mAlbums !=null && mAlbums.size()!=0){
          	  try{
          		mdapter = new ListAlbumAdapter(getActivity(), mAlbums);
          		myList.setAdapter(mdapter);
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