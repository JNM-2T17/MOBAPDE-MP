package edu.mobapde.selina.shuffle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1500;
    LinearLayout splashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashScreen = (LinearLayout) findViewById(R.id.splashScreen);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent main = new Intent(getBaseContext(), MainActivity.class);
                startActivity(main);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
