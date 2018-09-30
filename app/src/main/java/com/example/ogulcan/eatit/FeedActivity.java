package com.example.ogulcan.eatit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedActivity extends AppCompatActivity {

    static ArrayList<String> useremailsFromFB;
    static ArrayList<String> userimageFromFB;
    static ArrayList<String> usercommentFromFB;
    static ArrayList<String> timeFromFB;
    ArrayList<String> Long1;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    PostClass adapter;
    ListView listView;
    int timeInt = 999999999;
    static int son;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        useremailsFromFB = new ArrayList<String>();
        usercommentFromFB = new ArrayList<String>();
        userimageFromFB = new ArrayList<String>();
        timeFromFB = new ArrayList<String>();
        Long1 = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new PostClass(useremailsFromFB, userimageFromFB, usercommentFromFB, timeFromFB,this);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        getDataFromFirebase();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
        else if (item.getItemId() == R.id.profile){
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile, menu);

        MenuInflater menuInflater1 = getMenuInflater();
        menuInflater1.inflate(R.menu.post, menu);


        return super.onCreateOptionsMenu(menu);
    }

    protected void getDataFromFirebase() {

        DatabaseReference newReference = firebaseDatabase.getReference("Post");
        newReference.orderByChild("Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                useremailsFromFB.clear();
                usercommentFromFB.clear();
                userimageFromFB.clear();
                timeFromFB.clear();
                Long1.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    useremailsFromFB.add(hashMap.get("Mekanadi"));
                    userimageFromFB.add(hashMap.get("downloadURL"));
                    usercommentFromFB.add(hashMap.get("comment"));
                    timeFromFB.add(hashMap.get("time"));
                    Long1.add(hashMap.get("usermail"));

                    adapter.notifyDataSetChanged();
                }
                son = Long1.size();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public int getir() {
        return son;
    }

}
