package com.kosbrother.lyric;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.kosbrother.fragment.TotListSongFragment;
import com.viewpagerindicator.TabPageIndicator;

public class TopListSongsActivity extends FragmentActivity {

    private ViewPager                 pager;
    private AlertDialog.Builder       aboutUsDialog;
    private final String  adWhirlKey = Setting.adwhirlKey;
	private String listName;
	private int listId;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_tabs);
        setTitle("歌曲排行");
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion > 10) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        Bundle mBundle = this.getIntent().getExtras();
        listName = mBundle.getString("TopListName");
        listId =  mBundle.getInt("TopListId");

        FragmentStatePagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        setAboutUsDialog();
        
        AdViewUtil.setAdView((LinearLayout) findViewById(R.id.adonView), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
        case android.R.id.home:
            finish();
            break;
        // case R.id.action_settings:
        //
        // break;
        case R.id.action_about:
            aboutUsDialog.show();
            break;
        case R.id.action_contact:
            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "service@kosbrother.com" });
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "聯絡我們 from 歌曲王國");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            break;
        case R.id.action_grade:
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.recommend_url)));
            startActivity(browserIntent);
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
            // Fragment kk = new Fragment();
            // kk = HotSongFragment.newInstance(mCategory.get(position).getId());
        	TotListSongFragment kk = TotListSongFragment.newInstance(listId);
            return kk;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listName;
        }

        @Override
        public int getCount() {
            return 1;
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
    
    @Override
    public void onStart() {
      super.onStart();
    }

    @Override
    public void onStop() {
      super.onStop();
    }
}