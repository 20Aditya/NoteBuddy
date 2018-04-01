package com.example.aditya.notebuddy;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ViewPublicNoteActivity extends AppCompatActivity implements View.OnClickListener {


    String title, downloadURL;
    TextView textView3,textView5,textView7;
    File localFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_public_note);

        title = getIntent().getStringExtra(Utilities.Title);
        Log.d("View","Value=" + title);

        textView3 = (TextView)findViewById(R.id.textView3);
        textView5 = (TextView)findViewById(R.id.textView5);
        textView7 = (TextView)findViewById(R.id.textView7);


        setTitle("Requested Note");

        Firebase reference = new Firebase("https://notebuddy-9b5d4.firebaseio.com/Public Notes/" + title);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textView3.setText(dataSnapshot.child("Title").getValue().toString());
                textView5.setText(dataSnapshot.child("Description").getValue().toString());
                textView7.setText(dataSnapshot.child("File name").getValue().toString());
                downloadURL = dataSnapshot.child("File URL").getValue().toString();
                Log.d("ViewNote", "DURL = " + downloadURL);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        findViewById(R.id.button2).setOnClickListener(this);

        getSupportActionBar().setTitle("AddNoteActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


                /*Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }*/


        findViewById(R.id.button1).setOnClickListener(this);

    }


    private void downloadFile() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://notebuddy-9b5d4.appspot.com/uploads");
        StorageReference  islandRef = storageRef.child(textView7.getText().toString());

        String str = textView7.getText().toString();
        str.replace(".pdf", "");
        int index = str.indexOf(".");
        String f = str.substring(0,index);
        Log.d("View","Str=" + f);

        localFile  = null;
        try {
            File directory = new File(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());

            localFile = File.createTempFile(f,".pdf", directory);

            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.e("firebase ", ";local item file created  created " + localFile.toString());
                    Toast.makeText(ViewPublicNoteActivity.this, "File Downloaded Successfully", Toast.LENGTH_SHORT).show();
                    //  updateDb(timestamp,localFile.toString(),position);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("firebase ", ";local tem file not created  created " + exception.toString());
                }
            });
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void share(){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "This is the download URL of: " + textView7.getText().toString() + "\n" + downloadURL;
        Log.d("ViewNote", "content= " + shareBody);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button2:
                downloadFile();
                break;

            case R.id.button1:
                share();
                break;
        }

    }

}
