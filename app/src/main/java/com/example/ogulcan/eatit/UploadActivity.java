package com.example.ogulcan.eatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class UploadActivity extends AppCompatActivity {

    EditText yorumText,mekanText;
    ImageView imageView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private StorageReference mStorageRef;
    Uri selected;
    ProgressDialog dialog;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mekanText = findViewById(R.id.mekan_input);
        yorumText = findViewById(R.id.yorum_input);
        imageView = findViewById(R.id.imageView2);

        firebaseDatabase = firebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void upload(View view){
        UUID uuıdImage = UUID.randomUUID();
        String imageName = "images/"+uuıdImage+".jpg";
        StorageReference storageReference = mStorageRef.child(imageName);
        storageReference.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String downloadURL = taskSnapshot.getDownloadUrl().toString();
                FirebaseUser user = mAuth.getCurrentUser();
                String userEmail = user.getEmail().toString();
                String userContent = yorumText.getText().toString();
                String mekan = mekanText.getText().toString();


                FeedActivity b = new FeedActivity();
                String CocukSirasi = String.valueOf(b.timeInt-b.getir());

                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Date date = new Date();
                String dateStr =dateFormat.format(date);

                userID = mAuth.getCurrentUser().getUid();

                mRef.child("Post").child(CocukSirasi).child("usermail").setValue(userEmail);
                mRef.child("Post").child(CocukSirasi).child("comment").setValue(userContent);
                mRef.child("Post").child(CocukSirasi).child("downloadURL").setValue(downloadURL);
                mRef.child("Post").child(CocukSirasi).child("time").setValue(dateStr);
                mRef.child("Post").child(CocukSirasi).child("Mekanadi").setValue(mekan);


                mRef.child("User").child(userID).child(CocukSirasi).child("comment").setValue(userContent);
                mRef.child("User").child(userID).child(CocukSirasi).child("usermail").setValue(userEmail);
                mRef.child("User").child(userID).child(CocukSirasi).child("downloadURL").setValue(downloadURL);
                mRef.child("User").child(userID).child(CocukSirasi).child("time").setValue(dateStr);
                mRef.child("User").child(userID).child(CocukSirasi).child("Mekanadi").setValue(mekan);


                Toast.makeText(getApplicationContext(),"Post Paylaşıldı",Toast.LENGTH_LONG).show();
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),
                        Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //AntiFurkan v1.0
                dialog = ProgressDialog.show(UploadActivity.this,"","Yükleniyor. Lütfen Bekleyin...", true);
            }
        });
    }

    public void chooseImage(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED){
                requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length>0 && grantResults[0] == PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            selected = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selected);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
