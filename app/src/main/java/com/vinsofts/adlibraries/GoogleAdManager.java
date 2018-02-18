package com.vinsofts.adlibraries;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by macOS on 2/12/18.
 */

public class GoogleAdManager {

    public static final String BANNER_AD = "banner";
    public static final String INTERSTITIAL = "inters";
    public static final String REWARDED_VIDEO = "video";
    private static final String BASE_AD_URL = "http://shippy.yez.vn:1338/";
    private static final String AUTH_TOKEN = "EAAFHmq2uO0EBAGkAUECyj7KzJIT9yiifl1ZCZBSLly820bT3aB1ntZBi0MCpqZBCtKCsaqELBSm24vKFKmmywjEVPewhuYCyZAoXRNFBc0uLiH6XyMZBddYnpW3ZCcb41DigGvf7COJZCTc6u7KKa7dfFYolWhG7nXjbe3GZCz3pidBm22brClvC8";

    private static int intervalTimeShowAdInters;
    private static int totalShownAdInters;
    private InterstitialAd mInterstitialAd;
    private InterstitialAdListener listener;
    private RewardedVideoAd mRewardedVideoAd;
    private boolean disable = false;
    private boolean isTestAd = false;
    private boolean isDestroy = false;
    private boolean request =true;
    private String adUnitId;
    private Context context;
    private VinAdView adView;
    private String typeAd;
    private String adName;


    public static void initialize(int countShowAdInters) {
//        MobileAds.initialize(Utils.getApp(), appId);
        GoogleAdManager.intervalTimeShowAdInters = countShowAdInters;
    }

    public void disableAd(boolean disable) {
        this.disable = disable;
    }

    public GoogleAdManager with(Context context) {
        this.context = context;
        return this;
    }

    public GoogleAdManager setTestAd(boolean isTestAd) {
        this.isTestAd = isTestAd;
        return this;
    }

    public GoogleAdManager into(final VinAdView adView) {
        this.adView = adView;
        return this;
    }

    public GoogleAdManager setTypeAd(String typeAd) {
        this.typeAd = typeAd;
        return this;
    }

    public GoogleAdManager setAdName(String adName) {
        this.adName = adName;
        return this;
    }

    /**
     * Perform request to the server or not
     * @param request (default = true)
     * @return  true: perform request to server to get id
     *          false: set ad unit id from local.
     */
    public  GoogleAdManager setRequestServer(boolean request){
        this.request = request;
        return this;
    }

    /**
     * Set ad unit id. Just works when request = false
     * @param adUnitId
     * @return
     */
    public GoogleAdManager setAdUnitId(String adUnitId){
        this.adUnitId = adUnitId;
        return this;
    }

    /**
     * Finish all initial.
     */
    public void build() {
        if (TextUtils.isEmpty(typeAd)) throw new RuntimeException("Please prodvide a type ad");
        if (TextUtils.isEmpty(adName)) throw new RuntimeException("Please prodvide a ad name");
        if (typeAd.equals(BANNER_AD) && adView == null)
            throw new NullPointerException("Please init adView");

        totalShownAdInters++;
        if (typeAd.equals(INTERSTITIAL) && totalShownAdInters % intervalTimeShowAdInters != 0)
            return;

        if(request){
            requestAdId();
        } else {
            if(TextUtils.isEmpty(adUnitId)) throw new RuntimeException("Please prodvide ad unit id");
            createAds(typeAd, adUnitId);
        }

    }

    /**
     * Perform request to the server to get ad unit id
     */
    private void requestAdId() {
        Toast.makeText(context, "request Ad id", Toast.LENGTH_SHORT).show();
        RetrofitClient.getRetrofit(BASE_AD_URL).requestAdId(AUTH_TOKEN, context.getPackageName(), typeAd, adName)
                .enqueue(new Callback<GoogleAdResponse>() {
                    @Override
                    public void onResponse(Call<GoogleAdResponse> call, Response<GoogleAdResponse> response) {
                        Toast.makeText(context, "onResponse", Toast.LENGTH_SHORT).show();
                        if (isDestroy) return;
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() != 200) return;

                            createAds(response.body().getGoogleAd().getAdType(),
                                    response.body().getGoogleAd().getAdCode());

                        }
                    }

                    @Override
                    public void onFailure(Call<GoogleAdResponse> call, Throwable t) {
                        if (isDestroy) return;
                        System.out.print(t.getMessage());
                        Toast.makeText(context, "onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Create and show ad.
     * @param adType
     * @param adUnitId
     */
    private void createAds(String adType, String adUnitId) {
        switch (adType) {
            case BANNER_AD:
                createBannerAdPromatically(adView, adUnitId);
                break;

            case INTERSTITIAL:
                createInterstitial(adUnitId);
                showInterstitialAd();
                break;

            case REWARDED_VIDEO:
                break;
        }
    }

    /**
     *
     * @param viewGroup VinAdView
     * @param adUnitId
     */
    private void createBannerAdPromatically(VinAdView viewGroup, String adUnitId) {
        if (disable) return;

        final AdView adView = new AdView(context);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(adUnitId);
        adView.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        adView.setLayoutParams(params);
        viewGroup.addView(adView);

        AdRequest.Builder adRequest = new AdRequest.Builder();

        if (isTestAd) adRequest.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

        adView.loadAd(adRequest.build());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void createInterstitial(String adUnitId) {
        final AdRequest.Builder adRequest = new AdRequest.Builder();

        if (isTestAd) adRequest.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(adUnitId);
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                showInterstitialAd();
                if (listener != null) listener.onAdLoaded();
            }
        });

        mInterstitialAd.loadAd(adRequest.build());
    }


    private void showInterstitialAd() {
        if (disable) return;

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    public GoogleAdManager interstitialAdListener(InterstitialAdListener listener) {
        this.listener = listener;
        return this;
    }

//
//
//
//    //REWARDED VIDEO AD
//    public void initRewardVideo(String adUnitId, RewardedVideoAdListener rewardedVideoAdListener){
//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
//        mRewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
//        loadRewardedVideo(adUnitId);
//    }
//
//    public void loadRewardedVideo(String adUnitId){
//        AdRequest.Builder adRequest = new AdRequest.Builder();
//        if(isTestAd) adRequest.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
//
//        mRewardedVideoAd.loadAd(adUnitId, adRequest.build());
//    }
//
//    public void showRewardedVideo() {
//        if(disable) return;
//
//        if (mRewardedVideoAd.isLoaded()) {
//            mRewardedVideoAd.show();
//        } else {
//            Log.d("TAG", "The rewarded video wasn't loaded yet.");
//        }
//    }
//
//    public void  pauseRewardedVideo(){
//        mRewardedVideoAd.pause(context);
//    }
//
//    public void resumeRewardedVideo(){
//        mRewardedVideoAd.resume(context);
//    }
//
//    public void distroyRewardedVideo(){
//        mRewardedVideoAd.destroy(context);
//    }
//


    public void onDestroy() {
        isDestroy = true;
        mInterstitialAd = null;

    }
}
