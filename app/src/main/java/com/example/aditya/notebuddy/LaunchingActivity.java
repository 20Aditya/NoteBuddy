package com.example.aditya.notebuddy;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

public class LaunchingActivity extends AppCompatActivity {

    RadioButton radio,radio2,radio3,radio4;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);


        radio = (RadioButton)findViewById(R.id.radio);
        radio2 = (RadioButton)findViewById(R.id.radio2);
        radio3 = (RadioButton)findViewById(R.id.radio3);
        radio4 = (RadioButton)findViewById(R.id.radio4);

        button = (Button)findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radio.isChecked()){
                    Intent yearintent = new Intent(LaunchingActivity.this,MainActivity.class);
                    yearintent.putExtra(Utilities.Year,"First");
                    startActivity(yearintent);
                }else if(radio2.isChecked()){
                    Intent yearintent = new Intent(LaunchingActivity.this,MainActivity.class);
                    yearintent.putExtra(Utilities.Year,"Second");
                    startActivity(yearintent);
                }else if(radio3.isChecked()){
                    Intent yearintent = new Intent(LaunchingActivity.this,MainActivity.class);
                    yearintent.putExtra(Utilities.Year,"Third");
                    startActivity(yearintent);
                }else if(radio4.isChecked()){
                    Intent yearintent = new Intent(LaunchingActivity.this,MainActivity.class);
                    yearintent.putExtra(Utilities.Year,"Fourth");
                    startActivity(yearintent);
                }else{
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.relativeLayout),"Please Select an Option", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });



    }
}
