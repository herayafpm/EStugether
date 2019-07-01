package com.kelompok11.e_stugether;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kelompok11.e_stugether.fragment.LoginFragment;
import com.kelompok11.e_stugether.fragment.MainFragment;
import com.kelompok11.e_stugether.fragment.RegisterFragment;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuth;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void loadFragment(Fragment fr){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.emptyActivity,fr);
        ft.addToBackStack("tag");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void btnMasuk(View view) {
        loadFragment(new LoginFragment());
    }

    public void btnDaftar(View view) {
        loadFragment(new RegisterFragment());
    }
}
