package com.java.prj.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Marks extends AppCompatActivity {

    TextView marks;
    MaterialButton home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);

        marks = (TextView) findViewById(R.id.marks);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/students/" + SplashScreen.user.getUid() +"/quizes");
        ref.child(MainActivity.quiz.qid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                marks.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        home = (MaterialButton) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                startActivity(new Intent(Marks.this, MainActivity.class));
            }
        });
    }
}