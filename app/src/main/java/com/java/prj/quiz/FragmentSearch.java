package com.java.prj.quiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearch extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView topic;
    List<String> topicList;
    RecyclerView recyclerView;

    public FragmentSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSearch newInstance(String param1, String param2) {
        FragmentSearch fragment = new FragmentSearch();
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
        MainActivity.title.setText("Search Quiz Topic");
        FragmentHome.search.setVisibility(View.INVISIBLE);

        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        topic = (TextView) view.findViewById(R.id.topic);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        topic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if(s.length() >= 3){
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("quiz");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            topicList = new ArrayList<>();
                            for(DataSnapshot ds : snapshot.getChildren()){
                                String str = ds.getKey();
                                if(str.contains(s))
                                    topicList.add(str);
                            }
                            Adapter adapter = new Adapter(topicList);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    public class Adapter extends RecyclerView.Adapter<FragmentSearch.Adapter.ProductViewHolder> {

        private List<String> list;


        public Adapter(List<String> list) {
            this.list = list;
        }

        @Override
        public FragmentSearch.Adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.card_topic, parent , false);
            return new FragmentSearch.Adapter.ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FragmentSearch.Adapter.ProductViewHolder holder, final int position) {
            //getting the product of the specified position

            holder.topic.setText(list.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Authors.class);
                    intent.putExtra("topic", list.get(position));
                    startActivity(intent);
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