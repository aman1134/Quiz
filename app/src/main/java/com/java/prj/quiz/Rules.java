package com.java.prj.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Rules extends AppCompatActivity {

    Button startQuiz ;
    ProgressBar loadingQuiz;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        sharedPreferences = Rules.this.getSharedPreferences("user", MODE_PRIVATE);

        loadingQuiz = (ProgressBar) findViewById(R.id.loadingQuiz);
        loadingQuiz.setVisibility(View.INVISIBLE);
        startQuiz = (Button) findViewById(R.id.startQuiz);
        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz.setVisibility(View.INVISIBLE);
                loadingQuiz.setVisibility(View.VISIBLE);
                if(sharedPreferences.getString("name", "").isEmpty() || sharedPreferences.getString("img", "").isEmpty()){
                    Intent intent = new Intent(Rules.this, MainActivity.class);
                    intent.putExtra("act", "img");
                    startActivity(intent);
                }else {
                    DatabaseReference user = FirebaseDatabase.getInstance().getReference("users/students/" + SplashScreen.user.getUid());
                    user.child("quizes").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(MainActivity.quiz.qid)) {
                                Toast.makeText(Rules.this, "You have taken the Quiz.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Rules.this, Marks.class));
                            } else {
                                Intent intent = new Intent(Rules.this, Test.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        startQuiz.setVisibility(View.VISIBLE);
        loadingQuiz.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        startQuiz.setVisibility(View.VISIBLE);
        loadingQuiz.setVisibility(View.INVISIBLE);
    }
}