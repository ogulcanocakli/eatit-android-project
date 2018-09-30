package com.example.ogulcan.eatit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ogulcan.eatit.Giris.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    static ArrayList<String> useremailsFromFB;
    static ArrayList<String> userimageFromFB;
    static ArrayList<String> usercommentFromFB;
    static ArrayList<String> timeFromFB;

    TextView mail;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    PostClass adapter;
    ListView listView;
    private FirebaseAuth mAuth;
    String userID;
    private ImageButton imgBtnCikis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mail = (TextView) findViewById(R.id.mailText);
        imgBtnCikis = (ImageButton) findViewById(R.id.imgBtn_cikis);

        useremailsFromFB = new ArrayList<String>();
        usercommentFromFB = new ArrayList<String>();
        userimageFromFB = new ArrayList<String>();
        timeFromFB = new ArrayList<String>();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new PostClass(useremailsFromFB,userimageFromFB,usercommentFromFB, timeFromFB,this);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        mail.setText(mAuth.getCurrentUser().getEmail().toString());

        getDataFromFirebase();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
        else if (item.getItemId() == R.id.info){
            Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.post,menu);

        MenuInflater menuInflater1 = getMenuInflater();
        menuInflater1.inflate(R.menu.info,menu);

        return super.onCreateOptionsMenu(menu);

    }

    protected void getDataFromFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference newReference = firebaseDatabase.getReference("User").child(userID);
        newReference.orderByChild("Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                useremailsFromFB.clear();
                usercommentFromFB.clear();
                userimageFromFB.clear();
                timeFromFB.clear();


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    useremailsFromFB.add(hashMap.get("Mekanadi"));
                    userimageFromFB.add(hashMap.get("downloadURL"));
                    usercommentFromFB.add(hashMap.get("comment"));
                    timeFromFB.add(hashMap.get("time"));

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public void cikis (View view){
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


}

