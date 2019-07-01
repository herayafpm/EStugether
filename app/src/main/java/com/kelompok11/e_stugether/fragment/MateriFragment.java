package com.kelompok11.e_stugether.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kelompok11.e_stugether.KelasActivity;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.adapter.MateriAdapter;
import com.kelompok11.e_stugether.model.ListMateriModel;
import com.kelompok11.e_stugether.model.MateriModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MateriFragment extends Fragment {
    private DatabaseReference databaseReference;
   private RecyclerView recyclerView;
   private MateriAdapter adapter;
   private ArrayList<String> materiKeyList;
   private String kelasKey;

    public MateriFragment() {
    }

    public void setKelasKey(String kelasKey) {
        this.kelasKey = kelasKey;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.fr_materi,container,false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = rootView.findViewById(R.id.list_materi);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        addData();
    }
    void addData(){
        databaseReference.child("Kelas").child(kelasKey).child("Materi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                materiKeyList = new ArrayList<>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    materiKeyList.add(dataSnapshot1.getKey());
                }
                adapter = new MateriAdapter(materiKeyList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
