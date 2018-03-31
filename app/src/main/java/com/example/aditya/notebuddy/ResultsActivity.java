package com.example.aditya.notebuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ResultsActivity extends AppCompatActivity {

    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        query = getIntent().getStringExtra(Utilities.Results);
        Log.d("View","Value=" + query);

        SearchResultsActivity.showResults(query);


        getSupportActionBar().setTitle("AddNoteActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
