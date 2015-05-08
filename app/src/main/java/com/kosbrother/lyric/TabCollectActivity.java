package com.kosbrother.lyric;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.kosbrother.fragment.CollectAlbumFragment;
import com.kosbrother.fragment.CollectSingerFragment;
import com.kosbrother.fragment.CollectSongFragment;
import com.viewpagerindicator.TabPageIndicator;

public class TabCollectActivity extends FragmentActivity {

    private String[]                  CONTENT;
    private ViewPager                 pager;
    private FragmentStatePagerAdapter adapter;
    private TabPageIndicator          indicator;

    private SharedPreferences         sharePreference;
    private final String              keyDeleteDialog = "KeyDeleteDialog";
    public final static String              keyPref         = "pref";
    private Boolean                   showDeleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_tabs);

        sharePreference = getSharedPreferences(keyPref, 0);
        showDeleteDialog = sharePreference.getBoolean(keyDeleteDialog, true);

        Resources res = getResources();
        CONTENT = res.getStringArray(R.array.tabs);

        adapter = new GoogleMusicAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        if (showDeleteDialog) {
            showDeleteDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        this.getParent().onMenuItemSelected(featureId, item);
        return true;
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.reminder)).setIcon(R.drawable.app_icon_72)
                .setMessage(getResources().getString(R.string.delete_item_reminder))
                .setPositiveButton(getResources().getString(R.string.do_not_reminder), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        sharePreference.edit().putBoolean(keyDeleteDialog, false).commit();

                    }

                }).setNegativeButton(getResources().getString(R.string.reminder_next), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }

                }).show();

    }

    class GoogleMusicAdapter extends FragmentStatePagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment kk = new Fragment();
            if (position == 0) {
                kk = CollectSongFragment.newInstance();
            } else if (position == 1) {
                kk = CollectAlbumFragment.newInstance();
            } else if (position == 2) {
                kk = CollectSingerFragment.newInstance();
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

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null && pager != null) {
            int int_page = pager.getCurrentItem();
            adapter = new GoogleMusicAdapter(getSupportFragmentManager());
            pager.setAdapter(adapter);
            indicator.setViewPager(pager);
            pager.setCurrentItem(int_page);
            indicator.setCurrentItem(int_page);
        }
    }

    @Override
    public void onBackPressed() {
        this.getParent().onBackPressed();
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
