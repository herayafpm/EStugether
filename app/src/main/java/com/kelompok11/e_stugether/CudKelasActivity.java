package com.kelompok11.e_stugether;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kelompok11.e_stugether.fragment.CudMateriFragment;
import com.kelompok11.e_stugether.fragment.CudPelajaranFragment;

public class CudKelasActivity extends AppCompatActivity {
    private String tab;
    private String title;
    private String key;

    public String getKey() {
        return key;
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cud_kelas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        key = getIntent().getExtras().get("kelasKey").toString();
        tab =  getIntent().getExtras().get("case").toString();
        switch(tab){
            case "Materi":
                CudMateriFragment cudMateriFragment = new CudMateriFragment();
                cudMateriFragment.setType("Tambah");
                loadFragment(cudMateriFragment);
                break;
            case "Pelajaran":
                CudPelajaranFragment cudPelajaranFragment = new CudPelajaranFragment();
                cudPelajaranFragment.setType("Tambah");
                loadFragment(cudPelajaranFragment);
                break;
        }
    }
    private void loadFragment(Fragment fr){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.emptyActivity,fr);
        ft.commitAllowingStateLoss();
    }

}
