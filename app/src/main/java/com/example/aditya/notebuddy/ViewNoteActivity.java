package com.example.aditya.notebuddy;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ViewNoteActivity extends AppCompatActivity implements View.OnClickListener {

    String title,branch,year;
    TextView textView3,textView5,textView7;
    File localFile;
    String downloadURL;
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        Intent in = getIntent();
        title = in.getStringExtra(Utilities.Title);
        branch = in.getStringExtra(Utilities.Branch);
        year = in.getStringExtra(Utilities.Year);


        setTitle("Requested Note");

        Log.d("View","Value=" + title);

        textView3 = (TextView)findViewById(R.id.textView3);
        textView5 = (TextView)findViewById(R.id.textView5);
        textView7 = (TextView)findViewById(R.id.textView7);


        if(branch==null || branch=="branch") {

            Log.d("View", "branch=" + branch);

            Firebase reference = new Firebase("https://notebuddy-9b5d4.firebaseio.com/" + year + "/Information Technology/" + title);
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

        }else{

            Log.d("View", "branch=" + branch);
            Firebase reference = new Firebase("https://notebuddy-9b5d4.firebaseio.com/" + year + "/" + branch + "/" + title);
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

        }
        findViewById(R.id.button2).setOnClickListener(this);

        button1 = (Button)findViewById(R.id.button1);

        getSupportActionBar().setTitle("AddNoteActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        String type = str.substring(index,str.length());
        Log.d("View","Str=" + str.substring(index,str.length()));


        localFile  = null;
        try {
            File directory = new File(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());

             localFile = File.createTempFile(f,type, directory);

            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.e("firebase ", ";local item file created  created " + localFile.toString());
                    Toast.makeText(ViewNoteActivity.this, "File Downloaded Successfully", Toast.LENGTH_SHORT).show();
                    //  updateDb(timestamp,localFile.toString(),position);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("firebase ", ";local tem file not created  created " + exception.toString());
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.linearlayout),(int)progress + "% Downloaded...", Snackbar.LENGTH_LONG);
                    snackbar.show();


                }
            });
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void share(){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "This is the download URL of: " + textView7.getText().toString() + "\n" + downloadURL + " \n Click On the link to download automatically";
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
                final Animation myAnim = AnimationUtils.loadAnimation(ViewNoteActivity.this, R.anim.bounce);
                button1.startAnimation(myAnim);
                share();
                break;
        }

    }



}
