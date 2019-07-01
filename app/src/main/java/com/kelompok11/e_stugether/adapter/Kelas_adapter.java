package com.kelompok11.e_stugether.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok11.e_stugether.KelasActivity;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.model.KelasModel;
import com.kelompok11.e_stugether.model.MateriModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Kelas_adapter extends RecyclerView.Adapter<Kelas_adapter.KelasViewHolder> {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<String> dataList;
    private EditText etPasswordKelas;

    public Kelas_adapter(ArrayList<String> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public KelasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_kelas,viewGroup,false);
        return new KelasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KelasViewHolder kelasViewHolder, final int i) {
        final String getUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        kelasViewHolder.btnChoiceClass.setVisibility(View.INVISIBLE);
        kelasViewHolder.btnChoiceClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(kelasViewHolder.itemView.getContext());
                String items[] ={"Keluar Kelas"};
                builder.setTitle("Pilihan")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        databaseReference.child("Kelas").child(dataList.get(i)).child("Anggota").child(getUid).removeValue();
                                        databaseReference.child("Users").child(getUid).child("Kelas").child(dataList.get(i)).removeValue();
                                        break;
                                }
                            }
                        }).setNeutralButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        databaseReference.child("Kelas").child(dataList.get(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final KelasModel kelasModel = dataSnapshot.getValue(KelasModel.class);
                kelasViewHolder.tvNamaKelas.setText(kelasModel.getNamaKelas());
                databaseReference.child("Kelas").child(dataList.get(i)).child("Anggota").orderByKey().equalTo(getUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            kelasViewHolder.btnChoiceClass.setVisibility(View.VISIBLE);
                        }
                        kelasViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(dataSnapshot.exists()){
                                    Intent intent = new Intent(kelasViewHolder.itemView.getContext(),KelasActivity.class);
                                    intent.putExtra("kelasKey",dataList.get(i));
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    kelasViewHolder.itemView.getContext().startActivity(intent);
                                }else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(kelasViewHolder.itemView.getContext());
                                    LayoutInflater inflater = LayoutInflater.from(kelasViewHolder.itemView.getContext());
                                    View view = inflater.inflate(R.layout.ikut_kelas,null);
                                    etPasswordKelas = view.findViewById(R.id.etPasswordKelas);
                                    builder.setTitle("Ikut kelas "+kelasModel.getNamaKelas())
                                            .setView(view)
                                            .setPositiveButton("Ikut", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    databaseReference.child("Kelas").orderByKey().equalTo(dataList.get(i)).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            String password = null;
                                                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                                                KelasModel kelasModel = dataSnapshot1.getValue(KelasModel.class);
                                                                password = kelasModel.getPasswordKelas();

                                                            }
                                                            if(password.equals(etPasswordKelas.getText().toString())){
                                                                Map<String,Object> childUpdates = new HashMap<>();
                                                                childUpdates.put("/Users/"+getUid+"/Kelas/"+dataList.get(i),true);
                                                                childUpdates.put("/Kelas/"+dataList.get(i)+"/Anggota/"+getUid,true);
                                                                databaseReference.updateChildren(childUpdates);
                                                            }else{
                                                                Snackbar.make(kelasViewHolder.itemView,"Password Salah",Snackbar.LENGTH_LONG).show();
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return (dataList !=null)?dataList.size():0;
    }
    public class KelasViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNamaKelas;
        private Button btnChoiceClass;

        public KelasViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.btnChoiceClass  = itemView.findViewById(R.id.btnChoiceClass);
            this.tvNamaKelas = itemView.findViewById(R.id.tvNamaKelas);
        }
    }
}
