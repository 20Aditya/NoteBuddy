package com.example.aditya.notebuddy;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reginald.editspinner.EditSpinner;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener{

    final static int PICK_PDF_CODE = 2342;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    EditText editTextFilename;
    String descriptionname, branchname;
    String titlename;
    Uploads upload;
    String name;
    TextInputLayout title, description, branch;
    String year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        year = getIntent().getStringExtra(Utilities.Year);
        Log.d("AddNote", "Year = " + year);

         title = (TextInputLayout)findViewById(R.id.titleWrapper);
         description = (TextInputLayout)findViewById(R.id.descriptionlWrapper);
         branch = (TextInputLayout)findViewById(R.id.branchWrapper);

        title.setHint("Title");
        description.setHint("Description");
        branch.setHint("Branch");

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //get the spinner from the xml.
        EditSpinner mEditSpinner = (EditSpinner) findViewById(R.id.edit_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.Branches));
        mEditSpinner.setAdapter(adapter);

        editTextFilename = (EditText) findViewById(R.id.filename);


        findViewById(R.id.buttonUploadFile).setOnClickListener(this);
        findViewById(R.id.submitbutton).setOnClickListener(this);

        getSupportActionBar().setTitle("AddNoteActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void getPDF() {
        //for greater than lollipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf|application/vnd.ms-powerpoint|application/vnd.openxmlformats-officedocument.wordprocessingml.document|application/vnd.oasis.opendocument.spreadsheet");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //this method is uploading the file
    //the code is same as the previous tutorial
    //so we are not explaining it
    private void uploadFile(final Uri data) {

        StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + data.getLastPathSegment());
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddNoteActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        upload = new Uploads(editTextFilename.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                        name = taskSnapshot.getMetadata().getName();
                        editTextFilename.setText(name);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.linear),(int)progress + "% Uploading...", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                });

    }


    public void submit(){

        if(title.getEditText()!= null)
            titlename = title.getEditText().getText().toString();
        if(description.getEditText()!= null)
            descriptionname = description.getEditText().getText().toString();
        if(branch.getEditText()!= null)
            branchname = branch.getEditText().getText().toString();

        Log.d("AddNoteActivity","Titlename = " + branchname);

        if(!branchname.equals("Public Notes") ) {
            databaseReference.child(year).child(branchname).child(titlename).child("Title").setValue(titlename);
            databaseReference.child(year).child(branchname).child(titlename).child("Description").setValue(descriptionname);
            databaseReference.child(year).child(branchname).child(titlename).child("File name").setValue(name);
            databaseReference.child(year).child(branchname).child(titlename).child("File URL").setValue(upload.getUrl());
        }else{
            databaseReference.child(branchname).child(titlename).child("Title").setValue(titlename);
            databaseReference.child(branchname).child(titlename).child("Description").setValue(descriptionname);
            databaseReference.child(branchname).child(titlename).child("File name").setValue(name);
            databaseReference.child(branchname).child(titlename).child("File URL").setValue(upload.getUrl());
        }


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.buttonUploadFile:
                getPDF();
                break;
            case R.id.submitbutton:
                submit();
                Intent intent = new Intent(AddNoteActivity.this,MainActivity.class);
                intent.putExtra(Utilities.Year,year);
                startActivity(intent);
                finish();
                break;
        }
    }
}
