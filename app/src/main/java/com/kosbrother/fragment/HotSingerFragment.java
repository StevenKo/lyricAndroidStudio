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
import android.widget.Toast;

import com.kosbrother.lyric.R;
import com.kosbrother.lyric.api.LyricAPI;
import com.kosbrother.lyric.entity.Singer;
import com.taiwan.imageload.GridViewSingersAdapter;
import com.taiwan.imageload.LoadMoreGridView;

public class HotSingerFragment extends Fragment {

    // private ListSongAdapter mdapter;
    private ArrayList<Singer>      mSingers;
    private ArrayList<Singer>	   moreSingers;
    private int              category_id;
    private LoadMoreGridView       mGridView;
    private GridViewSingersAdapter mdapter;
    private LinearLayout           progressLayout;
    private LinearLayout           reloadLayout;
    private LinearLayout     loadmoreLayout;
    private Button                 buttonReload;
    private Boolean          checkLoad  = true;
    private int       myPage     = 1;

    public HotSingerFragment() {

    }
    
    public static final HotSingerFragment newInstance(int category_id)
    {
    	HotSingerFragment f = new HotSingerFragment();
        Bundle bdl = new Bundle();
        bdl.putInt("id", category_id);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	category_id = getArguments().getInt("id");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.loadmore_grid, container, false);
        progressLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_progress);
        reloadLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_reload);
        loadmoreLayout = (LinearLayout) myFragmentView.findViewById(R.id.load_more_grid);
        mGridView = (LoadMoreGridView) myFragmentView.findViewById(R.id.grid_loadmore);
        buttonReload = (Button) myFragmentView.findViewById(R.id.button_reload);
        
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
        
        if (mdapter != null) {
            // progressLayout.setVisibility(View.GONE);
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

            mSingers = LyricAPI.getCategoryHotSingers(category_id, myPage);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
            loadmoreLayout.setVisibility(View.GONE);
            
            if (mSingers != null && mSingers.size() != 0) {
                try {
                    mdapter = new GridViewSingersAdapter(getActivity(), mSingers);
                    mGridView.setAdapter(mdapter);
                } catch (Exception e) {

                }
            } else {
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

        	moreSingers = LyricAPI.getCategoryHotSingers(category_id, myPage);
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
                Toast.makeText(getActivity(), "no more data", Toast.LENGTH_SHORT).show();
            }
            mGridView.onLoadMoreComplete();

        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}