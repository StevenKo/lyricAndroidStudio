package com.taiwan.imageload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosbrother.lyric.R;
import com.kosbrother.lyric.entity.YoutubeVideo;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ListVideoAdapter extends BaseAdapter {

    private final Activity        activity;
    private final ArrayList<YoutubeVideo> data;
    private static LayoutInflater inflater = null;
    public ImageLoader            imageLoader;

    public ListVideoAdapter(Activity a, ArrayList<YoutubeVideo> d) {
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
        vi = inflater.inflate(R.layout.item_video_list, null);
        
        // set view onClick Listener
	        vi.setClickable(true);
	        vi.setFocusable(true);
//	        vi.setBackgroundResource(android.R.drawable.menuitem_background);
	        vi.setOnClickListener(new OnClickListener() {
	
	            @Override
	            public void onClick(View v) {
//	                Toast.makeText(activity, "tt", Toast.LENGTH_SHORT).show();
	                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(position).getLink()));
	                activity.startActivity(browserIntent);
	            }
	
	        });
        
	              
        TextView text = (TextView) vi.findViewById(R.id.text_news_list);
        ImageView image = (ImageView) vi.findViewById(R.id.image_news_list);




        text.setText(data.get(position).getTitle());

        
        if(data.get(position).getThumbnail().equals("") || data.get(position).getThumbnail() == null){
        	image.setImageResource(R.drawable.app_icon);
        }else{        	
        	imageLoader.DisplayImage(data.get(position).getThumbnail(), image);
        }
        
        
        return vi;
    }
    
    public static int[] splitToComponentTimes(int i)
    {
        long longVal = (long)i;
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins , secs};
        return ints;
    }
    
}
