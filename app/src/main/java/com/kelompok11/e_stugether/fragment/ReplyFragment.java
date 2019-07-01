package com.kelompok11.e_stugether.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kelompok11.e_stugether.PelajaranActivity;
import com.kelompok11.e_stugether.R;

public class ReplyFragment extends Fragment {
    private TextView tvNamaPengirim,tvPesan;
    private Button btnBatal;
    private String pembahasan;
    private String namaPengirim;

    public ReplyFragment() {
    }

    public void setNamaPengirim(String namaPengirim) {
        this.namaPengirim = namaPengirim;
    }

    public void setPembahasan(String pembahasan) {
        this.pembahasan = pembahasan;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.item_reply,container,false);
        tvNamaPengirim = view.findViewById(R.id.tvNamaPengirim);
        tvPesan = view.findViewById(R.id.tvPesan);
        btnBatal = view.findViewById(R.id.btnBatal);
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
            }
        });
        tvNamaPengirim.setText(namaPengirim);
        tvPesan.setText(pembahasan);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
