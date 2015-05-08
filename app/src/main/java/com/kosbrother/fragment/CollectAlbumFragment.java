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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kosbrother.lyric.AlbumActivity;
import com.kosbrother.lyric.MainTabActivty;
import com.kosbrother.lyric.R;
import com.kosbrother.lyric.api.LyricAPI;
import com.kosbrother.lyric.db.SQLiteLyric;
import com.kosbrother.lyric.entity.Album;
import com.kosbrother.lyric.entity.Singer;
import com.taiwan.imageload.ListCollectAlbumAdapter;

public class CollectAlbumFragment extends Fragment {

    private LinearLayout            progressLayout;
    private LinearLayout            noDataLayout;
    private ListView                myList;
    private ListCollectAlbumAdapter mdapter;
    private ArrayList<Album>        mAlbums;
    private Activity                mActivity;

    public static CollectAlbumFragment newInstance() {
        CollectAlbumFragment fragment = new CollectAlbumFragment();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = this.getActivity();

        View myFragmentView = inflater.inflate(R.layout.layout_collect, container, false);
        progressLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_progress);
        noDataLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_no_data);

        myList = (ListView) myFragmentView.findViewById(R.id.listview_collect);

        myList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(mActivity, AlbumActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("AlbumId", mAlbums.get(position).getId());
                bundle.putString("AlbumName", mAlbums.get(position).getName());
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
            }
        });

        myList.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                final String[] ListStr = { "欣賞專輯", "刪除", "取消" };

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(mAlbums.get(position).getName());
                builder.setItems(ListStr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // to song activity
                            Intent intent = new Intent(mActivity, AlbumActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("AlbumId", mAlbums.get(position).getId());
                            bundle.putString("AlbumName", mAlbums.get(position).getName());
                            intent.putExtras(bundle);
                            mActivity.startActivity(intent);
                        } else if (item == 1) {
                            // delete
                            SQLiteLyric db = new SQLiteLyric(mActivity);
                            db.deleteAlbum(mAlbums.get(position));
                            mAlbums = db.getAllAlbums();
                            mdapter = new ListCollectAlbumAdapter(mActivity, mAlbums);
                            myList.setAdapter(mdapter);
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
            myList.setAdapter(mdapter);
        } else {
            new DownloadChannelsTask().execute();
        }

        return myFragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
            mAlbums = db.getAllAlbums();
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);

            if (mAlbums != null && mAlbums.size() != 0) {
                try {
                    mdapter = new ListCollectAlbumAdapter(mActivity, mAlbums);
                    myList.setAdapter(mdapter);
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
			String albums = "";
			for(Album album :mAlbums){
				albums += album.getId() + ",";
			}
			
			LyricAPI.sendCollectAlbums(albums, Settings.Secure.getString(mActivity.getContentResolver(),Settings.Secure.ANDROID_ID));
			return null;
		}
    	
    }

}
