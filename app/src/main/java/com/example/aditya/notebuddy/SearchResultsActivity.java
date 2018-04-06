package com.example.aditya.notebuddy;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by aditya on 31/3/18.
 */

public class SearchResultsActivity extends AppCompatActivity {


    private static String year,searchbranch;
    private static Firebase reference;
    private static ArrayList<String> notesresult = new ArrayList<>();
    private static ListView listView;
    private static  ArrayAdapter<String> adapter;
    private static  Context context;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        context = SearchResultsActivity.this;

        listView = (ListView)findViewById(R.id.list);


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


        notesresult.clear();

        reference = new Firebase("https://notebuddy-9b5d4.firebaseio.com/" + year + "/" + searchbranch + "/");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {

                    String key = datas.child("Title").getValue().toString();
                    Log.d("Search", "key=" + key.toLowerCase());
                    Log.d("Search","querylenght="+ String.valueOf(key.length()));
                    if(query.length()<=key.length()) {
                        if (key.toLowerCase().contains(query.toLowerCase())) {

                            Log.d("Search", "results=" + key);
                            notesresult.add(key);
                        } else {
                            Log.d("Search", "key=" + key);
                        }
                    }
                }


                adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, notesresult);
                listView.setAdapter(adapter);

            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
}
