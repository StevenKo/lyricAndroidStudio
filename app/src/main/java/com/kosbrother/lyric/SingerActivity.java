package com.kosbrother.lyric;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kosbrother.fragment.SingerAlbumFragment;
import com.kosbrother.fragment.SingerNewsFragment;
import com.kosbrother.fragment.SingerSongFragment;
import com.kosbrother.fragment.SingerVideoFragment;
import com.kosbrother.lyric.api.LyricAPI;
import com.kosbrother.lyric.db.SQLiteLyric;
import com.kosbrother.lyric.entity.Singer;
import com.viewpagerindicator.TabPageIndicator;

@SuppressLint("NewApi")
public class SingerActivity extends FragmentActivity {
	
	private final int ID_INTRODUCE = 666666;
	private final int ID_COLLECT   = 777777;
	
	private ViewPager pager;
	private String[] CONTENT;
	private Bundle mBundle;
	private int SingerId;
	private String SingerName;
	private Singer theSinger;
	
	private AlertDialog.Builder aboutUsDialog;
	private AlertDialog.Builder introduceDialog;
	private ProgressDialog progressDialog   = null;
	
	private int sdkVersion;
	private SQLiteLyric db;
	private MenuItem itemCollect;
	
	private final String  adWhirlKey = Setting.adwhirlKey;
	
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_tabs);
        db = new SQLiteLyric(SingerActivity.this);
        
        mBundle = this.getIntent().getExtras();
        SingerId = mBundle.getInt("SingerId");
        SingerName = mBundle.getString("SingerName");
        theSinger = new Singer(SingerId, SingerName, "null");
        
        
        setTitle(SingerName);
        sdkVersion = android.os.Build.VERSION.SDK_INT; 
        if(sdkVersion > 10){
        	getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        Resources res = getResources();
        CONTENT = res.getStringArray(R.array.singer_tabs);

        
        FragmentStatePagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
        setIntroduceDialog();
        setAboutUsDialog();
        
        AdViewUtil.setAdView((LinearLayout) findViewById(R.id.adonView), this);
        
        new UpdateServerCollectTask().execute();
    }
	
	private class UpdateServerCollectTask extends AsyncTask {

		@Override
		protected Object doInBackground(Object... params) {
			
			LyricAPI.sendSinger(theSinger.getId(), Settings.Secure.getString(SingerActivity.this.getContentResolver(),Settings.Secure.ANDROID_ID));
			return null;
		}
    	
    }
	
	
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.main, menu);
    	if (sdkVersion > 10){
    		menu.add(0, ID_INTRODUCE, 0, getResources().getString(R.string.menu_introduce)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);  
    		if(db.isSingerCollected(theSinger.getId())){
    			itemCollect = menu.add(0, ID_COLLECT, 1, getResources().getString(R.string.menu_collect_cancel));
    			itemCollect.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    	}else{
	    		itemCollect = menu.add(0, ID_COLLECT, 1, getResources().getString(R.string.menu_collect));
	    		itemCollect.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    	}
    		
    	}else{
    		menu.add(0, ID_INTRODUCE, 0, getResources().getString(R.string.menu_introduce));
    		if(db.isSingerCollected(theSinger.getId())){
    			itemCollect = menu.add(0, ID_COLLECT, 1, getResources().getString(R.string.menu_collect_cancel));
	    	}else{
	    		itemCollect = menu.add(0, ID_COLLECT, 1, getResources().getString(R.string.menu_collect));
	    	}
    	}
    	return true;
    }
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	        finish();
	        break;
	    case R.id.action_about:
	    	aboutUsDialog.show();
	        break;
	    case R.id.action_contact:
	    	final Intent emailIntent = new Intent(Intent.ACTION_SEND);
	    	emailIntent.setType("plain/text");
	    	emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"service@kosbrother.com"});
	    	emailIntent.putExtra(Intent.EXTRA_SUBJECT, "聯絡我們 from 歌曲王國");
	    	emailIntent.putExtra(Intent.EXTRA_TEXT, "");
	    	startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	        break;
	    case R.id.action_grade:
	    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.recommend_url)));
			startActivity(browserIntent);
	        break;
	    case ID_INTRODUCE:
	    	if(theSinger.getDescription().equals("null")){
	    		new DownloadSingerTask().execute();
	    	}else{
	    		introduceDialog.setMessage(theSinger.getDescription());
	    		introduceDialog.show();
	    	}
	        break;
	    case ID_COLLECT:
	    	if(db.isSingerCollected(theSinger.getId())){	    		
	    		db.deleteSinger(theSinger);
	    		itemCollect.setTitle(getResources().getString(R.string.menu_collect));
	    		Toast.makeText(SingerActivity.this, "已取消此歌手收藏", Toast.LENGTH_SHORT).show();
	    	}else{	    		
	    		db.insertSinger(theSinger);
	    		itemCollect.setTitle(getResources().getString(R.string.menu_collect_cancel));
	    		Toast.makeText(SingerActivity.this, "已加入此歌手收藏", Toast.LENGTH_SHORT).show();
	    	}
	        break;
	    }
	    return true;
	}
	
	class GoogleMusicAdapter extends FragmentStatePagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	Fragment kk = new Fragment();
        	if(position==0){
            	kk = SingerAlbumFragment.newInstance(SingerId);
        	}else if(position == 1){
        		kk = SingerSongFragment.newInstance(SingerId);
        	}else if(position == 2){
        		kk = SingerNewsFragment.newInstance(SingerName);
        	}else if(position == 3){
        		kk = SingerVideoFragment.newInstance(SingerName);
        	}
            return kk;
        }
       

        @Override
        public CharSequence getPageTitle(int position) {
        	return CONTENT[position % CONTENT.length];
        }

        @Override
        public int getCount() {
        	return CONTENT.length;
        }
    }
	
	private class DownloadSingerTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SingerActivity.this, "資料讀取中...", null);
            progressDialog.setCancelable(true);
        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub
        	// get DB Song data
        	Singer aSinger = LyricAPI.getSinger(theSinger.getId());
        	if(aSinger!=null){
        		theSinger = aSinger;
        	}
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            if(!theSinger.getDescription().equals("null")){
            	introduceDialog.setMessage(theSinger.getDescription());
	    		introduceDialog.show();
        	}else{
        		Toast.makeText(SingerActivity.this, "無資料", Toast.LENGTH_SHORT).show();
        	}

        }
    }
	
	private void setAboutUsDialog() {
        // TODO Auto-generated method stub
        aboutUsDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.about_us_string)).setIcon(R.drawable.app_icon_72)
                .setMessage(getResources().getString(R.string.about_us))
                .setPositiveButton(getResources().getString(R.string.yes_string), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }
	
	private void setIntroduceDialog(){
		introduceDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.singer_introduce_title))
                .setMessage(theSinger.getDescription())
                .setPositiveButton(getResources().getString(R.string.yes_string), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
	}
    
    @Override
    public void onStart() {
      super.onStart();
    }

    @Override
    public void onStop() {
      super.onStop();
    }
}
