package com.java.prj.quiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextInputEditText qid, topic;
    public static MaterialButton search;
    ProgressBar progressBar;

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        if(MainActivity.title != null)
            MainActivity.title.setText("Home");
        qid = (TextInputEditText) view.findViewById(R.id.qid);
        topic = (TextInputEditText) view.findViewById(R.id.topic);
        search = (MaterialButton) view.findViewById(R.id.search);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qid.getText().toString().isEmpty())
                    qid.setError("Please enter the unique quiz id");
                if(topic.getText().toString().isEmpty())
                    topic.setError("Please enter the quiz topic");

                if(!qid.getText().toString().isEmpty() && !topic.getText().toString().isEmpty()){
                    search.setVisibility(View.INVISIBLE);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("quiz");
                    ref.child(topic.getText().toString()).child(qid.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot != null){
                                MainActivity.quiz = snapshot.getValue(ContainerClass.Quiz.class);
                                startActivity(new Intent(getActivity(), Rules.class));
                            }else
                                Toast.makeText(getActivity(), "Please Enter valid Quiz Topic and ID", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        return view;
    }
}