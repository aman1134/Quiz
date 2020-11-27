package com.java.prj.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Test extends AppCompatActivity implements LifecycleObserver {

    RecyclerView rv ;
    TextView question, question_no, time;
    RadioButton op1, op2, op3;
    RadioGroup radioGroup;
    MaterialButton next;
    ContainerClass.Quiz quiz;
    List<ContainerClass.Question> questionList;
    int q = 0, timer, marks = 0;
    Timer T;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(Test.this);

        quiz = MainActivity.quiz;
        questionList = quiz.getqList();

        rv = (RecyclerView) findViewById(R.id.recyclerview);
        question = (TextView) findViewById(R.id.question);
        time = (TextView) findViewById(R.id.timer);
        question_no = (TextView) findViewById(R.id.qsinbox);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup9);
        op1 = (RadioButton) findViewById(R.id.rb1);
        op2 = (RadioButton) findViewById(R.id.rb2);
        op3 = (RadioButton) findViewById(R.id.rb3);
        next = (MaterialButton) findViewById(R.id.submit_question);

        T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        timer++;
                        time.setText(String.valueOf(timer));
                        if(timer == quiz.getSec()){
                            Toast.makeText(Test.this, "Time Over ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Test.this, Marks.class));
                        }
                    }
                });
            }
        }, 1000, 1000 );

        final Adapter adapter = new Adapter(questionList, 0);
        rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent event) {
                View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
                long duration = event.getEventTime() - event.getDownTime();
                if (event.getAction() == MotionEvent.ACTION_UP && duration < 150) {
                    int pos = recyclerView.getChildAdapterPosition(child);
                    System.out.println("\n pos = "+ pos);
                    if(pos >= 0) {
                        setQuestion(pos);
                        Adapter adapter1 = new Adapter(questionList, pos);
                        rv.setAdapter(adapter1);
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        setQuestion(q);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb1:
                        if(((RadioButton) findViewById(R.id.rb1)).getText().toString().equals(questionList.get(q).getAnswer()))
                            marks += 1;
                        break;

                    case R.id.rb2:
                        if(((RadioButton) findViewById(R.id.rb2)).getText().toString().equals(questionList.get(q).getAnswer()))
                            marks += 1;
                        break;

                    case R.id.rb3:
                        if(((RadioButton) findViewById(R.id.rb3)).getText().toString().equals(questionList.get(q).getAnswer()))
                            marks += 1;
                        break;
                }
                q += 1;
                setQuestion(q);
                Adapter adapter1 = new Adapter(questionList, q);
                rv.setAdapter(adapter1);
            }
        });

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void teminate(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/students/" + SplashScreen.user.getUid() + "/quizes");
        ref.child(quiz.qid).setValue(marks + " / " +questionList.size());

        DatabaseReference updateQuiz = FirebaseDatabase.getInstance().getReference("quiz/");
        updateQuiz.child(quiz.topic).child(quiz.creatorid).child("users").child(SplashScreen.user.getUid()).setValue(marks + " / " +questionList.size());

        Toast.makeText(Test.this, "You are quiz Completed! ", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Test.this, Marks.class));
    }

    void setQuestion(int position){
        if(position < questionList.size()) {
            if(position == questionList.size() - 1)
                next.setText("Submit");
            else
                next.setText("Save & Next");

            ContainerClass.Question quest = questionList.get(position);

            question_no.setText((position+1) + ".");
            question.setText(quest.getQuestion());
            op1.setText(quest.getOptions().get(0));
            op2.setText(quest.getOptions().get(1));
            op3.setText(quest.getOptions().get(2));
        }else{
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/students/" + SplashScreen.user.getUid() + "/quizes");
            ref.child(quiz.qid).setValue(marks + " / " +questionList.size());

            DatabaseReference updateQuiz = FirebaseDatabase.getInstance().getReference("quiz/");
            updateQuiz.child(quiz.getTopic()).child(quiz.getCreatorid()).child("users").child(SplashScreen.user.getUid()).setValue(marks + " / " +questionList.size());

            Toast.makeText(Test.this, "Quiz Completed! ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Test.this, Marks.class));
        }
    }


    public class Adapter extends RecyclerView.Adapter<Adapter.ProductViewHolder> {

        private List<ContainerClass.Question> list;
        private int pos;


        public Adapter(List<ContainerClass.Question> list, int pos) {
            this.list = list;
            this.pos = pos;
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(Test.this);
            View view = inflater.inflate(R.layout.card_qno, parent , false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductViewHolder holder, final int position) {
            //getting the product of the specified position
            final ContainerClass.Question question = list.get(position);

            holder.q_no.setText(String.valueOf(position+1));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setQuestion(position);
                }
            });
            if (position == pos) {
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(Test.this, R.color.orange));
                holder.q_no.setTextColor(ContextCompat.getColor(Test.this, R.color.white));
            } else {
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(Test.this, R.color.white));
                holder.q_no.setTextColor(ContextCompat.getColor(Test.this, R.color.orange));
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        class ProductViewHolder extends RecyclerView.ViewHolder {

            TextView q_no;
            CardView cardView;

            public ProductViewHolder(View itemView) {
                super(itemView);

                q_no = itemView.findViewById(R.id.qno);
                cardView = itemView.findViewById(R.id.cardView);
            }
        }
    }
}