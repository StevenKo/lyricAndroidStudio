package com.taiwan.imageload;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kosbrother.lyric.R;
import com.kosbrother.lyric.entity.Singer;

public class GridViewCollectSingersAdapter extends BaseAdapter {

    private final Activity          activity;
    private final ArrayList<Singer> data;
    private static LayoutInflater   inflater = null;
    public ImageLoader              imageLoader;

    public GridViewCollectSingersAdapter(Activity a, ArrayList<Singer> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext(), 70);

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
        View vi = convertView;
        // if (convertView == null)
        // vi = inflater.inflate(R.layout.item_gridview_novel, null);

        // Display display = activity.getWindowManager().getDefaultDisplay();
        // int width = display.getWidth(); // deprecated
        // int height = display.getHeight(); // deprecated
        //
        // if (width > 480) {
        //
        // } else {
        // vi = inflater.inflate(R.layout.item_gridview_novel_small, null);
        // }

        vi = inflater.inflate(R.layout.item_gridview_novel, null);
        TextView mTextView = (TextView) vi.findViewById(R.id.text_grid_item);
        TextView bracketTextView = (TextView) vi.findViewById(R.id.text_bracket);
        String name = data.get(position).getName();
        String bracket;
        if (name.indexOf("(") != -1) {
            String name2 = name.substring(0, name.indexOf("("));
            bracket = name.substring(name.indexOf("("), name.length());
            name = name2;
            bracketTextView.setText(bracket);
        }

        mTextView.setText(name);


        return vi;
    }
}
