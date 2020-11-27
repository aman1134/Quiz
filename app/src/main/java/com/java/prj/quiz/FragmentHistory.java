package com.java.prj.quiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHistory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    List<List<String>> hList;

    public FragmentHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHistory newInstance(String param1, String param2) {
        FragmentHistory fragment = new FragmentHistory();
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
        MainActivity.title.setText("History");
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/students");
        ref.child(SplashScreen.user.getUid()).child("quizes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hList = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    List<String> l = new ArrayList<>();
                    l.add(ds.getKey());
                    l.add(ds.getValue(String.class));

                    hList.add(l);
                }
                Adapter adapter = new Adapter(hList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return  view;
    }

    public class Adapter extends RecyclerView.Adapter<FragmentHistory.Adapter.ProductViewHolder> {

        private List<List<String>> list;


        public Adapter(List<List<String>> list) {
            this.list = list;
        }

        @Override
        public FragmentHistory.Adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.card_history, parent , false);
            return new FragmentHistory.Adapter.ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FragmentHistory.Adapter.ProductViewHolder holder, final int position) {
            //getting the product of the specified position

            holder.topic.setText(list.get(position).get(0));
            holder.marks.setText(list.get(position).get(1));//ruk mei bs kuch colors paste krri hu apne vale se//okk
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        class ProductViewHolder extends RecyclerView.ViewHolder {

            TextView topic, marks;

            public ProductViewHolder(View itemView) {
                super(itemView);

                topic = itemView.findViewById(R.id.topic);
                marks = itemView.findViewById(R.id.marks);
            }
        }
    }

}