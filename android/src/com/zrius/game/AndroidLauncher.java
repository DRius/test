package com.zrius.game;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AndroidLauncher extends AndroidApplication {

	private  AdPubManager adPubManager;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//init pub
		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
			}
		});

		adPubManager = new AdPubManager(this);


		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new FlappyBrid(), config);
		FlappyBrid.setListenerOnGameOver(new FlappyBrid.ListernerOnGameOver() {
			@Override
			public void onGameOver() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(adPubManager != null){
							adPubManager.ShowInterstitialAd();
						}
					}
				});
			}
		});
	}
}
