package com.kosbrother.lyric;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kosbrother.lyric.api.LyricAPI;

import java.io.IOException;

public class MainTabActivty extends TabActivity {

    private TabHost mTabHost;
    private AlertDialog.Builder aboutUsDialog;
    private final String  adWhirlKey = Setting.adwhirlKey;
    
    
    //gcm
    public static final String EXTRA_MESSAGE = "message";
    private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTimeMs";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_DEVICE_ID = "device_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    /**
     * Default lifespan (7 days) of a reservation until it is considered expired.
     */
    public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;
    
    /**
     * Substitute you own sender ID here.
     */
    String SENDER_ID = "1037018589447";
	private Context context;
	String regid;
	GoogleCloudMessaging gcm;
	static final String TAG = "GCMDemo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabbarscreen);

//        SQLiteLyric db = new SQLiteLyric(this);
//        db.getAllAlbums();
//        db.getAllSingers();
//        db.getAllSongs();

        mTabHost = getTabHost();

        setupTab(TabHotActivity.class, "最新熱門", R.drawable.icon_tab_hot);
        setupTab(TabSingerActivity.class, "歌手列表", R.drawable.icon_tab_singer);
        setupTab(TabSearchActivity.class, "搜索", R.drawable.icon_tab_search);
        setupTab(TabCollectActivity.class, "收藏", R.drawable.icon_tab_box);
        
        setAboutUsDialog();
        
        AdViewUtil.setAdView((LinearLayout) findViewById(R.id.adonView), this);
        
        context = getApplicationContext();
        regid = getRegistrationId(context);
        String device_id = getDeviceId(context);

        if (regid.length() == 0 || device_id.length() == 0) {
            registerBackground();
        }
        gcm = GoogleCloudMessaging.getInstance(this);


    }

    private void setupTab(Class<?> ccls, String name, Integer iconId) {
        Intent intent = new Intent().setClass(this, ccls);

        View tab = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        ImageView image = (ImageView) tab.findViewById(R.id.icon);
        TextView text = (TextView) tab.findViewById(R.id.text);
        if (iconId != null) {
            image.setImageResource(iconId);
        }
        text.setText(name);
        TabSpec spec = mTabHost.newTabSpec(name).setIndicator(tab).setContent(intent);
        mTabHost.addTab(spec);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mTabHost.getCurrentTab() == 0) {
            finish();
        } else {
            mTabHost.setCurrentTab(0);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
//        case R.id.action_settings: // setting
//            Toast.makeText(MainTabActivty.this, "SEARCH", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
//            startActivity(intent);
//            break;
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
    
    // gcm use start from here
    
    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.length() == 0) {
            Log.v(TAG, "Registration not found.");
            return "";
        }
        // check if app was updated; if so, it must clear registration id to
        // avoid a race condition if GCM sends a message
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion || isRegistrationExpired(context)) {
            Log.v(TAG, "App version changed or registration expired.");
            return "";
        }
        return registrationId;
    }
    public static String getDeviceId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String deviceId = prefs.getString(PROPERTY_DEVICE_ID, "");
        return deviceId;
    }
    
    private static SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(TabCollectActivity.keyPref, 0);
    }
    
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    
    private static boolean isRegistrationExpired(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        // checks if the information is not stale
        long expirationTime =
                prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);
        return System.currentTimeMillis() > expirationTime;
    }
    
    private void registerBackground() {
        new AsyncTask() {

			@Override
			protected String doInBackground(Object... params) {
				String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                regid = gcm.register(SENDER_ID);
	                msg = "Device registered, registration id=" + regid;
	                String deviceId = Settings.Secure.getString(MainTabActivty.this.getContentResolver(),Settings.Secure.ANDROID_ID); 
	                LyricAPI.sendRegistrationId(regid,deviceId);    
	                setRegistrationId(context, regid,deviceId);
	            } catch (IOException ex) {
	                msg = "Error :" + ex.getMessage();
	            }
	            return msg;
			}

			private void setRegistrationId(Context context, String regid,String deviceId) {
				final SharedPreferences prefs = getGCMPreferences(context);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(PROPERTY_REG_ID, regid);
				editor.putString(PROPERTY_DEVICE_ID, deviceId);
				editor.putInt(PROPERTY_APP_VERSION, getAppVersion(context));
				editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, REGISTRATION_EXPIRY_TIME_MS + System.currentTimeMillis());
				editor.commit();
				
			}
            
        }.execute(null, null, null);
    }


}
