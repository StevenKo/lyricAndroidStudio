package com.kosbrother.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.kosbrother.lyric.MainTabActivty;
import com.kosbrother.lyric.R;
import com.kosbrother.lyric.SingerActivity;
import com.kosbrother.lyric.api.LyricAPI;
import com.kosbrother.lyric.db.SQLiteLyric;
import com.kosbrother.lyric.entity.Singer;
import com.kosbrother.lyric.entity.Song;
import com.taiwan.imageload.GridViewCollectSingersAdapter;

public class CollectSingerFragment extends Fragment {

    private LinearLayout                  progressLayout;
    private LinearLayout                  noDataLayout;
    private GridView                      myGrid;
    private GridViewCollectSingersAdapter mdapter;
    private ArrayList<Singer>             mSingers;
    private Activity                      mActivity;

    public static CollectSingerFragment newInstance() {

        CollectSingerFragment fragment = new CollectSingerFragment();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = this.getActivity();
        View myFragmentView = inflater.inflate(R.layout.layout_collect_grid, container, false);
        progressLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_progress);
        noDataLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_no_data);

        myGrid = (GridView) myFragmentView.findViewById(R.id.gridview_collect);

        myGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(mActivity, SingerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("SingerId", mSingers.get(position).getId());
                bundle.putString("SingerName", mSingers.get(position).getName());
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
            }
        });

        myGrid.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                final String[] ListStr = { "欣賞歌手", "刪除", "取消" };

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(mSingers.get(position).getName());
                builder.setItems(ListStr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // to song activity
                            Intent intent = new Intent(mActivity, SingerActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("SingerId", mSingers.get(position).getId());
                            bundle.putString("SingerName", mSingers.get(position).getName());
                            intent.putExtras(bundle);
                            mActivity.startActivity(intent);
                        } else if (item == 1) {
                            // delete
                            SQLiteLyric db = new SQLiteLyric(mActivity);
                            db.deleteSinger(mSingers.get(position));
                            mSingers = db.getAllSingers();
                            mdapter = new GridViewCollectSingersAdapter(mActivity, mSingers);
                            myGrid.setAdapter(mdapter);
                        } else if (item == 2) {
                            // do nothing
                            dialog.dismiss();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }
        });

        if (mdapter != null) {
            progressLayout.setVisibility(View.GONE);
            myGrid.setAdapter(mdapter);
        } else {
            new DownloadChannelsTask().execute();
        }

        return myFragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

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
            // get DB Song data
            SQLiteLyric db = new SQLiteLyric(mActivity);
            mSingers = db.getAllSingers();
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);

            if (mSingers != null && mSingers.size() != 0) {
                try {
                    mdapter = new GridViewCollectSingersAdapter(mActivity, mSingers);
                    myGrid.setAdapter(mdapter);
                } catch (Exception e) {

                }
            } else {
                noDataLayout.setVisibility(View.VISIBLE);
            }
            new UpdateServerCollectTask().execute();

        }
    }
    
    private class UpdateServerCollectTask extends AsyncTask {

		@Override
		protected Object doInBackground(Object... params) {
			String singers = "";
			for(Singer singer :mSingers){
				singers += singer.getId() + ",";
			}
			
			LyricAPI.sendCollectSingers(singers, Settings.Secure.getString(mActivity.getContentResolver(),Settings.Secure.ANDROID_ID));
			return null;
		}
    	
    }

}
