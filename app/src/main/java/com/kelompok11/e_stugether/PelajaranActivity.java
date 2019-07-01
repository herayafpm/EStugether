package com.kelompok11.e_stugether;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok11.e_stugether.adapter.PesanAdapter;
import com.kelompok11.e_stugether.fragment.ReplyFragment;
import com.kelompok11.e_stugether.model.PelajaranModel;
import com.kelompok11.e_stugether.model.PesanModel;
import com.kelompok11.e_stugether.model.UsersModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PelajaranActivity extends AppCompatActivity {
    private String pelajaranKey;
    private DatabaseReference databaseReference;
    private EditText etPesan;
    private TextView tvPengajar,tvPembahasan;
    private ArrayList<String> pesanKeyList;
    private ArrayList<PesanModel> pesanModels;
    private RecyclerView recyclerView;
    private PesanAdapter pesanAdapter;
    private Button btnReply;
    private FrameLayout frameLayout;
    private ActionBar actionBar;
    public String idReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelajaran);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        pelajaranKey = getIntent().getExtras().get("pelajaranKey").toString();
        etPesan = findViewById(R.id.etPesan);
        tvPengajar = findViewById(R.id.tvPengajar);
        tvPembahasan = findViewById(R.id.tvPembahasan);
        recyclerView = findViewById(R.id.list_pesan);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        btnReply = findViewById(R.id.btnReply);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PelajaranActivity.super.onBackPressed();
            }
        });

        frameLayout = findViewById(R.id.replyLayout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Pelajaran").child(pelajaranKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final PelajaranModel pelajaranModel = dataSnapshot.getValue(PelajaranModel.class);
                    setTitle(pelajaranModel.getJudulPelajaran());
                    tvPembahasan.setText(pelajaranModel.getPembahasan());
                    btnReply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ReplyFragment replyFragment = new ReplyFragment();
                            replyFragment.setPembahasan(pelajaranModel.getPembahasan());
                            loadFragment(replyFragment);
                            idReply = pelajaranKey;

                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Pelajaran").child(pelajaranKey).child("Pesan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                pesanModels = new ArrayList<>();
                for(final DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    PesanModel pesanModel = dataSnapshot1.getValue(PesanModel.class);
                    pesanModel.setIdPesan(dataSnapshot1.getKey());
                    pesanModels.add(pesanModel);
                }
                pesanAdapter = new PesanAdapter(pesanModels,pelajaranKey);
                recyclerView.setAdapter(pesanAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void btnSend(View view) {
        String getUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String key = databaseReference.child("Pelajaran").child(pelajaranKey).push().getKey();
        PesanModel pesanModel = new PesanModel(getUid,idReply,etPesan.getText().toString(),System.currentTimeMillis());
        Map<String,Object> pesanValues = pesanModel.toMap();
        Map<String,Object> childUpdates = new HashMap<>();
        childUpdates.put("/Pelajaran/"+pelajaranKey+"/Pesan/"+key,pesanValues);
        databaseReference.updateChildren(childUpdates);
        frameLayout.setVisibility(View.GONE);
        idReply = null;
        etPesan.setText(null);

    }
    public void loadFragment(Fragment fr){
        frameLayout.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.replyLayout,fr);
        fragmentTransaction.commit();
    }
}
