package com.kosbrother.lyric;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AdViewUtil {
	
	private static String admobKey = "ca-app-pub-5774496584789671/2433721549";
	
	private static void getAdRequest(final LinearLayout adBannerLayout, Context ctx){

		final AdView adMobAdView = new AdView(ctx);
        adMobAdView.setAdUnitId(admobKey);
        adMobAdView.setAdSize(AdSize.SMART_BANNER);
        adMobAdView.setAdListener(new LogAdListener(){
			@Override
		    public void onAdLoaded() {
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
		                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				if(adMobAdView.getParent() == null)
					adBannerLayout.addView(adMobAdView,params);
			}
		});
		adMobAdView.loadAd(new AdRequest.Builder().build());
				
	}
	
	public static void setAdView(LinearLayout adBannerLayout, Context ctx){
		try {
            Display display = ((Activity) ctx).getWindowManager().getDefaultDisplay();
            int width = display.getWidth(); // deprecated

            if (width > 320) {
            	getAdRequest(adBannerLayout, ctx);
            }
        } catch (Exception e) {

        }
	}

}
