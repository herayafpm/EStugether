package com.kelompok11.e_stugether.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelompok11.e_stugether.CudKelasActivity;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.model.PelajaranModel;
import com.kelompok11.e_stugether.model.UsersModel;

import java.util.HashMap;
import java.util.Map;

public class CudPelajaranFragment extends Fragment {
    private String type = "";
    private String key;
    private EditText etJudulPelajaran,etPembahasan;
    private DatabaseReference databaseReference;
    private Button btnSimpan,btnBatal;
    public CudPelajaranFragment() {
    }

    public void setType(String type) {
        this.type = type;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fr_cud_pelajaran,container,false);
        etJudulPelajaran = view.findViewById(R.id.etJudulPelajaran);
        etPembahasan = view.findViewById(R.id.etPembahasan);
        btnSimpan = view.findViewById(R.id.btnSimpan);
        btnBatal = view.findViewById(R.id.btnBatal);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if(type.equals("Tambah")){
            ((CudKelasActivity)getActivity()).setActionBarTitle("Tambah Pelajaran");
            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(etJudulPelajaran.getText())||TextUtils.isEmpty(etPembahasan.getText())){
                        Toast.makeText(getContext(),"Semua data harus diisi",Toast.LENGTH_SHORT).show();
                    }else{
                        String key = databaseReference.child("Pelajaran").push().getKey();
                        String kelasKey = ((CudKelasActivity)getActivity()).getKey();
                        PelajaranModel pelajaranModel = new PelajaranModel(etJudulPelajaran.getText().toString(),etPembahasan.getText().toString());
                        Map<String,Object> pelajaranValues = pelajaranModel.toMap();
                        Map<String,Object> result = new HashMap<>();
                        result.put("/Pelajaran/"+key,pelajaranValues);
                        result.put("/Kelas/"+kelasKey+"/Pelajaran/"+key,true);
                        databaseReference.updateChildren(result);
                        Toast.makeText(getContext(),"Berhasil menyimpan pelajaran",Toast.LENGTH_SHORT).show();
                        getActivity().getFragmentManager().popBackStack();
                    }

                }
            });
            btnBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getFragmentManager().popBackStack();
                }
            });

        }else if(type.equals("Ubah")){
            ((CudKelasActivity)getActivity()).setActionBarTitle("Ubah Pelajaran");
        }
        return view;
    }
}
