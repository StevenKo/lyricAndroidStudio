package com.taiwan.imageload;

import static android.provider.BaseColumns._ID;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.kosbrother.lyric.R;
import com.kosbrother.lyric.entity.YoutubeVideo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        TextView textDate = (TextView) vi.findViewById(R.id.text_list_date);
        TextView textViews = (TextView) vi.findViewById(R.id.text_list_views);
        TextView textDuration = (TextView) vi.findViewById(R.id.text_list_duration);
        TextView textLikes = (TextView) vi.findViewById(R.id.text_list_like);
        
        
        
//        String countString = Integer.toString(data.get(position).getViewCount());
        int views = data.get(position).getViewCount();
        textViews.setText(NumberFormat.getNumberInstance(Locale.US).format(views)+" "+activity.getResources().getString(R.string.views));
        
        int[] intTime = splitToComponentTimes(data.get(position).getDuration());
        if(intTime[0]!=0){
        	textDuration.setText(activity.getResources().getString(R.string.time)+":"+Integer.toString(intTime[0])+":"+Integer.toString(intTime[1])+":"+Integer.toString(intTime[2]));
        }else{
        	String timeSecond = "";
        	if(intTime[2]<10){
        		timeSecond = "0"+Integer.toString(intTime[2]);
        	}else{
        		timeSecond = Integer.toString(intTime[2]);
        	}
        	textDuration.setText(activity.getResources().getString(R.string.time)+":"+Integer.toString(intTime[1])+":"+timeSecond);
        }
        
        if(data.get(position).getLikes()!=-1){
        	textLikes.setText(Integer.toString(data.get(position).getLikes())+" "+activity.getResources().getString(R.string.likes));
        }else{
        	textLikes.setVisibility(View.GONE);
        }
        
        text.setText(data.get(position).getTitle());
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");  
        final String dateString = formatter.format(data.get(position).getUploadDate()); 
        textDate.setText(activity.getResources().getString(R.string.launch)+": "+dateString);
        
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
