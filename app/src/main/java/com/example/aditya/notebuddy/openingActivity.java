package com.example.aditya.notebuddy;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class openingActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);


        imageView = (ImageView)findViewById(R.id.splashscreen);
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {

                startActivity(new Intent(openingActivity.this, LaunchingActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, secondsDelayed * 1000);

        getSupportActionBar().hide();
    }
}
