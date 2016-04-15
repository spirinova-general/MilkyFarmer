package com.milky.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.milky.R;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;

public class SplashScreen extends Activity {
    private Animation _fadeIn, _fadeOut;
    private int SPLASH_DISPLAY_TIMER = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final ImageView logo = (ImageView) findViewById(R.id.logo);
        _fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        _fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadein);
        _fadeIn.setDuration(1200);
        _fadeIn.setFillAfter(true);

        _fadeOut = new AlphaAnimation(1.0f, 0.0f);
        _fadeOut.setDuration(1200);
        _fadeOut.setFillAfter(true);
        logo.startAnimation(_fadeIn);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.startAnimation(_fadeOut);
                if (new DatabaseHelper(SplashScreen.this).isTableNotEmpty(TableNames.ACCOUNT)) {

                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);

                }
                else
                {
                    Intent i = new Intent(SplashScreen.this, SignUp.class);
                    startActivity(i);

                }
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_TIMER);
    }

}
