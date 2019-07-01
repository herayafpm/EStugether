package com.kelompok11.e_stugether.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.adapter.MateriAdapter;
import com.kelompok11.e_stugether.adapter.PelajaranAdapter;
import com.kelompok11.e_stugether.model.MateriModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PelajaranFragment extends Fragment {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private PelajaranAdapter pelajaranAdapter;
    private ArrayList<String> pelajaranKeyList;

    private String kelasKey;
    public PelajaranFragment() {
    }

    public void setKelasKey(String kelasKey) {
        this.kelasKey = kelasKey;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fr_pelajaran,container,false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
//        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(),"Pelajaran",Toast.LENGTH_LONG).show();
//            }
//        });
        recyclerView = view.findViewById(R.id.list_pelajaran);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        addData();
    }


    void addData(){
        databaseReference.child("Kelas").child(kelasKey).child("Pelajaran").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pelajaranKeyList = new ArrayList<>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    pelajaranKeyList.add(dataSnapshot1.getKey());
                }
                pelajaranAdapter = new PelajaranAdapter(pelajaranKeyList);
                recyclerView.setAdapter(pelajaranAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
