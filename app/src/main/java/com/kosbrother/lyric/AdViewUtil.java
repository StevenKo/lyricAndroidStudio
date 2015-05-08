package com.kosbrother.lyric;


import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

public class AdViewUtil {

	private static String admobKey = "ca-app-pub-5774496584789671/2433721549";
	private static String mopubKey = "9a4c985738d347e5a71b4733e9af0066";
	
//	private static void getAdRequest(final LinearLayout adBannerLayout, Context ctx){
//
//		final AdView adMobAdView = new AdView(ctx);
//        adMobAdView.setAdUnitId(admobKey);
//        adMobAdView.setAdSize(AdSize.SMART_BANNER);
//        adMobAdView.setAdListener(new LogAdListener(){
//			@Override
//		    public void onAdLoaded() {
//				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//		                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//				if(adMobAdView.getParent() == null)
//					adBannerLayout.addView(adMobAdView,params);
//			}
//		});
//		adMobAdView.loadAd(new AdRequest.Builder().build());
//
//	}
//
//	public static void setAdView(LinearLayout adBannerLayout, Context ctx){
//		try {
//            Display display = ((Activity) ctx).getWindowManager().getDefaultDisplay();
//            int width = display.getWidth(); // deprecated
//
//            if (width > 320) {
//            	getAdRequest(adBannerLayout, ctx);
//            }
//        } catch (Exception e) {
//
//        }
//	}

	public static MoPubView setAdView(LinearLayout bannerAdView, Context ctx) {
		try {
			Display display = ((Activity) ctx).getWindowManager().getDefaultDisplay();
			int width = display.getWidth(); // deprecated

			if (width > 320) {
				return getBannerAdRequest(bannerAdView,ctx);
			}
		} catch (Exception e) {

		}
		return null;
	}

	private static MoPubView getBannerAdRequest(final LinearLayout bannerAdView, Context ctx) {
		final MoPubView moPubView = new MoPubView(ctx);
		moPubView.setAdUnitId(mopubKey);
		moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
			@Override
			public void onBannerLoaded(MoPubView banner) {
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				if (moPubView.getParent() == null)
					bannerAdView.addView(moPubView, params);
			}

			@Override
			public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {

			}

			@Override
			public void onBannerClicked(MoPubView banner) {

			}

			@Override
			public void onBannerExpanded(MoPubView banner) {

			}

			@Override
			public void onBannerCollapsed(MoPubView banner) {

			}
		});

		moPubView.loadAd();

		return moPubView;
	}

}
