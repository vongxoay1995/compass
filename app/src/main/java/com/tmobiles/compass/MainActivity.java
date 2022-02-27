package com.tmobiles.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Compass compass;
    private SOTWFormatter sotwFormatter;
    private float currentAzimuth;
    private ImageView ic_rotate;
    private RelativeLayout layoutAds;
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ic_rotate = findViewById(R.id.img_compass_rotate);
        layoutAds = findViewById(R.id.layout_ads);
        MobileAds.initialize(this, initializationStatus -> {
        });
        if (BuildConfig.DEBUG) {
            List<String> testDeviceIds = Arrays.asList("C672C9D51F65E8B9B0345F9F8E4F7CC1");
            RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
            MobileAds.setRequestConfiguration(configuration);
        }
        sotwFormatter = new SOTWFormatter(this);
        compass = new Compass(this);
        Compass.CompassListener cl = getCompassListener();
        compass.setListener(cl);
        showAds();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
    }
    private Compass.CompassListener getCompassListener() {
        return azimuth -> runOnUiThread(() -> {
            adjustArrow(azimuth);
            // adjustSotwLabel(azimuth);
        });
    }

    private void adjustArrow(float azimuth) {
        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = azimuth;
        an.setDuration(0);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        ic_rotate.startAnimation(an);
    }
    public void showAds(){
        adView = new com.google.android.gms.ads.AdView(this);
        com.google.android.gms.ads.AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        layoutAds.addView(adView);
        AdRequest bannerAdRequest = new AdRequest.Builder().build();
        adView.loadAd(bannerAdRequest);
        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
    }
    private com.google.android.gms.ads.AdSize getAdSize() {
        Display display =getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}