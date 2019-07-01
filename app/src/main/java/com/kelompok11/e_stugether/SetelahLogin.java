package com.kelompok11.e_stugether;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.module.LibraryGlideModule;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kelompok11.e_stugether.fragment.DashboardFragment;
import com.kelompok11.e_stugether.fragment.UserFragment;
import com.kelompok11.e_stugether.model.UsersModel;

import java.util.HashMap;

public class SetelahLogin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuth;
    private FirebaseUser firebaseUser;
    private TextView tvUsername,tvRole;
    private ImageView imageView;
    private String UserKey;
    private StorageReference storageReference;
    private NavigationView navigationView;
    FloatingActionButton fab;
    private Switch aSwitch;

    public String getUserKey() {
        return UserKey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setelah_login);
        firebaseAuth = FirebaseAuth.getInstance();
        fab = findViewById(R.id.fab);
        fab.hide();
        aSwitch = findViewById(R.id.switchPengajar);
        fab.hide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        autentikasi();
        loadFragment(new DashboardFragment());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView= findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        autentikasi();
    }


    public void autentikasi(){
        mAuth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser == null) {
                    Intent intent = new Intent(SetelahLogin.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }else{
                    UserKey = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference();
                    storageReference = FirebaseStorage.getInstance().getReference();
                    reference.child("Users").child(UserKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            UsersModel usersModel = dataSnapshot.getValue(UsersModel.class);
                            tvUsername = findViewById(R.id.tvUsername);
                            tvRole = findViewById(R.id.tvRole);
                            imageView = findViewById(R.id.imageView);
                             Glide.with(getApplicationContext()).load(usersModel.getImageUrl()).into(imageView);

                            tvUsername.setText(""+usersModel.getUsername());
                            tvRole.setText(""+usersModel.getRole());

                            if(usersModel.getRole().equals("Pengajar")) {
                                fab.show();
                                aSwitch.setChecked(true);
                            }else{
                                fab.hide();
                                aSwitch.setChecked(false);
                            }
                            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(isChecked){
                                        reference.child("Users").child(dataSnapshot.getKey()).child("role").setValue("Pengajar");
                                        finishAffinity();
                                        Intent intent = new Intent(getApplicationContext(),SetelahLogin.class);
                                        startActivity(intent);
                                    }else{
                                        reference.child("Users").child(dataSnapshot.getKey()).child("role").setValue("Pelajar");
                                        finishAffinity();
                                        Intent intent = new Intent(getApplicationContext(),SetelahLogin.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SetelahLogin.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.buat_kelas,null);
                            final EditText etNamaKelas = dialogView.findViewById(R.id.etNamaKelas);
                            final EditText etPasswordKelas = dialogView.findViewById(R.id.etPasswordKelas);
                            builder.setTitle("Buat Kelas")
                                    .setView(dialogView)
                                    .setPositiveButton("Buat", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            HashMap<String,Object> hashMap = new HashMap<>();
                                            hashMap.put("namaKelas",etNamaKelas.getText().toString());
                                            hashMap.put("passwordKelas",etPasswordKelas.getText().toString());
                                            hashMap.put("keyKelasMaker",UserKey);

                                            reference.child("Kelas").push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(),"Kelas Berhasil Ditambahkan",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();


                        }
                    });
                }
            }
        };
        firebaseAuth.addAuthStateListener(mAuth);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        autentikasi();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            loadFragment(new DashboardFragment());
        } else if (id == R.id.nav_semua_kelas) {
            DashboardFragment dashboardFragment = new DashboardFragment();
            dashboardFragment.setType("Kelas");
            loadFragment(dashboardFragment);
        } else if (id == R.id.nav_keluar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Keluar")
                    .setMessage("Yakin ingin keluar?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void loadFragment(Fragment fr){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.navDrawerActivity,fr);
        ft.commitAllowingStateLoss();
    }

    public void btnDetail(View view) {
        for(int i = 0;i < navigationView.getMenu().size();i++){
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        loadFragment(new UserFragment());
        onBackPressed();
    }
}
