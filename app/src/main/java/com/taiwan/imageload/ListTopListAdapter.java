package com.taiwan.imageload;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kosbrother.lyric.R;
import com.kosbrother.lyric.TopListSongsActivity;
import com.kosbrother.lyric.entity.Album;
import com.kosbrother.lyric.entity.TopList;

public class ListTopListAdapter extends BaseAdapter {

    private final Activity         activity;
	private ArrayList<TopList> data;
    private static LayoutInflater  inflater = null;

    public ListTopListAdapter(Activity a, ArrayList<TopList> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // View vi= new View();
        View vi = inflater.inflate(R.layout.item_top_list, null);
        TextView text_name = (TextView) vi.findViewById(R.id.text_list_name);
        String name = data.get(position).getName();
        text_name.setText(name);


        vi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TopListSongsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("TopListId", data.get(position).getId());
                bundle.putString("TopListName", data.get(position).getName());
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });

        return vi;
    }
}
