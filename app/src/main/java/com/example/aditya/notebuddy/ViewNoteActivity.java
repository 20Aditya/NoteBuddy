package com.example.aditya.notebuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ViewNoteActivity extends AppCompatActivity {

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        title = getIntent().getStringExtra(Utilities.Title);
        Log.d("View","Value=" + title);




    }
}
