package com.kosbrother.circlegallery;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosbrother.lyric.R;
import com.kosbrother.lyric.entity.Video;
import com.taiwan.imageload.ImageLoader;

public class CircleGalleryAdapter extends BaseAdapter {
	   
    private Activity mActivity;
    private ArrayList<Video> data;
    private static LayoutInflater inflater = null;
    public ImageLoader            imageLoader;

    private Integer[] mImageIds = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e
    };

    public CircleGalleryAdapter(Activity c, ArrayList<Video> b) {
    	mActivity = c;
    	inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	imageLoader = new ImageLoader(mActivity.getApplicationContext(), 80);
    	data = b;
    }
    public int getCount() { 
        return Integer.MAX_VALUE; 
    } 

    public Object getItem(int position) { 
        return position; 
    } 

    public long getItemId(int position) { 
        return position; 
    } 

    public View getView(int position, View convertView, ViewGroup parent) 
    {
        
    	View vi = convertView;
    	Display display = mActivity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth(); // deprecated
        int height = display.getHeight(); // deprecated

        if (width > 480) {
            vi = inflater.inflate(R.layout.item_gallery, null);
        } else {
            vi = inflater.inflate(R.layout.item_gallery_small, null);
        }
    	
        ImageView img = (ImageView) vi.findViewById(R.id.image_gallery);
        TextView txt = (TextView) vi.findViewById(R.id.text_gallery);
        
        
        position= getPosition(position);
        txt.setText(data.get(position).getTitle());
        
                
        if (width > 480) {
        	try{
                imageLoader.DisplayImage(data.get(position).getThumbnail(), img, 550);
             }catch(Exception e){
             	
             }
        } else {
        	try{
                imageLoader.DisplayImage(data.get(position).getThumbnail(), img, 350);
             }catch(Exception e){
             	
             }
        }
        
        return vi; 
    } 
    
    int getPosition(int position)
    {
    	 if (position >= data.size()) { 
	            position = position % data.size(); 
	        } 
	     return position; 
    }
}