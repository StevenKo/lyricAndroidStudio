package com.taiwan.imageload;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.kosbrother.lyric.R;
import com.kosbrother.lyric.SongActivity;
import com.kosbrother.lyric.entity.SingerNews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListNewsAdapter extends BaseAdapter {

    private final Activity        activity;
    private final ArrayList<SingerNews> data;
    private static LayoutInflater inflater = null;
    public ImageLoader            imageLoader;

    public ListNewsAdapter(Activity a, ArrayList<SingerNews> d) {
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
        vi = inflater.inflate(R.layout.item_news_list, null);

        TextView text = (TextView) vi.findViewById(R.id.text_news_list);
        ImageView image = (ImageView) vi.findViewById(R.id.image_news_list);
        TextView textDate = (TextView) vi.findViewById(R.id.text_list_date);
        
        text.setText(Html.fromHtml(data.get(position).getTitle()));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");  
        String dateString = formatter.format(data.get(position).getReleateTime()); 
        textDate.setText(dateString);
        
        if(data.get(position).getPicLink()!= null && !data.get(position).getPicLink().equals("null")){
        	imageLoader.DisplayImage(data.get(position).getPicLink(), image);
        }else{
        	image.setImageResource(R.drawable.app_icon);
        }
        
        vi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent gradeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(position).getLink()));
            	activity.startActivity(gradeIntent);
            }
        });
        
        return vi;
    }
}
