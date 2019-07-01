package com.kelompok11.e_stugether.adapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kelompok11.e_stugether.CudKelasActivity;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.model.MateriModel;
import com.kelompok11.e_stugether.model.UsersModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.MateriViewHolder> {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<MateriModel> dataList;
    private ArrayList<String> materiKeyList;
    private FirebaseStorage firebaseStorage;

    public MateriAdapter(ArrayList<String> materiKeyList) {
        this.materiKeyList = materiKeyList;
    }

    @NonNull
    @Override
    public MateriViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        firebaseStorage = FirebaseStorage.getInstance();
        View view = layoutInflater.inflate(R.layout.item_materi,viewGroup,false);
        return new MateriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MateriViewHolder materiViewHolder, int i) {
        databaseReference.child("Materi").orderByKey().equalTo(materiKeyList.get(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    final MateriModel materiModel = dataSnapshot1.getValue(MateriModel.class);
                    materiViewHolder.tvJudulMateri.setText(materiModel.getJudul());
                    materiViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            File storagePath = new File(Environment.getExternalStorageDirectory(),"Estugether/Document");
                            if(!storagePath.exists()){
                                storagePath.mkdirs();
                            }
                            StorageReference storageReference = firebaseStorage.getReferenceFromUrl(materiModel.getLink());
                            final File localFile = new File(storagePath,storageReference.getName());
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(materiViewHolder.itemView.getContext(),"Berhasil didownload",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setAction(Intent.ACTION_VIEW);
                                    String type = "application/msword";
                                    intent.setDataAndType(Uri.fromFile(localFile),type);
                                    materiViewHolder.itemView.getContext().startActivity(intent);
                                }
                            });
                        }
                    });
                    databaseReference.child("Users").child(materiModel.getPengupload()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UsersModel usersModel = dataSnapshot.getValue(UsersModel.class);
                            materiViewHolder.tvPengupload.setText(usersModel.getUsername());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

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
        return (materiKeyList!= null)?materiKeyList.size():0;
    }
    public class MateriViewHolder extends RecyclerView.ViewHolder{
        private TextView tvJudulMateri,tvPengupload,tvLink;


        public MateriViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudulMateri = itemView.findViewById(R.id.tvJudulMateri);
            tvPengupload = itemView.findViewById(R.id.tvPengupload);
            tvLink = itemView.findViewById(R.id.tvLink);
        }
    }
}
