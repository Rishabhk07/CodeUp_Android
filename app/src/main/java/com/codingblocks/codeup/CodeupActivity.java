package com.codingblocks.codeup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codingblocks.codeup.match.MatchQuestion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by naman on 21/6/17.
 */

public class CodeupActivity extends AppCompatActivity {

    Editor editor;
    TextView tvTitle, tvNumber;
    View codelayout;
    ProgressBar progressBar;
    Button submit;

    List<Question> contestQuestions = new ArrayList<>();
    int currentQuestion = 0;

    List<MatchQuestion> answeredQuestions = new ArrayList<>();
    String matchid;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codeup);
        matchid = getIntent().getStringExtra("match_id");
        editor = (Editor) findViewById(R.id.editor);
        tvTitle = (TextView) findViewById(R.id.tv_question_title);
        tvNumber = (TextView)findViewById(R.id.tv_question_number);
        codelayout = findViewById(R.id.code_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        submit = (Button) findViewById(R.id.btn_submit);
        pDialog = new ProgressDialog(this);
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
                    question.setId(snapshot.getKey());
                    questions.add(question);
                }

                Collections.shuffle(questions);

                for (int i=0; i<3; i++) {
                    contestQuestions.add(questions.get(i));
                }

                progressBar.setVisibility(View.GONE);
                codelayout.setVisibility(View.VISIBLE);
                showQuestion();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CodeupActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showQuestion() {

        Question question = contestQuestions.get(currentQuestion);

        tvTitle.setText(question.getStatement());
        tvNumber.setText("Question "+ String.valueOf(currentQuestion+1));
        if (question.getStub()!=null){
            editor.setText(question.getStub());
        }
    }

    public void submitClicked(View v) {

        MatchQuestion matchQuestion =new MatchQuestion(editor.getText().toString(), contestQuestions.get(currentQuestion).getId());
        submitQuestion(matchQuestion);



    }

    private void submitQuestion(final MatchQuestion matchQuestion) {

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        pDialog.setMessage("Submitting");
        pDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("matches").child(matchid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()){
                    DataSnapshot snapshot = iterator.next();
                    if (snapshot.child("id").getValue(String.class).equals(uid)) {
                        switch (currentQuestion) {
                            case 0:
                                setQuestion(matchQuestion, snapshot.getRef().child("q1"));
                                break;
                            case 1:
                                setQuestion(matchQuestion, snapshot.getRef().child("q2"));
                                break;
                            case 2:
                                setQuestion(matchQuestion, snapshot.getRef().child("q3"));
                                break;
                        }
                        return;
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setQuestion(MatchQuestion matchQuestion, DatabaseReference ref) {
        ref.setValue(matchQuestion, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (currentQuestion >= contestQuestions.size() - 1 ){
                    //questions finished
                    Intent intent = new Intent(CodeupActivity.this, ScoreActivity.class);
                    intent.putExtra("match_id",matchid);
                    startActivity(intent);
                    return;
                }

                pDialog.hide();
                currentQuestion++;
                showQuestion();
            }
        });

    }
}
