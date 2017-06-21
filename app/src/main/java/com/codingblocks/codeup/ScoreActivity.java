package com.codingblocks.codeup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by naman on 21/6/17.
 */

public class ScoreActivity extends AppCompatActivity {

    private String matchid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        matchid = getIntent().getStringExtra("match_id");

        if ( matchid!= null){
            fetchScore();
        }
    }

    private void fetchScore() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = database.getReference("matches").child(matchid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()){
                    DataSnapshot snapshot = iterator.next();
                    if (snapshot.child("id").getValue(String.class).equals(uid)) {
                       addScoreListener(snapshot.getRef().child("total"));
                        return;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addScoreListener(DatabaseReference ref) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int total = dataSnapshot.getValue(Integer.class);
                if (total>=0) {
                    Toast.makeText(ScoreActivity.this,"Score is "+String.valueOf(total), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
