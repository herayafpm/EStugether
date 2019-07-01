package com.kelompok11.e_stugether.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.model.UsersModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnggotaAdapter extends RecyclerView.Adapter<AnggotaAdapter.AnggotaViewHolder> {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<String> userKeyList;

    public AnggotaAdapter(ArrayList<String> userKeyList) {
        this.userKeyList= userKeyList;
    }

    @NonNull
    @Override
    public AnggotaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_anggota,viewGroup,false);

        return new AnggotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnggotaViewHolder anggotaViewHolder, int i) {
        databaseReference.child("Users").orderByKey().equalTo(userKeyList.get(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    UsersModel usersModel = dataSnapshot1.getValue(UsersModel.class);
                    try{
                        Glide.with(anggotaViewHolder.itemView.getContext()).load(usersModel.getImageUrl()).into(anggotaViewHolder.circleImageView);
                    }catch (Exception e){
                        Log.d("Tag","Hasil "+e);
                    }
                    anggotaViewHolder.tvNamaAnggota.setText(usersModel.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return (userKeyList != null)?userKeyList.size():0;
    }

    public class AnggotaViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNamaAnggota;
        private CircleImageView circleImageView;

        public AnggotaViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.imageUser);
            tvNamaAnggota = itemView.findViewById(R.id.tvNamaAnggota);
        }
    }

}
