package com.zrius.game;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdPubManager {

    public static final String TAG = AdPubManager.class.getSimpleName();

    private InterstitialAd mInterstitialAd;
    private Activity  mContext;

    public AdPubManager(Activity mContext) {
        this.mContext = mContext;
        InitInterstitialAd(mContext);
    }

    public  void InitInterstitialAd(Context context){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }

    public void ShowInterstitialAd(){
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Log.i(TAG, "onAdFailedToShowFullScreenContent "+adError);
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    Log.i(TAG, "onAdShowedFullScreenContent ");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Log.i(TAG, "onAdDismissedFullScreenContent ");
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    Log.i(TAG, "onAdImpression ");
                    InitInterstitialAd(mContext);
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    Log.i(TAG, "onAdClicked ");
                }
            });
            mInterstitialAd.show(mContext);
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.");
        }
    }

    public void ShowInterstitialAd(FullScreenContentCallback fullScreenContentCallback){
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(fullScreenContentCallback);
            mInterstitialAd.show(mContext);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
}
