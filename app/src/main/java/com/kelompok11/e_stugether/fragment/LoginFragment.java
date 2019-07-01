package com.kelompok11.e_stugether.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.kelompok11.e_stugether.MainActivity;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.SetelahLogin;

public class LoginFragment extends Fragment  {
    private Button btnDaftar;
    private Button btnMasuk;
    private ProgressDialog progressDialog;
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fr_login,container,false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(view.getContext());
        btnDaftar = getView().findViewById(R.id.btnDaftar);
        btnMasuk = getView().findViewById(R.id.btnMasuk);
        etEmail = getView().findViewById(R.id.etEmail);
        etPassword = getView().findViewById(R.id.etPassword);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    return true;
                }
                return false;
            }
        });
        auth = FirebaseAuth.getInstance();
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(getContext(),"All field are required!",Toast.LENGTH_SHORT).show();
                }
                else{
                    showProgressDialog();
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                getActivity().finishAffinity();
                                Intent intent = new Intent(getActivity(), SetelahLogin.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getContext(),"Email atau Password salah",Toast.LENGTH_SHORT).show();
                            }
                            hideProgressDialog();
                        }
                    });
                }
            }
        });

//        btnDaftar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction ft = LoginFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.emptyActivity,new RegisterFragment());
//                ft.commit();
//            }
//        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void showProgressDialog(){
        progressDialog.setMessage("Sedang Masuk ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void hideProgressDialog(){
        progressDialog.dismiss();
    }
}
