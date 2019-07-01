package com.kelompok11.e_stugether.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelompok11.e_stugether.MainActivity;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.model.UsersModel;

import java.util.HashMap;

public class RegisterFragment extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private EditText etUsername,etEmail,etPassword;
    private Button btnDaftar;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_register,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(view.getContext());
        etUsername = getView().findViewById(R.id.etUsername);
        etEmail = getView().findViewById(R.id.etEmail);
        etPassword = getView().findViewById(R.id.etPassword);
        btnDaftar = getView().findViewById(R.id.btnDaftar);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    return true;
                }
                return false;
            }
        });
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nim = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if(TextUtils.isEmpty(nim)||TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(getContext(),"All field are required!",Toast.LENGTH_SHORT).show();
                }
                else{
                    showProgressDialog();
                    register(nim,email,password);
                }
            }
        });
        auth = FirebaseAuth.getInstance();
        super.onViewCreated(view, savedInstanceState);
    }
    private void register(final String username, final String email, String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    final FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userId = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    UsersModel usersModel = new UsersModel(username,"Pelajar","https://firebasestorage.googleapis.com/v0/b/estugether.appspot.com/o/gambar%2Fdefault_user.jpg?alt=media&token=63f7c626-d803-4faa-9755-6618edce66b6",email);
                    reference.setValue(usersModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(),"Berhasil Mendaftar, Silahkan Login",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(),"Email yang anda masukan telah terdaftar",Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }
        });

    }
    public void Masuk(){
        FragmentTransaction ft = RegisterFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.emptyActivity,new LoginFragment());
        ft.commit();
    }
    private void showProgressDialog(){
        progressDialog.setMessage("Mendaftar ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void hideProgressDialog(){
        progressDialog.dismiss();
    }

}
