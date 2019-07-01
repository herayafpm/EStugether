package com.kelompok11.e_stugether.fragment;

import android.graphics.drawable.Drawable;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.SetelahLogin;
import com.kelompok11.e_stugether.adapter.AnggotaAdapter;
import com.kelompok11.e_stugether.adapter.MateriAdapter;
import com.kelompok11.e_stugether.model.UsersModel;

import java.util.ArrayList;

public class AnggotaFragment extends Fragment {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private AnggotaAdapter anggotaAdapter;
    private ArrayList<UsersModel> usersModelArrayList;
    private ArrayList<String> userKeyList;
    private String kelasKey;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_anggota,container,false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = view.findViewById(R.id.list_anggota);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        addData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public AnggotaFragment() {
    }

    public void setKelasKey(String kelasKey) {
        this.kelasKey = kelasKey;
    }
    private void addData(){
        databaseReference.child("Kelas").child(kelasKey).child("Anggota").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userKeyList = new ArrayList<>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    userKeyList.add(dataSnapshot1.getKey());
                }
                anggotaAdapter = new AnggotaAdapter(userKeyList);
                recyclerView.setAdapter(anggotaAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
