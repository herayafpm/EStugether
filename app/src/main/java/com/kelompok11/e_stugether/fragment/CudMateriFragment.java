package com.kelompok11.e_stugether.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kelompok11.e_stugether.CudKelasActivity;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.model.MateriModel;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class CudMateriFragment extends Fragment {
    private final static int pickDocCode = 1000;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String type = "";
    private EditText etJudulMateri;
    private String urlUpload ="";
    private TextView tvLink;
    private Button btnUpload,btnSimpan,btnBatal;
    private UploadTask uploadTask;
    public CudMateriFragment() {
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == pickDocCode&& resultCode == RESULT_OK&&data!= null&&data.getData() != null){
            if(data.getData()!=null){
                uploadFile(data.getData());
            }
            else{
                Toast.makeText(getContext(),"Tidak ada document yang dipilih",Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void uploadFile(Uri data){
       final StorageReference ref =  storageReference.child("Materi/"+System.currentTimeMillis());
       uploadTask = ref.putFile(data);
       Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
           @Override
           public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
               if(!task.isSuccessful()){
                   throw task.getException();
               }
               return ref.getDownloadUrl();
           }
       }).addOnCompleteListener(new OnCompleteListener<Uri>() {
           @Override
           public void onComplete(@NonNull Task<Uri> task) {
               if(task.isSuccessful()){
                   Uri downloadUri = task.getResult();
                   urlUpload = downloadUri.toString();
                   StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(urlUpload);
                   tvLink.setText(storageReference.getName());
               }else{

               }
           }
       });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fr_cud_materi,container,false);
        etJudulMateri = view.findViewById(R.id.etJudulMateri);
        tvLink = view.findViewById(R.id.tvLink);
        btnUpload = view.findViewById(R.id.btnUploadMateri);
        btnSimpan = view.findViewById(R.id.btnSimpan);
        btnBatal = view.findViewById(R.id.btnBatal);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if(type.equals("Tambah")){
            ((CudKelasActivity)getActivity()).setActionBarTitle("Tambah Materi");
            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("application/msword");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Pilih Document"),pickDocCode);
                }
            });
            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(etJudulMateri.getText())||urlUpload.equals("")){
                        Toast.makeText(getContext(),"Semua data dibutuhkan",Toast.LENGTH_SHORT).show();
                    }else{
                        String kelasKey = ((CudKelasActivity)getActivity()).getKey();
                        String key = databaseReference.child("Materi").push().getKey();
                        String getUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        MateriModel materiModel = new MateriModel(etJudulMateri.getText().toString(),getUid,urlUpload);
                        Map<String,Object> materiValues = materiModel.toMap();
                        Map<String,Object> result = new HashMap<>();
                        result.put("/Materi/"+key,materiValues);
                        result.put("/Kelas/"+kelasKey+"/Materi/"+key,true);
                        databaseReference.updateChildren(result);
                        getActivity().getFragmentManager().popBackStack();
                    }
                }
            });
            btnBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StorageReference materiReference = FirebaseStorage.getInstance().getReferenceFromUrl(urlUpload);
                    materiReference.delete();
                    getActivity().getFragmentManager().popBackStack();
                }
            });
        }
        return view;
    }

}
