package com.kelompok11.e_stugether;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok11.e_stugether.fragment.CudMateriFragment;
import com.kelompok11.e_stugether.model.KelasModel;
import com.kelompok11.e_stugether.model.MateriModel;
import com.kelompok11.e_stugether.model.UsersModel;
import com.kelompok11.e_stugether.ui.main.SectionsPagerAdapter;

import java.util.HashMap;
import java.util.Map;

public class KelasActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private String getUid;
    private String kelasKey;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private FloatingActionButton floatingActionButton;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelas);
        kelasKey = getIntent().getExtras().get("kelasKey").toString();
        toolbar = findViewById(R.id.toolBarKelas);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KelasActivity.super.onBackPressed();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(kelasKey,this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.hide();
        getUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("Users").child(getUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersModel usersModel = dataSnapshot.getValue(UsersModel.class);
                if(usersModel.getRole().equals("Pengajar")){
                    floatingActionButton.show();
                }else{
                    floatingActionButton.hide();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setFloatingActionButton("Materi");
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_kelas,menu);
        databaseReference.child("Kelas").child(kelasKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KelasModel kelasModel = dataSnapshot.getValue(KelasModel.class);
                if(kelasModel.getIdPengajar().equals(getUid)){
                    MenuItem item = menu.findItem(R.id.ubahKelas);
                    item.setVisible(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.ubahKelas) {

        }
        else if(id == R.id.keluarKelas){
            AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
            builder.setTitle("Anda yakin ingin keluar kelas?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseReference.child("Kelas").child(kelasKey).child("Anggota").child(getUid).removeValue();
                            databaseReference.child("Users").child(getUid).child("Kelas").child(kelasKey).removeValue();
                        }
                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.child("Kelas").child(kelasKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KelasModel kelasModel = dataSnapshot.getValue(KelasModel.class);
                setTitle(kelasModel.getNamaKelas());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                databaseReference.child("Users").child(getUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UsersModel usersModel = dataSnapshot.getValue(UsersModel.class);
                        if(usersModel.getRole().equals("Pengajar")){
                            switch (tab.getPosition()) {
                                case 0:
                                    floatingActionButton.show();
                                    setFloatingActionButton("Materi");
                                    break;
                                case 1:
                                    floatingActionButton.show();
                                    setFloatingActionButton("Pelajaran");
                                    break;
                                case 2:
                                    floatingActionButton.hide();
                                    setFloatingActionButton("Anggota");
                                    break;
                            }
                        }else{
                            floatingActionButton.hide();
                            switch (tab.getPosition()) {
                                case 0:
                                    setFloatingActionButton("Materi");
                                    break;
                                case 1:
                                    setFloatingActionButton("Pelajaran");
                                    break;
                                case 2:
                                    setFloatingActionButton("Anggota");
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    public void setFloatingActionButton(final String tab){

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CudKelasActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                switch (tab){
                    case "Materi":
                        intent.putExtra("kelasKey",kelasKey);
                        intent.putExtra("case",tab);
                        break;
                    case "Pelajaran":
                        intent.putExtra("kelasKey",kelasKey);
                        intent.putExtra("case",tab);
                        break;

                }
                startActivity(intent);
            }
        });
    }

}