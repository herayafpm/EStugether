package com.kelompok11.e_stugether.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.model.UsersModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {
    private DatabaseReference databaseReference;
    private String userUid;
    private String email;
    private TextView tvUsername,tvEmail;
    private CircleImageView circleImageView;
    private FirebaseUser firebaseUser;
    private LinearLayout linearLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.profile_user,container,false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userUid = firebaseUser.getUid();
        email = firebaseUser.getEmail();
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        circleImageView = view.findViewById(R.id.imageUser);
        linearLayout = view.findViewById(R.id.lvUsername2);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.child("Users").child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final UsersModel usersModel = dataSnapshot.getValue(UsersModel.class);
                tvUsername.setText(usersModel.getUsername());
                tvEmail.setText(usersModel.getEmail());
                Glide.with(getActivity().getApplicationContext()).load(usersModel.getImageUrl()).into(circleImageView);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        final EditText etUsername = new EditText(getContext());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                        etUsername.setLayoutParams(layoutParams);
                        etUsername.setText(usersModel.getUsername());
                        builder.setTitle("Ubah Username")
                                .setView(etUsername)
                                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        databaseReference.child("Users").child(userUid).child("username").setValue(etUsername.getText().toString());
                                    }
                                })
                                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
