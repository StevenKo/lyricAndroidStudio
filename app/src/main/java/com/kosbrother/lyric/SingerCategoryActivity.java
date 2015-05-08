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

import com.kosbrother.fragment.SingerCategoryFragment;
import com.kosbrother.lyric.api.LyricAPI;
import com.kosbrother.lyric.entity.SingerSearchWay;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class SingerCategoryActivity extends FragmentActivity {

    private ViewPager                  pager;
    private ArrayList<SingerSearchWay> mCategory;
    private Bundle                     mBundle;
    private int                        categoryId;
    private String                     categoryName;
    private AlertDialog.Builder        aboutUsDialog;
    private final String  adWhirlKey = Setting.adwhirlKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_tabs);

        mBundle = this.getIntent().getExtras();
        categoryId = mBundle.getInt("SingerCategoryId");
        categoryName = mBundle.getString("SingerCategoryName");

        setTitle(categoryName);
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion > 10) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mCategory = LyricAPI.getSingerCategoryWays(categoryId);
        ;

        FragmentStatePagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicator.setCurrentItem(1);

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

            SingerCategoryFragment kk = SingerCategoryFragment.newInstance(mCategory.get(position).getId());
            return kk;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mCategory.get(position).getName();
        }

        @Override
        public int getCount() {
            return mCategory.size();
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
