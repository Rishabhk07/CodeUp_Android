package com.codingblocks.codeup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by naman on 21/6/17.
 */

public class CodeupActivity extends AppCompatActivity {

    Editor editor;
    TextView tvTitle;
    View codelayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codeup);

        editor = (Editor) findViewById(R.id.editor);
        tvTitle = (TextView) findViewById(R.id.tv_question_title);
        codelayout = findViewById(R.id.code_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        fetchQuestions();
    }

    private void fetchQuestions(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("questions");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                List<Question> questions =new ArrayList<>();
                while (iterator.hasNext()){
                    DataSnapshot snapshot = iterator.next();
                    Question question = snapshot.getValue(Question.class);
                    questions.add(question);
                }

                showQuestion(questions.get(0));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("lol",databaseError.getDetails());
            }
        });
    }

    private void showQuestion(Question question) {
        progressBar.setVisibility(View.GONE);
        codelayout.setVisibility(View.VISIBLE);

        tvTitle.setText(question.getStatement());

        if (question.getStub()!=null){
            editor.setText(question.getStub());
        }
    }
}
