package com.example.aditya.notebuddy;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LaunchingActivity extends AppCompatActivity {

    static Button btn1, btn2, btn3, btn4;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);


        textView = (TextView)findViewById(R.id.textView);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(LaunchingActivity.this, R.anim.bounce);
                btn1.startAnimation(myAnim);
                Intent yearintent = new Intent(LaunchingActivity.this,MainActivity.class);
                yearintent.putExtra(Utilities.Year,"First");
                startActivity(yearintent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                btn2.startAnimation(myAnim);

                Intent yearintent = new Intent(LaunchingActivity.this,MainActivity.class);
                yearintent.putExtra(Utilities.Year,"Second");
                startActivity(yearintent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                btn3.startAnimation(myAnim);

                Intent yearintent = new Intent(LaunchingActivity.this,MainActivity.class);
                yearintent.putExtra(Utilities.Year,"Third");
                startActivity(yearintent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                btn4.startAnimation(myAnim);
                Intent yearintent = new Intent(LaunchingActivity.this,MainActivity.class);
                yearintent.putExtra(Utilities.Year,"Fourth");
                startActivity(yearintent);
            }
        });

        getSupportActionBar().hide();

    }
}
