package com.java.prj.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Authors extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ContainerClass.Quiz> quizList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authors);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(Authors.this));
        recyclerView.setHasFixedSize(true);

        String topic = (String) getIntent().getExtras().get("topic");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("quiz");
        ref.child(topic).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    quizList.add(ds.getValue(ContainerClass.Quiz.class));
                }
                Adapter adapter = new Adapter(quizList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class Adapter extends RecyclerView.Adapter<Authors.Adapter.ProductViewHolder> {

        private List<ContainerClass.Quiz> list;


        public Adapter(List<ContainerClass.Quiz> list) {
            this.list = list;
        }

        @Override
        public Authors.Adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(Authors.this);
            View view = inflater.inflate(R.layout.card_topic, parent , false);
            return new Authors.Adapter.ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(Authors.Adapter.ProductViewHolder holder, final int position) {
            //getting the product of the specified position
            final ContainerClass.Quiz quiz = list.get(position);

            holder.topic.setText(quiz.creator);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.quiz = quiz;
                    startActivity(new Intent(Authors.this, Rules.class));
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        class ProductViewHolder extends RecyclerView.ViewHolder {

            TextView topic;

            public ProductViewHolder(View itemView) {
                super(itemView);

                topic = itemView.findViewById(R.id.topic);
            }
        }
    }
}