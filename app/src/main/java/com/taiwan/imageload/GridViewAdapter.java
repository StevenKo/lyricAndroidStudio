package com.taiwan.imageload;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.kosbrother.lyric.R;
import com.kosbrother.lyric.SingerCategoryActivity;
import com.kosbrother.lyric.entity.SingerCategory;

public class GridViewAdapter extends BaseAdapter {

    private final Activity                  activity;
    private final ArrayList<SingerCategory> data;
    private static LayoutInflater           inflater = null;
    public ImageLoader                      imageLoader;
    private final int[]                     icons    = { R.drawable.male_singer_32, R.drawable.female_singer, R.drawable.group, R.drawable.japan,
            R.drawable.world, R.drawable.other, R.drawable.band, R.drawable.origin_sound

                                                     };

    public GridViewAdapter(Activity a, ArrayList<SingerCategory> d) {
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

        vi = inflater.inflate(R.layout.item_gridview_singer_category, null);
        TextView mTextView = (TextView) vi.findViewById(R.id.text_grid_item);
        ImageView img = (ImageView) vi.findViewById(R.id.icon);
        mTextView.setText(data.get(position).getName());
        img.setImageDrawable(activity.getResources().getDrawable(icons[position]));
        vi.setClickable(true);
        vi.setFocusable(true);
        vi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SingerCategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("SingerCategoryId", data.get(position).getId());
                bundle.putString("SingerCategoryName", data.get(position).getName());
                intent.putExtras(bundle);
                activity.startActivity(intent);

            }

        });

        return vi;
    }
}
