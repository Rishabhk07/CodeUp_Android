package com.codingblocks.codeup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codingblocks.codeup.match.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    boolean createMatch =true;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_content)
    CoordinatorLayout root;
    @BindView(R.id.profileImage)
    CircleImageView profileImage;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.progress_text)
    TextView progressText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ButterKnife.bind(profileImage, toolbar);
        setSupportActionBar(toolbar);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(MainActivity.this, IntroActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();
    }

    public void startClicked(View view) {

        findMatch();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.e("TAG", String.valueOf(auth.getCurrentUser().getPhotoUrl()));
            Picasso.with(this).load(auth.getCurrentUser().getPhotoUrl()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(profileImage);
        }
    }

    private void findMatch() {


        progressBar.setVisibility(View.VISIBLE);
        progressText.setText("Finding match");

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("matches");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                   final DataSnapshot snapshot = iterator.next();
                   if(!snapshot.child("user2").exists()) {
                       createMatch = false;
                       snapshot.child("user2").getRef().setValue(new User(uid), new DatabaseReference.CompletionListener() {
                           @Override
                           public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                               startContest(snapshot.getKey());
                           }
                       });

                       return;
                   }
                }

                if(createMatch){
                    createMatch();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createMatch() {

        progressText.setText("Creating new match");

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("matches").push().child("user1").setValue(new User(uid), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                progressText.setText("Finding another player");
            }
        });
    }

    private void startContest(String matchid) {
        Intent intent =new Intent(this, CodeupActivity.class);
        intent.putExtra("match_id",matchid);
        startActivity(intent);
    }
}
