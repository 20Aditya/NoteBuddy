package com.example.aditya.notebuddy;

import android.app.SearchManager;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by aditya on 31/3/18.
 */

public class SearchResultsActivity extends AppCompatActivity {


    private static String year,searchbranch;
    private static Firebase reference;



    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);



        year = getIntent().getStringExtra(Utilities.Year);
        searchbranch = getIntent().getStringExtra(Utilities.searchbranch);


        Log.d("Search","year="+ year + " " + searchbranch);


        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        Log.d("Search", "quesry="+ intent.getStringExtra(Utilities.Results));

        showResults(intent.getStringExtra(Utilities.Results));
    }

    public static void showResults(final String query) {


        reference = new Firebase("https://notebuddy-9b5d4.firebaseio.com/" + year + "/" + searchbranch + "/");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {

                    String key = datas.child("Title").getValue().toString();
                    Log.d("Search", "key=" + key.toLowerCase());
                    Log.d("Search","querylenght="+ String.valueOf(key.length()));
                    if(query.length()<=key.length()) {
                        if (query.toLowerCase().equals(key.toLowerCase().substring(0, query.length()))) {

                            Log.d("Search", "results=" + key);
                        } else {
                            Log.d("Search", "key=" + key);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
}
