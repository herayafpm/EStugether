package com.kelompok11.e_stugether.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.SetelahLogin;
import com.kelompok11.e_stugether.adapter.Kelas_adapter;
import com.kelompok11.e_stugether.model.KelasModel;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private Kelas_adapter adapter;
    private String type = "Kelasku";
    private ArrayList<String> kelasKeys;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public DashboardFragment() {
    }

    public void setType(String type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_dashboard,container,false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = rootView.findViewById(R.id.rvKelas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        addData();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    void addData() {
        if (type.equals("Kelasku")) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if(firebaseUser != null){
                String getUid = firebaseUser.getUid();
                databaseReference.child("Users").child(getUid).child("Kelas").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        kelasKeys = new ArrayList<>();
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                            kelasKeys.add(noteDataSnapshot.getKey());
                        }
                        adapter = new Kelas_adapter(kelasKeys);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
        else{
            databaseReference.child("Kelas").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    kelasKeys = new ArrayList<>();
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        kelasKeys.add(noteDataSnapshot.getKey());
                    }
                    adapter = new Kelas_adapter(kelasKeys);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
