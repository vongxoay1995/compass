package com.tmobiles.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Compass compass;
    private SOTWFormatter sotwFormatter;
    private float currentAzimuth;
    private ImageView ic_rotate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ic_rotate = findViewById(R.id.img_compass_rotate);
        sotwFormatter = new SOTWFormatter(this);
        compass = new Compass(this);
        Compass.CompassListener cl = getCompassListener();
        compass.setListener(cl);
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
}