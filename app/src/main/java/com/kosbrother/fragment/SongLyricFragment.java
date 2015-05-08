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
import android.widget.ScrollView;
import android.widget.TextView;

import com.kosbrother.lyric.R;
import com.kosbrother.lyric.SongActivity;
import com.kosbrother.lyric.api.LyricAPI;
import com.kosbrother.lyric.entity.Song;

public class SongLyricFragment extends Fragment {
	
	private LinearLayout progressLayout;
	private LinearLayout reloadLayout;
	private ScrollView myScroll;
	private Button buttonReload;
	private TextView mTextView;
	private Song mSong;
	private static int songId;
	
    public static SongLyricFragment newInstance(int song_id) {

    	SongLyricFragment fragment = new SongLyricFragment();
    	songId = song_id;
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.layout_lyric, container, false);
        progressLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_progress);
    	reloadLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_reload);
    	buttonReload = (Button) myFragmentView.findViewById(R.id.button_reload);
    	myScroll = (ScrollView) myFragmentView.findViewById(R.id.scrollview_lyric);
    	mTextView = (TextView) myFragmentView.findViewById(R.id.textview_lyric);
        
    	
    	buttonReload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                progressLayout.setVisibility(View.VISIBLE);
                reloadLayout.setVisibility(View.GONE);
                new DownloadChannelsTask().execute();
            }
        });
    	
        new DownloadChannelsTask().execute();
       
        
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
        	
        	mSong = LyricAPI.getSong(songId);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
                       
            if(mSong !=null && !mSong.getLyric().equals("null")){
          	  try{
          		mTextView.setText(mSong.getLyric());
          		SongActivity.theSong = mSong;
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
