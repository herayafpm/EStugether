package com.kelompok11.e_stugether.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok11.e_stugether.PelajaranActivity;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.model.MateriModel;
import com.kelompok11.e_stugether.model.PelajaranModel;

import java.util.ArrayList;

public class PelajaranAdapter extends RecyclerView.Adapter<PelajaranAdapter.PelajaranViewHolder> {
    private ArrayList<String> pelajaranKeyList;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public PelajaranAdapter(ArrayList<String> pelajaranKeyList) {
        this.pelajaranKeyList = pelajaranKeyList;
    }

    @NonNull
    @Override
    public PelajaranViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_pelajaran,viewGroup,false);
        return new PelajaranViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PelajaranAdapter.PelajaranViewHolder pelajaranViewHolder, int i) {
        databaseReference.child("Pelajaran").orderByKey().equalTo(pelajaranKeyList.get(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    PelajaranModel pelajaranModel = dataSnapshot1.getValue(PelajaranModel.class);
                    pelajaranViewHolder.tvJudulPelajaran.setText(pelajaranModel.getJudulPelajaran());
                    pelajaranViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(pelajaranViewHolder.itemView.getContext(), PelajaranActivity.class);
                            intent.putExtra("pelajaranKey",dataSnapshot1.getKey());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pelajaranViewHolder.itemView.getContext().startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return (pelajaranKeyList != null)?pelajaranKeyList.size():0;
    }
    public class PelajaranViewHolder extends RecyclerView.ViewHolder{
        private TextView tvJudulPelajaran;

        public PelajaranViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudulPelajaran = itemView.findViewById(R.id.tvJudulPelajaran);
        }
    }
}
